package data

import processing.DialogsProcessing
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import model.AttachmentType
import model.Dialog
import model.UsersNameId
import utils.*
import java.io.File

class VkArchiveData {
    val currentDirectory = mutableStateOf("")

    private val currentFolder: MutableState<File?> = mutableStateOf(null)
    var dialogsData = mutableStateListOf<UsersNameId>()
    val preparedDialogsData = mutableStateListOf<Dialog>()
    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value = direction?.absolutePath ?: ""
        currentFolder.value = direction
    }

    fun prepareDialogsList() {
        dialogsData.clear()
        val listNameId = getUsersNameIdList(currentFolder.value.toString())
        dialogsData.addAll(listNameId?: emptyList())
    }

    fun getFilteredDialogs(filter: (UsersNameId) -> Boolean): List<UsersNameId> =
        dialogsData.filter(filter)

    fun getFilteredPreparedDialogs(filter: (Dialog) -> Boolean): List<Dialog> =
        preparedDialogsData.filter(filter)

    fun parseAllDialogs(uiUpdater: UIProcessUpdater? = null): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            uiUpdater?.initProcess()
            if (isActive) {
                if (currentFolder.value != null) {
                    val listFiles =
                        File("${currentFolder.value?.path}/messages/").listFiles()
                            ?: arrayOf()
                    val tempList = arrayListOf<Dialog>()

                    for ((index, file) in listFiles.withIndex()) {
                        if (isActive) {
                            uiUpdater?.updateProcessStatus("${(index + 1)}/${listFiles.size}")
                            if (file.path.last().isDigit()) tempList.add(
                                HtmlParser.parseDialogFolder(File(file.path))
                            )
                        } else break
                    }
                    if (isActive) {
                        preparedDialogsData.clear()
                        preparedDialogsData.addAll(tempList)
                    }
                }
            }
            uiUpdater?.finishProcess()
        }
    }

    fun parseDialog(id: String, uiUpdater: UIProcessUpdater? = null): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            uiUpdater?.initProcess()
            if (isActive) {
                if (currentFolder.value != null) {
                    val file = File("${currentFolder.value?.path}/messages/$id")
                    var dialog: Dialog? = null
                    if (isActive) {
                        uiUpdater?.updateProcessStatus("1/1")
                        if (file.path.last().isDigit())
                            dialog = HtmlParser.parseDialogFolder(File(file.path))
                    }
                    if (isActive && dialog != null && !preparedDialogsData.contains(dialog)) {
                        preparedDialogsData.add(dialog)
                    }
                }
            }
            uiUpdater?.finishProcess()
        }
    }

    fun goThroughMessages(): MutableList<String?>{
        //возвращаем список обработанных диалогов
        val fileNames = mutableListOf<String?>()
        var counter = 0
        val archiveFolder = File(currentDirectory.value).toString()
        val messagesFolder = File("$archiveFolder/messages").listFiles()
        if (messagesFolder != null){
            for (dialogue in messagesFolder) {
                if(dialogue.isDirectory){
                    fileNames.add(dialogue.name)
                    counter += goThroughDialogue(dialogue)
                }
            }
            println(counter)
        }
        return fileNames
    }

    fun importPreparedDialogs(uiUpdater: UIProcessUpdater? = null): Job? {
        if (currentFolder.value != null) {
            return CoroutineScope(Dispatchers.IO).launch {
                uiUpdater?.initProcess()
                val updateProcessStatus =
                    if (uiUpdater != null) uiUpdater::updateProcessStatus
                    else null
                preparedDialogsData.clear()
                preparedDialogsData.addAll(
                    DialogJsonHelper.importAll(
                        File("${currentFolder.value!!.absolutePath}/parsed_messages"),
                        updateProcessStatus,
                        checkActiveState = { isActive }
                    )
                )
                uiUpdater?.finishProcess()
            }
        }
        return null
    }

    fun exportPreparedDialogs(uiUpdater: UIProcessUpdater? = null): Job? {
        if (preparedDialogsData.isNotEmpty() && currentFolder.value != null) {
            return CoroutineScope(Dispatchers.IO).launch {
                uiUpdater?.initProcess()
                val path = "${currentFolder.value!!.absolutePath}/parsed_messages"
                for ((i, dialog) in preparedDialogsData.withIndex()) {
                    uiUpdater?.updateProcessStatus("$i/${preparedDialogsData.size}")
                    DialogJsonHelper.export(File(path), dialog)
                }
                uiUpdater?.finishProcess()
            }
        }
        return null
    }

    /**
     * пример использования: downloadAttachments(dialog, listOf(AttachmentType.PHOTO, AttachmentType.VIDEO))
     */
    fun downloadAttachments(
        dialogs: List<Dialog>,
        fileTypesToDownload: List<AttachmentType>,
        uiUpdater: UIProcessUpdater? = null,
        amountMessages: Int? = null,
    ) : Job {
        return CoroutineScope(Dispatchers.IO).launch {
            currentFolder.value?.let { currentFolder ->
                DialogsProcessing.downloadAttachments(
                    dialogs,
                    fileTypesToDownload,
                    FileAttachmentSaver(currentFolder),
                    amountMessages,
                    uiUpdater,
                    isActive = { isActive }
                )
            }
        }
    }
}