package utils.languages

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


object StringResources {
    var currentLanguage: BaseStringResources = StringResourcesEN
        set(newLanguage) {
            if (currentLanguage != newLanguage) {
                field = newLanguage
                notifyLanguageChange()
            }
        }

    private val parentListOfLanguageStrings = mutableListOf<LanguageString>()

    private fun notifyLanguageChange() {
        parentListOfLanguageStrings.forEach { it.changeString(currentLanguage) }
    }

    private fun createString(updateString: (BaseStringResources) -> String) =
        LanguageString(currentLanguage, updateString)

    val aboutText = createString { currentLanguage.aboutText }
    val currentDirectoryPlaceholder =
        createString { currentLanguage.currentDirectoryPlaceholder }
    val parseAll = createString { currentLanguage.parseAll }
    val downloadAttachments =
        createString { currentLanguage.downloadAttachments }
    val selectTypeOfAttachments =
        createString { currentLanguage.selectTypeOfAttachments }
    val submit = createString { currentLanguage.submit }
    val cancel = createString { currentLanguage.cancel }
    val chosenFolder = createString { currentLanguage.chosenFolder }
    val chooseFolder = createString { currentLanguage.chooseFolder }
    val searchAll = createString { currentLanguage.searchAll }
    val nothingHere = createString { currentLanguage.nothingHere }
    val actions = createString { currentLanguage.actions }
    val select = createString { currentLanguage.select }
    val selectAll = createString { currentLanguage.selectAll }
    val clearAll = createString { currentLanguage.clearAll }
    val amountMsg = createString { currentLanguage.amountMsg }
    val amountAttchm = createString { currentLanguage.amountAttchm }
    val about = createString { currentLanguage.about }
    val status = createString { currentLanguage.status }
    val file = createString { currentLanguage.file }
    val import = createString { currentLanguage.import }
    val export = createString { currentLanguage.export }
    val language = createString { currentLanguage.language }
    val ruLanguage = createString { currentLanguage.ruLanguage }
    val enLanguage = createString { currentLanguage.enLanguage }
    val parsingDialogs = createString { currentLanguage.parsingDialogs }
    val parsingDialog = createString { currentLanguage.parsingDialog }
    val importDialogs = createString { currentLanguage.importDialogs }
    val exportDialogs = createString { currentLanguage.exportDialogs }
    val parse = createString { currentLanguage.parse }

    enum class Language {
        ENGLISH,
        RUSSIAN
    }

    class LanguageString(
        initStringResources: BaseStringResources,
        private val getString: (BaseStringResources) -> String
    ) {
        init {
            parentListOfLanguageStrings.add(this)
        }

        private var string: MutableState<String> = mutableStateOf(
            getString(initStringResources)
        )

        fun updatableString() = string.value

        fun changeString(newLanguage: BaseStringResources) {
            string.value = getString(newLanguage)
        }
    }
}
