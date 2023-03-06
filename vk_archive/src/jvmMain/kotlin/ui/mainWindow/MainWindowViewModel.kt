package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import utils.chooseDirection
import java.io.File

class MainWindowViewModel {
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")
    val currentFolder: MutableState<File?> = mutableStateOf(null)
    val isShowAboutAlertDialog = mutableStateOf(false)

    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value = direction?.absolutePath ?: "Please, choose VK Archive folder"
        currentFolder.value = direction
    }

    fun showAboutAlertDialog() {
        isShowAboutAlertDialog.value = true
    }

    fun hideAboutAlertDialog() {
        isShowAboutAlertDialog.value = false
    }
}
