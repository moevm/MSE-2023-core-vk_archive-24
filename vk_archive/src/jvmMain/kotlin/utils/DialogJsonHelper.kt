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

    /**
     * Принимает на вход директорию dir, куда нужно сохранить объект Dialog в формате JSON. Функция проверяет существование указанной директории и при отсутствии создает ее. Затем формируется путь к файлу с помощью dialog.id, где id - уникальный идентификатор диалога. Далее производится сериализация объекта dialog в JSON формат и запись в файл с помощью file.writeText().
     * **/
    fun export(dir: File, dialog: Dialog) {
        if (!dir.exists()) {
            Files.createDirectory(Paths.get(dir.absolutePath))
        }
        val file = File("${dir.absolutePath}/${dialog.id}.json")

        val dialogString = Json.encodeToString(Dialog.serializer(), dialog)
        file.writeText(dialogString, Charsets.UTF_8)
    }

    /**
     * Принимает на вход директорию с диалогами в виде объекта File и использует переданные функции updateProcessStatus и checkActiveState для обновления информации о прогрессе и проверки состояния процесса импорта всех диалогов. Импортирует каждый файл диалога из директории, парсит JSON-строку, используя Json.decodeFromString, и добавляет декодированный объект Dialog в список результатов. Возвращает список всех импортированных диалогов.
     * **/
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
     * Принимает объект типа File и список ids (id диалогов), обрабатывает каждый id в списке и пытается импортировать соответствующий файл JSON из директории, указанной в параметре dir. Преобразовывает его содержимое в объект типа Dialog с помощью функции Json.decodeFromString и возвращает его (Если файл не существует, вернет значение null)
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