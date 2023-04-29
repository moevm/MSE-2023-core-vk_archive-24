package utils.languages

interface BaseStringResources {
    val languageType: StringResources.Language

    val aboutText: String
    val currentDirectoryPlaceholder: String
    val parseAll: String
    val downloadAttachments: String
    val selectTypeOfAttachments: String
    val submit: String
    val cancel: String
    val chosenFolder: String
    val chooseFolder: String
    val searchAll: String
    val nothingHere: String
    val actions: String
    val select: String
    val selectAll: String
    val clearAll: String
    val amountMsg: String
    val amountAttchm: String
    val about: String
    val status: String
    val file: String
    val import: String
    val export: String
    val language: String
    val ruLanguage: String
    val enLanguage: String
    val parsingDialogs: String
    val parsingDialog: String
    val importDialogs: String
    val exportDialogs: String
    val parse: String

    fun todoTranslate(): String
}
