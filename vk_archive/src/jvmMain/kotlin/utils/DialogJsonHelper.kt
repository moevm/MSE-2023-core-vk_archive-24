package utils

import kotlinx.serialization.json.Json
import model.Dialog
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object DialogJsonHelper {
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
        updateProcessStatus: (Double) -> Unit,
        checkActiveState: () -> Boolean
    ): List<Dialog> {
        val dialogs = dir.listFiles() ?: return emptyList()
        val result = ArrayList<Dialog>(dialogs.size)
        for ((index, file) in dialogs.withIndex()) {
            if (checkActiveState()) {
                updateProcessStatus((index + 1).toDouble() / dialogs.size * 100)
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
}