package ui.mainWindow

import androidx.compose.runtime.mutableStateOf
import data.VkArchiveData
import kotlinx.coroutines.Job

class MainWindowViewModel {
    val vkArchiveData = VkArchiveData()

    val isShowAboutAlertDialog = mutableStateOf(false)

    val isShowProcessAlertDialog = mutableStateOf(false)
    val processProgress = mutableStateOf(0.0)
    val processText = mutableStateOf("")
    var processJob: Job? = null

    var currentDialogId = mutableStateOf<String?>(null)

    fun chooseFolder() {
        vkArchiveData.chooseFolder()
    }

    fun prepareDialogsList(): List<String> {
        vkArchiveData.prepareDialogsList()
        return vkArchiveData.dialogsData.map { it.name }
    }

    fun showAboutAlertDialog() {
        isShowAboutAlertDialog.value = true
    }

    fun hideAboutAlertDialog() {
        isShowAboutAlertDialog.value = false
    }

    fun showProcessAlertDialog() {
        isShowProcessAlertDialog.value = true
    }

    fun hideProcessAlertDialog() {
        isShowProcessAlertDialog.value = false
    }

    fun parseAllDialogs() {
        processJob = vkArchiveData.parseAllDialogs(
            initProcess = {
                isShowProcessAlertDialog.value = true
                processProgress.value = 0.0
                processText.value = "Parsing dialogs..."
            },
            updateProcessStatus = { process -> processProgress.value = process },
            resetProcess = { isShowProcessAlertDialog.value = false }
        )
    }

    fun parseDialog(id: String) {
        processJob = vkArchiveData.parseDialog(
            id,
            initProcess = {
                isShowProcessAlertDialog.value = true
                processProgress.value = 0.0
                processText.value = "Parsing dialog..."
            },
            updateProcessStatus = { process -> processProgress.value = process },
            resetProcess = { isShowProcessAlertDialog.value = false }
        )
    }

    fun tryImportPreparedDialogs() {
        vkArchiveData.importPreparedDialogs()
    }

    fun exportPreparedDialogs(): Boolean {
        return vkArchiveData.exportPreparedDialogs()
    }
}
