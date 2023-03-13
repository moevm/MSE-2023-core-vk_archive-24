package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.application
import ui.mainWindow.MainWindow
import windowManager.WindowsManager

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainWindow()
    }
}

fun main() = application {
    WindowsManager.setupApplicationScope(this)
    WindowsManager.prepareMainWindow()
}
