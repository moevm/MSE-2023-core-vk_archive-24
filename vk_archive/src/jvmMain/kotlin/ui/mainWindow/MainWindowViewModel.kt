package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.VkArchiveData
import kotlinx.coroutines.Job
import model.AttachmentType
import model.Dialog
import model.UsersNameId
import ui.alertDialog.DialogWithContentState
import utils.languages.StringResources

class MainWindowViewModel {
    val vkArchiveData = VkArchiveData()

    val isShowAboutDialog = mutableStateOf(false)

    val isShowProcessAlertDialog = mutableStateOf(false)
    val status = mutableStateOf("")
    val processText = mutableStateOf("")
    var processJob: Job? = null

    val dialogWithContentState: MutableState<DialogWithContentState?> =
        mutableStateOf(null)

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
        isShowAboutDialog.value = true
    }

    fun hideAboutAlertDialog() {
        isShowAboutDialog.value = false
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
                processText.value = "${StringResources.parsingDialogs.updatableString()}..."
            },
            updateProcessStatus = { process -> status.value = process },
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
                status.value = ""
                processText.value = "${StringResources.parsingDialog.updatableString()}..."
            },
            updateProcessStatus = { process -> status.value = process },
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
                status.value = ""
                processText.value = "${StringResources.importDialogs.updatableString()}..."
            },
            updateProcessStatus = { process -> status.value = process },
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
                status.value = ""
                processText.value = "${StringResources.exportDialogs.updatableString()}..."
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
                            processText.value = "${StringResources.downloadAttachments.updatableString()}..."
                        },
                        updateProcessStatus = { process ->
                            status.value = process
                        },
                        resetProcess = { hideProcessAlertDialog() },
                        dialogs = vkArchiveData.preparedDialogsData.filter {
                            selectedIds.contains(
                                it.id
                            )
                        },
                        fileTypesToDownload = AttachmentType.values().filter { types ->
                            types.translates.values.find { type ->
                                selectedAttachments.contains(type)
                            } != null
                        }
                    )
                }
            )
    }
}
