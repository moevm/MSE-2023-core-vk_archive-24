package model

enum class AttachmentType(val translates: List<String>) {
    PHOTO(listOf("Фотография", "Photo")),
    VIDEO(listOf("Видеозапись", "Video")),
    GIFT(listOf("Подарок", "Gift")),
    FILE(listOf("Файл", "File")),
    STICKER(listOf("Стикер", "Sticker")),
    URL(listOf("Ссылка", "Link")),
    AUDIO(listOf("Аудиозапись", "Audio file")),
    CALL(listOf("Звонок", "Call")),
    POST(listOf("Запись на стене", "Wall post")),
    COMMENT(listOf("Комментарий на стене", "Wall comment"));

    companion object {
        @JvmStatic
        fun getDownloadable() = listOf(
            PHOTO.translates,
            VIDEO.translates,
            FILE.translates
        )
    }
}