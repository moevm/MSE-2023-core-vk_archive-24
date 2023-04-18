package windowManager

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState

class AppWindowState(
    private val title: String,
    private val windowState: WindowState,
    private val applicationScope: ApplicationScope,
    private val closeWindow: () -> Unit,
    val openWindow: @Composable FrameWindowScope.(ComposeWindow) -> Unit,
) {
    @Composable
    fun run() {
        applicationScope.run {
            Window(
                title = title,
                state = windowState,
                onCloseRequest = closeWindow
            ) {
                openWindow(window)
            }
        }
    }
}