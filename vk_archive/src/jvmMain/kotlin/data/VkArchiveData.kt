package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.mockObject.FakeDialog
import kotlinx.coroutines.*
import model.Dialog
import utils.HtmlParser
import utils.chooseDirection
import utils.getUsersNameIdList
import utils.goThroughDialogue
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
        updateProcessStatus: (Double) -> Unit,
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
                            updateProcessStatus((index + 1).toDouble() / listFiles.size * 100)
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
        updateProcessStatus: (Double) -> Unit,
        resetProcess: () -> Unit
    ): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            initProcess()
            if (isActive) {
                if (currentFolder.value != null) {
                    val file = File("${currentFolder.value?.path}/messages/$id")
                    var dialog: Dialog? = null
                    if (isActive) {
                        updateProcessStatus(1.0 * 100)
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

    //я все перенесу, я помню
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

    fun importPreparedDialogs() {
        fun loadPreparedDialogs(file: File): List<FakeDialog>? { // TODO Удалить заглушку
            return if (file.exists() || Math.random() >= 0.3) // TODO Убрать рандом
                getFakeDialogs().shuffled().dropLast((Math.random() * 5).toInt())
            else
                null
        }

        if (currentFolder.value != null) {
            preparedDialogs.clear()
            preparedDialogs.addAll(loadPreparedDialogs(File("${currentFolder.value!!.absolutePath}/parsed_messages"))?.map {
                Dialog(it.id, it.name)
            } ?: listOf())
        }
    }

    fun exportPreparedDialogs(): Boolean {
        fun savePreparedDialogs(preparedDialogs: List<Dialog>): Boolean { // TODO Удалить, когда будет парсер
            return Math.random() >= 0.5
        }

        if (preparedDialogs.isNotEmpty()) {
            return savePreparedDialogs(preparedDialogs)
        }
        return true
    }
}