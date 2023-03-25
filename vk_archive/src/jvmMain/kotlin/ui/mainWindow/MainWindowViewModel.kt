package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import utils.*
import data.getFakeDialogs
import data.mockObject.FakeDialog
import utils.chooseDirection
import utils.getUsersNameIdList
import utils.goThroughDialogue
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
