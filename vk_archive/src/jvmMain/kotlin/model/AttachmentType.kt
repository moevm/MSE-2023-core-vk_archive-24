package model

import utils.languages.StringResources

enum class AttachmentType(val translates: Map<StringResources.Language, String>) {
    PHOTO(
        mapOf(
            StringResources.Language.RUSSIAN to "Фотография",
            StringResources.Language.ENGLISH to "Photo"
        )
    ),
    VIDEO(
        mapOf(
            StringResources.Language.RUSSIAN to "Видеозапись",
            StringResources.Language.ENGLISH to "Video"
        )
    ),
    GIFT(
        mapOf(
            StringResources.Language.RUSSIAN to "Подарок",
            StringResources.Language.ENGLISH to "Gift"
        )
    ),
    FILE(
        mapOf(
            StringResources.Language.RUSSIAN to "Файл",
            StringResources.Language.ENGLISH to "File"
        )
    ),
    STICKER(
        mapOf(
            StringResources.Language.RUSSIAN to "Стикер",
            StringResources.Language.ENGLISH to "Sticker"
        )
    ),
    URL(
        mapOf(
            StringResources.Language.RUSSIAN to "Ссылка",
            StringResources.Language.ENGLISH to "Link"
        )
    ),
    AUDIO(
        mapOf(
            StringResources.Language.RUSSIAN to "Аудиозапись",
            StringResources.Language.ENGLISH to "Audio file"
        )
    ),
    CALL(
        mapOf(
            StringResources.Language.RUSSIAN to "Звонок",
            StringResources.Language.ENGLISH to "Call"
        )
    ),
    POST(
        mapOf(
            StringResources.Language.RUSSIAN to "Запись на стене",
            StringResources.Language.ENGLISH to "Wall post"
        )
    ),
    COMMENT(
        mapOf(
            StringResources.Language.RUSSIAN to "Комментарий на стене",
            StringResources.Language.ENGLISH to "Wall comment"
        )
    );

    companion object {
        @JvmStatic
        fun getDownloadable(language: StringResources.Language) = listOf(
            (PHOTO.translates[language] ?: "") to true,
            (VIDEO.translates[language] ?: "") to false,
            (FILE.translates[language] ?: "") to false
        )
    }
}