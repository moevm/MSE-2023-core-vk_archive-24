package utils

import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
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

//загрузка и сохранение изображения по ссылке
fun downloadImage(imageUrl: String, filePath: String) {
    val url = URL(imageUrl)
    val connection = url.openConnection()
    connection.connect()
    val inputStream = connection.getInputStream()
    val outputStream = File(filePath).outputStream()
    inputStream.copyTo(outputStream)
    outputStream.close()
    inputStream.close()
}

//Создание копии изображения в меньшем разрешении
fun reduceImageResolution(filePath: String, outputPath: String) {
    val inputImage = ImageIO.read(File(filePath))
    val newWidth = inputImage.getWidth(null)/4
    val newHeight = inputImage.getHeight(null)/4
    val outputImage = BufferedImage(newWidth, newHeight, inputImage.type)
    val graphics = outputImage.createGraphics()
    graphics.drawImage(inputImage, 0, 0, newWidth, newHeight, null)
    graphics.dispose()
    ImageIO.write(outputImage, "jpg", File(outputPath))
}
