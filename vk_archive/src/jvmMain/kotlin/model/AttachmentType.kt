package model

enum class AttachmentType(val translates: List<String>) {
    PHOTO(listOf("Фотография", "Photo")),
    VIDEO(listOf("Видеозапись")),
    GIFT(listOf("Подарок")),
    FILE(listOf("Файл")),
    STICKER(listOf("Стикер")),
    URL(listOf("Ссылка")),
    AUDIO(listOf("Аудиозапись")),
    CALL(listOf("Звонок")),
    POST(listOf("Запись на стене"));

    companion object {
        @JvmStatic
        fun getDownloadable() = listOf(
            PHOTO.translates,
            VIDEO.translates,
            FILE.translates
        )
    }
}