package ui.mainWindow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import model.Dialog
import model.UsersNameId
import ui.aboutAlertDialog.AboutAlertDialog
import ui.alertDialog.StatusAlertDialog
import ui.dialogItem.DialogItemAfter
import ui.dialogItem.DialogItemBefore
import windowManager.WindowsManager

@Composable
fun FrameWindowScope.MainWindow() {
    val viewModel = MainWindowViewModel()

    val chosenFolderState = remember { viewModel.vkArchiveData.currentDirectory }
    val aboutAlertDialogState: MutableState<Boolean> =
        remember { viewModel.isShowAboutAlertDialog }
    val currentProcessAlertDialogState =
        remember { viewModel.isShowProcessAlertDialog }
    val preparedDialogs = remember { viewModel.vkArchiveData.preparedDialogs }
    val currentDialogId = remember { viewModel.currentDialogId }

    val preparedDialogsCheckboxManager = remember { CheckboxListManager() }

    MainWindowTopBar(
        onClickImportButton = { viewModel.importPreparedDialogs() },
        onClickExportButton = { viewModel.exportPreparedDialogs() },
        onClickParseAllButton = { viewModel.parseAllDialogs() },
        onClickDownloadImages = {
            viewModel.downloadImages(preparedDialogsCheckboxManager.getSelectedIds())
        },
        onClickAboutButton = { viewModel.showAboutAlertDialog() }
    )

    Scaffold {
        if (aboutAlertDialogState.value)
            AboutAlertDialog(onDismissRequest = { viewModel.hideAboutAlertDialog() })

        if (currentProcessAlertDialogState.value)
            StatusAlertDialog(
                viewModel.processText.value,
                viewModel.status.value,
                onDismissRequest = {
                    viewModel.processJob?.cancel()
                    viewModel.hideProcessAlertDialog()
                })

        if (currentDialogId.value != null) {
            WindowsManager.prepareDialogWindow(
                id = currentDialogId.value ?: "error id",
                onExitClick = {
                    currentDialogId.value = null
                })
        }

        Column {
            ChosenFolderContent(
                chosenFolderState,
                onChooseFolderClick = {
                    viewModel.chooseFolder()
                    viewModel.prepareDialogsList()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ListOfDialogsBefore(
                    dialogs = viewModel.vkArchiveData.dialogsData,
                    onDialogParsingClick = { id ->
                        viewModel.currentDialogId.value = id
                    })

                ListOfDialogsAfter(
                    preparedDialogs = preparedDialogs,
                    checkboxListManager = preparedDialogsCheckboxManager,
                    onPreparedDialogClick = { id ->
                        viewModel.currentDialogId.value = id
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ChosenFolderContent(
    chosenFolderState: MutableState<String>,
    onChooseFolderClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fontSize = TextUnit(16f, TextUnitType.Sp)
    val fontFamily = FontFamily.SansSerif

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = "Chosen Folder: ",
                fontSize = fontSize,
                fontFamily = fontFamily
            )

            Text(
                modifier = Modifier
                    .width(380.dp),
                text = chosenFolderState.value,
                fontSize = fontSize,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onClick = onChooseFolderClick
        ) {
            Text("Choose Folder")
        }
    }
}

@Composable
private fun RowScope.ListOfDialogsBefore(
    dialogs: List<UsersNameId>,
    onDialogParsingClick: (String) -> Unit
) {
    val lazyColumnDialogBeforeState = rememberLazyListState()
    Box(
        modifier = Modifier
            .weight(50f)
            .border(
                width = 3.dp,
                color = Color.Gray
            )
    ) {
        LazyColumn(
            state = lazyColumnDialogBeforeState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            fillDialogBeforeList(dialogs, onDialogParsingClick)
        }
    }
}

@Composable
private fun RowScope.ListOfDialogsAfter(
    preparedDialogs: List<Dialog>,
    checkboxListManager: CheckboxListManager,
    onPreparedDialogClick: (String) -> Unit
) {
    val lazyColumnDialogAfterState = rememberLazyListState()

    Box(
        modifier = Modifier
            .weight(50f)
            .border(
                width = 3.dp,
                color = Color.Green
            )
    ) {
        LazyColumn(
            state = lazyColumnDialogAfterState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            fillDialogAfterList(
                preparedDialogs,
                checkboxListManager,
                onPreparedDialogClick
            )
        }
    }
}

private fun LazyListScope.fillDialogBeforeList(
    dialogs: List<UsersNameId>,
    onDialogParsingClick: (String) -> Unit
) {
    for ((id, dialogTitle) in dialogs) {
        item {
            DialogItemBefore(
                title = dialogTitle,
                onParsingClick = { onDialogParsingClick(id) },
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
            )
            Divider(Modifier.fillMaxWidth())
        }
    }
}

private fun LazyListScope.fillDialogAfterList(
    dialogs: List<Dialog>,
    checkboxListManager: CheckboxListManager,
    onPreparedDialogClick: (String) -> Unit
) {
    for (dialog in dialogs) {
        checkboxListManager.createCheckboxState(dialog.id)
        item {
            DialogItemAfter(
                dialog.id,
                dialog.name,
                dialog.messages.size.toLong(),
                dialog.messages.sumOf { it.attachments.size }.toLong(),
                checkboxListManager.getCheckboxState(dialog.id),
                checkboxListManager::updateCheckboxState,
                checkboxListManager::hideCheckbox,
                checkboxListManager::selectAllCheckboxes,
                checkboxListManager.showCheckboxes,
                { onPreparedDialogClick(dialog.id) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
