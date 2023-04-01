package utils

import model.UsersNameId
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import java.nio.charset.Charset
import javax.imageio.ImageIO
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

//загрузка и сохранение файла по ссылке
fun downloadAttachment(imageUrl: String, filePath: File) {
    val url = URL(imageUrl)
    val connection = url.openConnection()
    connection.connect()
    val inputStream = connection.getInputStream()
    val outputStream = filePath.outputStream()
    inputStream.copyTo(outputStream)
    outputStream.close()
    inputStream.close()
}

//Создание копии изображения в меньшем разрешении
fun reduceImageResolution(filePath: String, outputPath: String, newWidth: Int, newHeight: Int) {
    val inputImage = ImageIO.read(File(filePath))
    val outputImage = BufferedImage(newWidth, newHeight, inputImage.type)
    val graphics = outputImage.createGraphics()
    graphics.drawImage(inputImage, 0, 0, newWidth, newHeight, null)
    graphics.dispose()
    ImageIO.write(outputImage, "jpg", File(outputPath))
}

//сортировка файлов по убыванию числа в названии
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

//обработка файлов в диалоге
fun goThroughDialogue(dialogueFolder: File): Int{
    var counter = 0
    val fileList = sortFilesByNum(dialogueFolder)
    for (item in fileList) {
        // TODO() парсер на файл
        counter ++
    }
    return counter
}

// список id и имен
fun getUsersNameIdList (directoryPath: String): List<UsersNameId>?{
    val messagesDirectory = File(directoryPath, "messages")
    val indexFile = File(messagesDirectory, "index-messages.html")
    val usersNameIdList = mutableListOf<UsersNameId>()

    if (indexFile.exists()) {
        val htmlText = indexFile.readText(charset = Charset.forName("windows-1251"))
        val regex = """<div class="message-peer--id">\s+<a href="(-?\d+)/messages0.html">([^<]+)</a>""".toRegex()
        val matches = regex.findAll(htmlText)

        for (match in matches) {
            val (id, name) = match.destructured
            usersNameIdList.add(UsersNameId(id, name))
        }
    }
    else return null
    return usersNameIdList
}
