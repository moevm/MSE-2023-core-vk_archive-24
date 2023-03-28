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

    fun export(dir:File, dialog: Dialog){
        if (!dir.exists()){
            Files.createDirectory(Paths.get(dir.absolutePath))
        }
        val file = File("${dir.absolutePath}/${dialog.id}.json")

        //обернуть в корутины
        val dialogString = Json.encodeToString(Dialog.serializer(),dialog);
        file.writeText(dialogString,Charsets.UTF_8);
    }

    fun importAll(dir:File):List<Dialog?>{
        val dialogs = dir.listFiles();
        return dialogs.map {
            val dialogString = it.readText(Charsets.UTF_8);
            Json.decodeFromString(Dialog.serializer(),dialogString)
        }
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