package data

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
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")

    private val currentFolder: MutableState<File?> = mutableStateOf(null)
    var dialogsData = mutableStateListOf<UsersNameId>()
    val preparedDialogs = mutableStateListOf<Dialog>()
    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value =
            direction?.absolutePath ?: "Please, choose VK Archive folder"
        currentFolder.value = direction
    }

    fun prepareDialogsList() {
        dialogsData.clear()
        val listNameId = getUsersNameIdList(currentFolder.value.toString())
        dialogsData.addAll(listNameId?: emptyList())
    }

    fun parseAllDialogs(
        initProcess: () -> Unit,
        updateProcessStatus: (String) -> Unit,
        resetProcess: () -> Unit
    ): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            initProcess()
            if (isActive) {
                if (currentFolder.value != null) {
                    val listFiles =
                        File("${currentFolder.value?.path}/messages/").listFiles()
                            ?: arrayOf()
                    val tempList = arrayListOf<Dialog>()

                    for ((index, file) in listFiles.withIndex()) {
                        if (isActive) {
                            updateProcessStatus("${(index + 1)}/${listFiles.size}")
                            if (file.path.last().isDigit()) tempList.add(
                                HtmlParser.parseDialogFolder(File(file.path))
                            )
                        } else break
                    }
                    if (isActive) {
                        preparedDialogs.clear()
                        preparedDialogs.addAll(tempList)
                    }
                }
            }
            resetProcess()
        }
    }

    fun parseDialog(
        id: String,
        initProcess: () -> Unit,
        updateProcessStatus: (String) -> Unit,
        resetProcess: () -> Unit
    ): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            initProcess()
            if (isActive) {
                if (currentFolder.value != null) {
                    val file = File("${currentFolder.value?.path}/messages/$id")
                    var dialog: Dialog? = null
                    if (isActive) {
                        updateProcessStatus("1/1")
                        if (file.path.last().isDigit())
                            dialog = HtmlParser.parseDialogFolder(File(file.path))
                    }
                    if (isActive && dialog != null && !preparedDialogs.contains(dialog)) {
                        preparedDialogs.add(dialog)
                    }
                }
            }
            resetProcess()
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

    fun importPreparedDialogs(
        initProcess: () -> Unit,
        updateProcessStatus: (String) -> Unit,
        resetProcess: () -> Unit
    ): Job? {
        if (currentFolder.value != null) {
            return CoroutineScope(Dispatchers.IO).launch {
                initProcess()
                preparedDialogs.clear()
                preparedDialogs.addAll(
                    DialogJsonHelper.importAll(
                        File("${currentFolder.value!!.absolutePath}/parsed_messages"),
                        updateProcessStatus,
                        checkActiveState = { isActive }
                    )
                )
                resetProcess()
            }
        }
        return null
    }

    fun exportPreparedDialogs(
        initProcess: () -> Unit,
        updateProcessStatus: (String) -> Unit,
        resetProcess: () -> Unit
    ): Job? {
        if (preparedDialogs.isNotEmpty() && currentFolder.value != null) {
            return CoroutineScope(Dispatchers.IO).launch {
                initProcess()
                val path = "${currentFolder.value!!.absolutePath}/parsed_messages"
                for ((i, dialog) in preparedDialogs.withIndex()) {
                    updateProcessStatus("$i/${preparedDialogs.size}")
                    DialogJsonHelper.export(File(path), dialog)
                }
                resetProcess()
            }
        }
        return null
    }

    /**
     * пример использования: downloadAttachments(dialog, listOf(AttachmentType.PHOTO, AttachmentType.VIDEO))
     */
    fun downloadAttachments(
        initProcess: () -> Unit,
        updateProcessStatus: (String) -> Unit,
        resetProcess: () -> Unit,
        dialogs: List<Dialog>,
        fileTypesToDownload: List<AttachmentType>,
        amountMessages: Int? = null,
    ) : Job {
        return CoroutineScope(Dispatchers.IO).launch {
            initProcess()
            updateProcessStatus("0/${dialogs.size}")
            for((index, dialog) in dialogs.withIndex()) {
                if (isActive) {
                    val messagesToProcess = dialog.messages.take(amountMessages ?: dialog.messages.size)
                    for (message in messagesToProcess) {
                        if (isActive) {
                            for (attachment in message.attachments) {
                                if (isActive) {
                                    if (attachment.url == null) continue
                                    var destination =
                                        File(currentFolder.value!!.absolutePath + "/parsed_attachments/${dialog.id}")

                                    when (attachment.attachmentType) {
                                        in AttachmentType.PHOTO.translates -> {
                                            if (AttachmentType.PHOTO !in fileTypesToDownload) continue
                                            destination = File("${destination}/images").apply {
                                                if (!exists() && !mkdirs()) throw IllegalStateException("Failed to create directory: $this")
                                            }
                                            val regexImage = Regex("""/([\w-]+\.(?:jpg|png|jpeg|gif))""")
                                            downloadAttachment(
                                                attachment.url,
                                                File("$destination/${regexImage.find(attachment.url)?.value ?: continue}")
                                            )
                                        }

                                        in AttachmentType.VIDEO.translates,
                                        in AttachmentType.GIFT.translates,
                                        in AttachmentType.FILE.translates,
                                        in AttachmentType.STICKER.translates,
                                        in AttachmentType.URL.translates,
                                        in AttachmentType.AUDIO.translates,
                                        in AttachmentType.CALL.translates,
                                        in AttachmentType.POST.translates -> { continue }
                                        else -> { continue }
                                    }
                                } else break
                            }
                        } else break
                    }
                }
                updateProcessStatus("${index + 1}/${dialogs.size}")
            }
            resetProcess()
        }
    }
}