package cl.truchoradios.chile

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import cl.truchoradios.chile.player.RadioPlayerManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TruchoRadiosApp : Application(), ImageLoaderFactory {

    @Inject
    lateinit var playerManager: RadioPlayerManager
    
    override fun onCreate() {
        super.onCreate()
        // Inicialización de Hilt ocurre automáticamente con @HiltAndroidApp
    }
    
    override fun onTerminate() {
        super.onTerminate()
        // Liberar recursos del reproductor cuando la aplicación se cierra
        playerManager.release()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .build()
    }
}
