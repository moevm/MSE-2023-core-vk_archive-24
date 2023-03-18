package utils

import java.io.File
import javax.swing.JFileChooser

// TODO: Удалить временное решение после добавления готового

fun chooseDirection(): File? {
    val fileChooser = JFileChooser("/").apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        dialogTitle = "Select VK Archive Folder"
        approveButtonText = "Select"
    }
    fileChooser.showOpenDialog(null)
    val result = fileChooser.selectedFile
    println(result?.absolutePath ?: "")
    return result
}

fun findFolder(startDir: File, folderName: String): File? {
    val fileTree = startDir.walk()
    for (file in fileTree) {
        if (file.isDirectory && file.name == folderName) {
            return file
        }
    }
    return null
}

fun sortFilesByNum(currentFolder: File): MutableList<File> {
    val filesList = mutableListOf<File>()
    val filesNames = mutableListOf<String>()
    currentFolder.list().forEach { filesNames.add(it) }
    filesNames.sortByDescending { s -> s.filter { it.isDigit() }.toInt() }
    for (name in filesNames) {
        filesList.add(File("$currentFolder/$name"))
    }
    return filesList
}