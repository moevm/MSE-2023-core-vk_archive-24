package utils.languages

object StringResourcesEN: BaseStringResources {
    override val languageType = StringResources.Language.ENGLISH

    override val aboutText = todoTranslate()

    override val currentDirectoryPlaceholder = "Please, choose VK Archive folder"
    override val parseAll = "Parse All"
    override val downloadAttachments = "Download attachments"
    override val selectTypeOfAttachments = "Select type of attachments"
    override val submit = "Submit"
    override val cancel = "Cancel"
    override val chosenFolder = "Chosen Folder"
    override val chooseFolder = "Choose Folder"
    override val searchAll = "Search all"
    override val nothingHere = "Nothing here"
    override val actions = "Actions"
    override val select = "Select"
    override val selectAll = "Select all"
    override val clearAll = "Clear all"
    override val amountMsg = "Amount msg"
    override val amountAttchm = "Amount attchm"
    override val about = "About"
    override val status = "Status"
    override val file = "File"
    override val import = "Import"
    override val export = "Export"
    override val language = "Language"
    override val ruLanguage = "Russian"
    override val enLanguage = "English"
    override val parsingDialogs = "Parsing dialogs"
    override val parsingDialog = "Parsing dialog"
    override val importDialogs = "Import dialogs"
    override val exportDialogs = "Export dialogs"
    override val parse = "Parse"

    override fun todoTranslate(): String {
        println("Missing english translation")
        return "Error"
    }
}
