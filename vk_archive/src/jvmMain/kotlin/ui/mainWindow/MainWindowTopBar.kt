package ui.mainWindow

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import utils.languages.StringResources


@Composable
fun FrameWindowScope.MainWindowTopBar(
    onClickImportButton: () -> Unit,
    onClickExportButton: () -> Unit,
    onClickChangeLanguageToEnglishButton: () -> Unit,
    onClickChangeLanguageToRussianButton: () -> Unit,
    onClickAboutButton: () -> Unit
) {
    MenuBar {
        Menu(StringResources.file.updatableString(), mnemonic = StringResources.file.updatableString()[0]) {
            Item(StringResources.import.updatableString(), onClick = onClickImportButton)
            Item(StringResources.export.updatableString(), onClick = onClickExportButton)
        }
        Menu(StringResources.language.updatableString(), mnemonic = StringResources.language.updatableString()[0]) {
            Item(StringResources.enLanguage.updatableString(), onClick = onClickChangeLanguageToEnglishButton)
            Item(StringResources.ruLanguage.updatableString(), onClick = onClickChangeLanguageToRussianButton)
        }
        Menu(StringResources.about.updatableString(), mnemonic = StringResources.about.updatableString()[0]) {
            Item(StringResources.about.updatableString(), onClick = onClickAboutButton)
        }
    }
}
