package cl.truchoradios.chile.player

import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.audio.TeeAudioProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Builds a real, logarithmic frequency spectrum from ExoPlayer's decoded PCM stream.
 * The tee processor gives this sink a read-only copy, so analysis never alters playback.
 */
@UnstableApi
class AudioSpectrumAnalyzer : TeeAudioProcessor.AudioBufferSink {
    private val sampleWindow = DoubleArray(FFT_SIZE)
    private val smoothedBands = FloatArray(BAND_COUNT)
    private var samplesInWindow = 0
    private var sampleRateHz = DEFAULT_SAMPLE_RATE
    private var channelCount = 2
    private var encoding = C.ENCODING_PCM_16BIT

    private val _bands = MutableStateFlow(EMPTY_BANDS)
    val bands: StateFlow<List<Float>> = _bands.asStateFlow()

    override fun flush(sampleRateHz: Int, channelCount: Int, encoding: Int) {
        this.sampleRateHz = sampleRateHz.coerceAtLeast(1)
        this.channelCount = channelCount.coerceAtLeast(1)
        this.encoding = encoding
        samplesInWindow = 0
        smoothedBands.fill(0f)
        _bands.value = EMPTY_BANDS
    }

    override fun handleBuffer(buffer: ByteBuffer) {
        val bytesPerSample = bytesPerSample(encoding) ?: return
        val bytesPerFrame = bytesPerSample * channelCount
        val input = buffer.asReadOnlyBuffer().order(ByteOrder.nativeOrder())

        while (input.remaining() >= bytesPerFrame) {
            var monoSample = 0.0
            repeat(channelCount) {
                monoSample += readSample(input, encoding)
            }
            sampleWindow[samplesInWindow++] = monoSample / channelCount

            if (samplesInWindow == FFT_SIZE) {
                publishSpectrum()
                samplesInWindow = 0
            }
        }
    }

    private fun publishSpectrum() {
        val real = DoubleArray(FFT_SIZE)
        val imaginary = DoubleArray(FFT_SIZE)

        for (index in 0 until FFT_SIZE) {
            // Hann window reduces spectral leakage between adjacent frequency bands.
            val window = 0.5 - 0.5 * cos(2.0 * PI * index / (FFT_SIZE - 1))
            real[index] = sampleWindow[index] * window
        }
        fft(real, imaginary)

        val nyquist = sampleRateHz / 2.0
        val upperFrequency = minOf(MAX_FREQUENCY_HZ, nyquist)
        val frequencyRatio = (upperFrequency / MIN_FREQUENCY_HZ).coerceAtLeast(1.0)
        val nextBands = ArrayList<Float>(BAND_COUNT)

        for (band in 0 until BAND_COUNT) {
            val lowFrequency = MIN_FREQUENCY_HZ * frequencyRatio.pow(band.toDouble() / BAND_COUNT)
            val highFrequency = MIN_FREQUENCY_HZ * frequencyRatio.pow((band + 1.0) / BAND_COUNT)
            val lowBin = max(1, (lowFrequency * FFT_SIZE / sampleRateHz).toInt())
            val highBin = max(lowBin, (highFrequency * FFT_SIZE / sampleRateHz).toInt())
                .coerceAtMost(FFT_SIZE / 2)

            var peak = 0.0
            for (bin in lowBin..highBin) {
                peak = max(peak, sqrt(real[bin] * real[bin] + imaginary[bin] * imaginary[bin]))
            }

            val amplitude = peak / (FFT_SIZE / 2.0)
            val decibels = 20.0 * ln(amplitude.coerceAtLeast(0.000001)) / ln(10.0)
            val normalized = ((decibels + 60.0) / 60.0)
                .coerceIn(0.0, 1.0)
                .pow(0.72)
                .toFloat()
            val smoothing = if (normalized > smoothedBands[band]) 0.68f else 0.24f
            smoothedBands[band] += (normalized - smoothedBands[band]) * smoothing
            nextBands += smoothedBands[band]
        }

        _bands.value = nextBands
    }

    private fun readSample(buffer: ByteBuffer, encoding: Int): Double = when (encoding) {
        C.ENCODING_PCM_8BIT -> ((buffer.get().toInt() and 0xFF) - 128) / 128.0
        C.ENCODING_PCM_16BIT -> buffer.short / 32768.0
        C.ENCODING_PCM_24BIT -> {
            val low = buffer.get().toInt() and 0xFF
            val middle = buffer.get().toInt() and 0xFF
            val high = buffer.get().toInt()
            (low or (middle shl 8) or (high shl 16)) / 8388608.0
        }
        C.ENCODING_PCM_32BIT -> buffer.int / 2147483648.0
        C.ENCODING_PCM_FLOAT -> buffer.float.coerceIn(-1f, 1f).toDouble()
        else -> 0.0
    }

    private fun bytesPerSample(encoding: Int): Int? = when (encoding) {
        C.ENCODING_PCM_8BIT -> 1
        C.ENCODING_PCM_16BIT -> 2
        C.ENCODING_PCM_24BIT -> 3
        C.ENCODING_PCM_32BIT, C.ENCODING_PCM_FLOAT -> 4
        else -> null
    }

    /** In-place radix-2 Cooley-Tukey FFT. */
    private fun fft(real: DoubleArray, imaginary: DoubleArray) {
        var target = 0
        for (index in 1 until FFT_SIZE) {
            var bit = FFT_SIZE shr 1
            while (target and bit != 0) {
                target = target xor bit
                bit = bit shr 1
            }
            target = target xor bit
            if (index < target) {
                val realValue = real[index]
                real[index] = real[target]
                real[target] = realValue
                val imaginaryValue = imaginary[index]
                imaginary[index] = imaginary[target]
                imaginary[target] = imaginaryValue
            }
        }

        var length = 2
        while (length <= FFT_SIZE) {
            val angle = -2.0 * PI / length
            val phaseStepReal = cos(angle)
            val phaseStepImaginary = sin(angle)
            for (start in 0 until FFT_SIZE step length) {
                var phaseReal = 1.0
                var phaseImaginary = 0.0
                for (offset in 0 until length / 2) {
                    val even = start + offset
                    val odd = even + length / 2
                    val oddReal = real[odd] * phaseReal - imaginary[odd] * phaseImaginary
                    val oddImaginary = real[odd] * phaseImaginary + imaginary[odd] * phaseReal

                    real[odd] = real[even] - oddReal
                    imaginary[odd] = imaginary[even] - oddImaginary
                    real[even] += oddReal
                    imaginary[even] += oddImaginary

                    val nextPhaseReal = phaseReal * phaseStepReal - phaseImaginary * phaseStepImaginary
                    phaseImaginary = phaseReal * phaseStepImaginary + phaseImaginary * phaseStepReal
                    phaseReal = nextPhaseReal
                }
            }
            length = length shl 1
        }
    }

    companion object {
        const val BAND_COUNT = 48
        private const val FFT_SIZE = 2048
        private const val DEFAULT_SAMPLE_RATE = 44_100
        private const val MIN_FREQUENCY_HZ = 40.0
        private const val MAX_FREQUENCY_HZ = 16_000.0
        private val EMPTY_BANDS = List(BAND_COUNT) { 0f }
    }
}
