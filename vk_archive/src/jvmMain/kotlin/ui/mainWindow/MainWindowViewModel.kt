package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import utils.chooseDirection
import utils.findFolder
import utils.sortFilesByNum
import java.io.File
import java.nio.charset.Charset

class MainWindowViewModel {
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")
    val currentFolder: MutableState<File?> = mutableStateOf(null)
    val isShowAboutAlertDialog = mutableStateOf(false)

    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value = direction?.absolutePath ?: "Please, choose VK Archive folder"
        currentFolder.value = direction

        println(goThroughMessages()) //тест для папки Messages в архиве


        //тест функции поиска имени (потом удалить)
        println(getFriendUserName("-15365973"))

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

    // поиск имени по id (потом вынести из viewModel)
    fun getFriendUserName(id: String): String? {
        val startDir = File(currentDirectory.value).toString()
        val searchDirs = listOf("$startDir/messages",startDir)
        for (dir in searchDirs) {
            println("Search in $dir")
            val folder = findFolder(File(dir), id)
            val htmlFile =
                folder?.listFiles()?.get(0)?.readText(charset = Charset.forName("windows-1251")) ?: "error"
            val regex = Regex("""<div class="ui_crumb"\s>\s*([^<]+)\s*</div>""")
            val matchResult = regex.find(htmlFile)
            if (matchResult != null){
                return matchResult.groupValues[1]
            }
        }
        return null
    }
}
