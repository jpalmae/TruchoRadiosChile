package cl.truchoradios.chile.presentation.components

import android.view.ContextThemeWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import cl.truchoradios.chile.R
import com.google.android.gms.cast.framework.CastButtonFactory

@Composable
fun CastButton(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val themedContext = ContextThemeWrapper(context, R.style.TruchoMediaRouter)
            MediaRouteButton(themedContext).apply {
                CastButtonFactory.setUpMediaRouteButton(context, this)
            }
        }
    )
}
