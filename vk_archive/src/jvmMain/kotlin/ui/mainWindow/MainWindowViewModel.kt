package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.getFakeDialogs
import data.mockObject.FakeDialog
import kotlinx.coroutines.*
import utils.chooseDirection
import utils.getUsersNameIdList
import utils.goThroughDialogue
import java.io.File
import java.lang.Integer.min

class MainWindowViewModel {
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")
    private val currentFolder: MutableState<File?> = mutableStateOf(null)
    var currentDialogs = mutableStateListOf<String>()
    val preparedDialogs = mutableStateListOf<FakeDialog>()

    val isShowAboutAlertDialog = mutableStateOf(false)

    val isShowProcessAlertDialog = mutableStateOf(false)
    val processProgress = mutableStateOf(0.0)
    val processText = mutableStateOf("")
    var processJob: Job? = null

    var currentDialogId = mutableStateOf<String?>(null)

    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value = direction?.absolutePath ?: "Please, choose VK Archive folder"
        currentFolder.value = direction
    }

    fun prepareDialogsList(): List<String> {
        currentDialogs.clear()
        currentDialogs.addAll(
            getUsersNameIdList(currentFolder.value.toString())
                ?.map { it.name }
            ?: currentFolder.value?.listFiles()?.map { it.name } ?: emptyList())
        return currentDialogs
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
    
    fun parseAllDialogs() {
        processJob = CoroutineScope(Dispatchers.Default).launch {
            isShowProcessAlertDialog.value = true
            processProgress.value = 0.0
            processText.value = "Parsing dialogs..."
            val time = System.currentTimeMillis()
            while (true) { // Симуляция долгого процесса
                if ((System.currentTimeMillis() - time) / 100 > 100)
                    break
                if (!isActive)
                    break
                processProgress.value = min(((System.currentTimeMillis() - time) / 100).toInt(), 100).toDouble()
            }
            if (isActive) {
                processProgress.value = min(((System.currentTimeMillis() - time) / 100).toInt(), 100).toDouble()
                if (currentFolder.value != null) {
                    preparedDialogs.clear()
                    preparedDialogs.addAll(getFakeDialogs().shuffled())
                }
            }
            isShowProcessAlertDialog.value = false
        }
    }

    fun tryImportPreparedDialogs() {
        fun loadPreparedDialogs(file: File): List<FakeDialog>? { // TODO Удалить заглушку
            return if (file.exists() || Math.random() >= 0.3) // TODO Убрать рандом
                getFakeDialogs().shuffled().dropLast((Math.random() * 5).toInt())
            else
                null
        }

        if (currentFolder.value != null) {
            preparedDialogs.clear()
            preparedDialogs.addAll(
                loadPreparedDialogs(File("${currentFolder.value!!.absolutePath}/parsed_messages"))
                    ?: listOf()
            )
        }
    }

    fun exportPreparedDialogs(): Boolean {
        fun savePreparedDialogs(preparedDialogs: List<FakeDialog>): Boolean { // TODO Удалить, когда будет парсер
            return Math.random() >= 0.5
        }

        if (preparedDialogs.isNotEmpty()) {
            return savePreparedDialogs(preparedDialogs)
        }
        return true
    }
}
