package utils

import kotlinx.serialization.json.Json
import model.Dialog
import model.Message
import model.UsersNameId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

object VkDialogParserFileProxy {
    /**
     * file - директория с файлами сообщений
     */
    fun parseDialogFromFolder(file: File): Dialog {
        val dialog = Dialog()
        dialog.id = file.name

        val filesInFolder = file.listFiles()
        filesInFolder?.sortWith { o1, o2 ->
            val value1 = o1!!.name.filter { it.isDigit() }.toInt()
            val value2 = o2!!.name.filter { it.isDigit() }.toInt()
            value1 - value2
        }
        filesInFolder?.forEach {
            val tmpMessages = parseMessagesFromFile(dialog, it)
            VkDialogParser.addMessagesToDialog(dialog, tmpMessages)
        }
        return dialog
    }

    private fun parseMessagesFromFile(dialog:Dialog,file:File): List<Message>{
        if (!file.name.endsWith(".html")) throw RuntimeException("Not html file")
        val document: Document = try {
            Jsoup.parse(file, "Windows-1251")
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (dialog.name == ""){
            VkDialogParser.setName(dialog, document)
        }
        return VkDialogParser.pushMessagesFromDOM(document);
    }

    /**
     * Во всех методах dir - папка ..\Archive\parsed_messages
     */

    fun export(dir: File, dialog: Dialog) {
        if (!dir.exists()) {
            Files.createDirectory(Paths.get(dir.absolutePath))
        }
        val file = File("${dir.absolutePath}/${dialog.id}.json")

        val dialogString = Json.encodeToString(Dialog.serializer(), dialog)
        file.writeText(dialogString, Charsets.UTF_8)
    }

    fun importAll(
        dir: File,
        updateProcessStatus: (String) -> Unit,
        checkActiveState: () -> Boolean
    ): List<Dialog> {
        val dialogs = dir.listFiles() ?: return emptyList()
        val result = ArrayList<Dialog>(dialogs.size)
        for ((index, file) in dialogs.withIndex()) {
            if (checkActiveState()) {
                updateProcessStatus("${index + 1}/${dialogs.size}")
                val dialogString = file.readText(Charsets.UTF_8)
                result.add(Json.decodeFromString(Dialog.serializer(), dialogString))
            } else {
                return emptyList()
            }
        }
        return result
    }

    /**
     * ids - список с id диалогов
     */
    fun importIds(dir:File,ids:List<String>):List<Dialog?>{
        return ids.map {
            val file = File("${dir.absolutePath}/${it}.json")
            if (!file.exists()){
                return@map null
            }
            val dialogString = file.readText(Charsets.UTF_8);
            Json.decodeFromString(Dialog.serializer(),dialogString);
        }
    }

    // список id и имен
    fun getUsersNameIdList(directoryPath: String): List<UsersNameId>? {
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
        } else return null
        return usersNameIdList
    }
}