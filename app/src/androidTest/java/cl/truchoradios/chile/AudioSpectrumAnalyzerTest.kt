package cl.truchoradios.chile

import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.truchoradios.chile.player.AudioSpectrumAnalyzer
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.sin

@OptIn(markerClass = [UnstableApi::class])
@RunWith(AndroidJUnit4::class)
class AudioSpectrumAnalyzerTest {

    @Test
    fun pcmTone_producesPeakInExpectedFrequencyBands() {
        val analyzer = AudioSpectrumAnalyzer()
        analyzer.flush(SAMPLE_RATE, 1, C.ENCODING_PCM_16BIT)

        val pcm = ByteBuffer.allocateDirect(SAMPLE_COUNT * 2).order(ByteOrder.nativeOrder())
        repeat(SAMPLE_COUNT) { sample ->
            val value = (sin(2.0 * PI * TONE_HZ * sample / SAMPLE_RATE) * Short.MAX_VALUE * 0.8)
                .toInt()
                .toShort()
            pcm.putShort(value)
        }
        pcm.flip()
        analyzer.handleBuffer(pcm)

        val bands = analyzer.bands.value
        val peakBand = bands.indices.maxBy { bands[it] }
        assertTrue("expected visible spectrum energy", bands[peakBand] > 0.4f)
        assertTrue("1 kHz peak was in unexpected band $peakBand", peakBand in 23..29)
    }

    private companion object {
        const val SAMPLE_RATE = 48_000
        const val SAMPLE_COUNT = 2_048
        const val TONE_HZ = 1_000.0
    }
}
