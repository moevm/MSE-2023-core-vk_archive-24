package utils

import java.io.File
import java.io.OutputStream

interface AttachmentSaver {
    fun createOutputStream(dialogId: String, fileName: String): OutputStream?
}

class FileAttachmentSaver(
    private val currentFolder: String
) : AttachmentSaver {
    override fun createOutputStream(dialogId: String, fileName: String): OutputStream? {
        val destination =
            File("${currentFolder}/parsed_attachments/${dialogId}/images").apply {
                if (!exists() && !mkdirs())
                    throw IllegalStateException("Failed to create directory: $this")
            }
        val file = File("$destination/$fileName")
        if (file.exists()) {
            println("File $file already exists")
            return null
        }

        return file.outputStream()
    }
}
