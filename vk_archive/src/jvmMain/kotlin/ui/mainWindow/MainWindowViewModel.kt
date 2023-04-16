package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.VkArchiveData
import kotlinx.coroutines.Job
import model.AttachmentType
import ui.alertDialog.DialogWithContentState

class MainWindowViewModel {
    val vkArchiveData = VkArchiveData()

    val isShowAboutAlertDialog = mutableStateOf(false)

    val isShowProcessAlertDialog = mutableStateOf(false)
    val status = mutableStateOf("")
    val processText = mutableStateOf("")
    var processJob: Job? = null

    val dialogWithContentState: MutableState<DialogWithContentState?> =
        mutableStateOf(null)

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
                status.value = ""
                processText.value = "Parsing dialogs..."
            },
            updateProcessStatus = { process -> status.value = process },
            resetProcess = { hideProcessAlertDialog() }
        )
    }

    fun parseDialog(id: String) {
        processJob = vkArchiveData.parseDialog(
            id,
            initProcess = {
                showProcessAlertDialog()
                status.value = ""
                processText.value = "Parsing dialog..."
            },
            updateProcessStatus = { process -> status.value = process },
            resetProcess = { hideProcessAlertDialog() }
        )
    }

    fun importPreparedDialogs() {
        processJob = vkArchiveData.importPreparedDialogs(
            initProcess = {
                showProcessAlertDialog()
                status.value = ""
                processText.value = "Import dialogs..."
            },
            updateProcessStatus = { process -> status.value = process },
            resetProcess = { hideProcessAlertDialog() }
        )
    }

    fun exportPreparedDialogs() {
        processJob = vkArchiveData.exportPreparedDialogs(
            initProcess = {
                showProcessAlertDialog()
                status.value = ""
                processText.value = "Export dialogs..."
            },
            updateProcessStatus = { process -> status.value = process },
            resetProcess = { hideProcessAlertDialog() }
        )
    }

    fun downloadAttachments(selectedIds: Set<String>) {
        this.dialogWithContentState.value =
            createDownloadDialog(
                hideDownloadDialog = { this.dialogWithContentState.value = null },
                downloadAttachments = { selectedAttachments: Set<String> ->
                    processJob = vkArchiveData.downloadAttachments(
                        initProcess = {
                            showProcessAlertDialog()
                            status.value = ""
                            processText.value = "Download attachments..."
                        },
                        updateProcessStatus = { process ->
                            status.value = process
                        },
                        resetProcess = { hideProcessAlertDialog() },
                        dialogs = vkArchiveData.preparedDialogs.filter {
                            selectedIds.contains(
                                it.id
                            )
                        },
                        fileTypesToDownload = AttachmentType.values().filter { types ->
                            types.translates.find { type ->
                                selectedAttachments.contains(type)
                            } != null
                        }
                    )
                }
            )
    }

}
