package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.getFakeDialogs
import data.mockObject.FakeDialog
import utils.chooseDirection
import utils.sortFilesByNum
import utils.getUsersNameIdList
import java.io.File

class MainWindowViewModel {
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")
    private val currentFolder: MutableState<File?> = mutableStateOf(null)
    var currentDialogs = mutableStateListOf<String>()
    val preparedDialogs = mutableStateListOf<FakeDialog>()

    val isShowAboutAlertDialog = mutableStateOf(false)

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

    //я все перенесу, я помню
    fun goThroughDialogue(dialogueFolder: File): Int{
        //пока тут просто счетчик для проверки прохода по всем файлам
        var counter = 0
        val fileList = sortFilesByNum(dialogueFolder)
        for (item in fileList) {
            if(item == dialogueFolder) continue
            // TODO() парсер на файл
            counter ++
        }
        return counter
    }

    fun goThroughMessages(): MutableList<String?>{
        //возввращаем список обработанных диалогов
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
    
    fun tryLoadAllPreparedDialogs() {
        if (currentFolder.value != null) {
            loadAllPreparedDialogs()
        }
    }

    fun loadAllPreparedDialogs() {
        preparedDialogs.clear()
        preparedDialogs.addAll(getFakeDialogs().shuffled())
    }
}
