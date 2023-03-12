package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ui.mainWindow.MainWindow
import java.awt.Dimension

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainWindow()
    }
}

fun main() = application {
    Window(
        title = "Vk Archive",
        state = WindowState(size = DpSize(1200.dp, 800.dp)),
        onCloseRequest = ::exitApplication
    ) {
        window.minimumSize = Dimension(1200, 800)
        App()
    }
}
