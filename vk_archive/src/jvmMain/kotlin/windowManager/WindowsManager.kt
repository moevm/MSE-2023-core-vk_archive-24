package windowManager

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowState
import app.App
import ui.dialogWindow.DialogWindow
import java.awt.Dimension

object WindowsManager {
    private lateinit var applicationScope: ApplicationScope
    fun setupApplicationScope(applicationScope: ApplicationScope) {
        this.applicationScope = applicationScope
    }

    @Composable
    fun prepareMainWindow() {
        AppWindowState(
            title = "Vk Archive",
            windowState = WindowState(size = DpSize(1200.dp, 800.dp)),
            applicationScope = applicationScope,
            applicationScope::exitApplication
        ) {
            it.minimumSize = Dimension(1200, 800)
            App()
        }.run()
    }

    @Composable
    fun prepareDialogWindow(id: String, onExitClick: () -> Unit) {
        AppWindowState(
            title = "Dialog",
            windowState = WindowState(size = DpSize(1200.dp, 800.dp)),
            applicationScope = applicationScope,
            onExitClick
        ) {
            it.minimumSize = Dimension(1200, 800)
            DialogWindow(id)
        }.run()
    }
}
