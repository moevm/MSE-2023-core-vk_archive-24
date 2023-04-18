package ui.mainWindow

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar


@Composable
fun FrameWindowScope.MainWindowTopBar(
    onClickImportButton: () -> Unit,
    onClickExportButton: () -> Unit,
    onClickParseAllButton: () -> Unit,
    onClickDownloadAttachments: () -> Unit,
    onClickAboutButton: () -> Unit
) {
    MenuBar {
        Menu("File", mnemonic = 'F') {
            Item("Import", onClick = onClickImportButton)
            Item("Export", onClick = onClickExportButton)
        }
        Menu("Parse", mnemonic = 'P') {
            Item("Parse All", onClick = onClickParseAllButton)
        }
        Menu("Dialogs", mnemonic = 'D') {
            Item("Download attachments from dialogs", onClick = onClickDownloadAttachments)
        }
        Menu("About", mnemonic = 'A') {
            Item("About", onClick = onClickAboutButton)
        }
    }
}
