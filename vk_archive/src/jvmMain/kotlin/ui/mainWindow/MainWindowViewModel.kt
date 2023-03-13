package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.getFakeDialogs
import data.mockObject.FakeDialog
import utils.chooseDirection
import utils.findFolder
import java.io.File
import java.nio.charset.Charset

class MainWindowViewModel {
    val currentDirectory = mutableStateOf("Please, choose VK Archive folder")
    val currentFolder: MutableState<File?> = mutableStateOf(null)
    val isShowAboutAlertDialog = mutableStateOf(false)
    val preparedDialogs = mutableStateListOf<FakeDialog>()

    fun chooseFolder() {
        val direction = chooseDirection()
        currentDirectory.value = direction?.absolutePath ?: "Please, choose VK Archive folder"
        currentFolder.value = direction

        //тест функции поиска имени (потом удалить)
        println(getFriendUserName("-15365973"))
    }

    fun showAboutAlertDialog() {
        isShowAboutAlertDialog.value = true
    }

    fun hideAboutAlertDialog() {
        isShowAboutAlertDialog.value = false
    }

    fun loadAllPreparedDialogs() {
        preparedDialogs.clear()
        preparedDialogs.addAll(getFakeDialogs().shuffled())
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
