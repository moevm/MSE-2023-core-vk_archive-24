package ui.mainWindow

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.UsersNameId
import data.VkArchiveData
import kotlinx.coroutines.Job
import model.Dialog

class MainWindowViewModel {
    val vkArchiveData = VkArchiveData()

    val isShowAboutAlertDialog = mutableStateOf(false)

    val isShowProcessAlertDialog = mutableStateOf(false)
    val processProgress = mutableStateOf(0.0)
    val processText = mutableStateOf("")
    var processJob: Job? = null

    var currentDialogId = mutableStateOf<String?>(null)

    var filteredDialogs = mutableStateListOf<UsersNameId>()
    var nameFilterForDialogs = ""
        set(value) {
            field = value
            filteredDialogs.clear()
            val newList = vkArchiveData.getFilteredDialogs { value in it.name }
            filteredDialogs.addAll(newList)
        }

    var filteredPreparedDialogs = mutableStateListOf<Dialog>()
    var nameFilterForPreparedDialogs = ""
        set(value) {
            field = value
            filteredPreparedDialogs.clear()
            val newList = vkArchiveData.getFilteredPreparedDialogs { value in it.name }
            filteredPreparedDialogs.addAll(newList)
        }

    fun updateVkArchiveFolder() {
        vkArchiveData.chooseFolder()
        vkArchiveData.prepareDialogsList()
        nameFilterForDialogs = ""
    }

    fun showAboutAlertDialog() {
        isShowAboutAlertDialog.value = true
    }

    fun hideAboutAlertDialog() {
        isShowAboutAlertDialog.value = false
    }

    private fun showProcessAlertDialog() {
        isShowProcessAlertDialog.value = true
    }

    fun hideProcessAlertDialog() {
        isShowProcessAlertDialog.value = false
    }

    fun parseAllDialogs() {
        processJob = vkArchiveData.parseAllDialogs(
            initProcess = {
                showProcessAlertDialog()
                processProgress.value = 0.0
                processText.value = "Parsing dialogs..."
            },
            updateProcessStatus = { process -> processProgress.value = process },
            resetProcess = {
                hideProcessAlertDialog()
                nameFilterForPreparedDialogs = ""
            }
        )
    }

    fun parseDialog(id: String) {
        processJob = vkArchiveData.parseDialog(
            id,
            initProcess = {
                showProcessAlertDialog()
                processProgress.value = 0.0
                processText.value = "Parsing dialog..."
            },
            updateProcessStatus = { process -> processProgress.value = process },
            resetProcess = {
                hideProcessAlertDialog()
                nameFilterForPreparedDialogs = ""
            }
        )
    }

    fun importPreparedDialogs() {
        processJob = vkArchiveData.importPreparedDialogs(
            initProcess = {
                showProcessAlertDialog()
                processProgress.value = 0.0
                processText.value = "Import dialogs..."
            },
            updateProcessStatus = { process -> processProgress.value = process },
            resetProcess = {
                hideProcessAlertDialog()
                nameFilterForPreparedDialogs = ""
            }
        )
    }

    fun exportPreparedDialogs() {
        processJob = vkArchiveData.exportPreparedDialogs(
            initProcess = {
                showProcessAlertDialog()
                processProgress.value = 0.0
                processText.value = "Export dialogs..."
            },
            updateProcessStatus = { process -> processProgress.value = process },
            resetProcess = { hideProcessAlertDialog() }
        )
    }
}
