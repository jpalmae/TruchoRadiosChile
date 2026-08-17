package cl.truchoradios.chile

import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.truchoradios.chile.player.PlaybackState
import cl.truchoradios.chile.player.shouldKeepCurrentRadio
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerLifecycleTest {

    @Test
    fun sameActiveRadio_doesNotReloadStream() {
        assertTrue(
            shouldKeepCurrentRadio(
                requestedRadioId = "radio-1",
                currentRadioId = "radio-1",
                currentMediaId = "radio-1",
                playbackState = PlaybackState.PLAYING,
            )
        )
        assertTrue(
            shouldKeepCurrentRadio(
                requestedRadioId = "radio-1",
                currentRadioId = null,
                currentMediaId = "radio-1",
                playbackState = PlaybackState.BUFFERING,
            )
        )
        assertFalse(
            shouldKeepCurrentRadio(
                requestedRadioId = "radio-2",
                currentRadioId = "radio-1",
                currentMediaId = "radio-1",
                playbackState = PlaybackState.PLAYING,
            )
        )
        assertFalse(
            shouldKeepCurrentRadio(
                requestedRadioId = "radio-1",
                currentRadioId = "radio-1",
                currentMediaId = "radio-1",
                playbackState = PlaybackState.ERROR,
            )
        )
    }
}
