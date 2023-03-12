package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import utils.chooseDirection
import java.io.File

class MainWindowViewModel {
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")
    val currentFolder: MutableState<File?> = mutableStateOf(null)
    val isShowAboutAlertDialog = mutableStateOf(false)

    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value = direction?.absolutePath ?: "Please, choose VK Archive folder"
        currentFolder.value = direction
        println(goThroughMessages()) //тест для папки Messages в архиве
    }

    fun showAboutAlertDialog() {
        isShowAboutAlertDialog.value = true
    }

    fun hideAboutAlertDialog() {
        isShowAboutAlertDialog.value = false
    }

    fun goThroughDialogue(dialogueFolder: File): Int{
        //пока тут просто счетчик для проверки прохода по всем файлам
        var counter = 0
        val fileTree = dialogueFolder.walk()
        for (file in fileTree) {
            if(file == dialogueFolder) continue
            // TODO() парсер на файл
            counter += 1
        }
        return counter
    }

    fun goThroughMessages(): MutableList<String?>{
        //вовзвращаем список обработанных диалогов
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
}
