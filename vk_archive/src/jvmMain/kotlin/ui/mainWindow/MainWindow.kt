package ui.mainWindow

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import model.AttachmentType
import model.Dialog
import model.UsersNameId
import ui.alertDialog.AboutAlertDialog
import ui.alertDialog.DialogWithContent
import ui.alertDialog.DialogWithContentState
import ui.alertDialog.StatusAlertDialog
import ui.dialogItem.DialogItemAfter
import ui.dialogItem.DialogItemBefore
import utils.DefaultCheckboxListManager
import utils.HideableCheckboxListManager
import windowManager.WindowsManager

@Composable
fun FrameWindowScope.MainWindow() {
    val viewModel = MainWindowViewModel()

    val chosenFolderState = remember { viewModel.vkArchiveData.currentDirectory }
    val preparedDialogs = remember { viewModel.vkArchiveData.preparedDialogs }
    val currentDialogId = remember { viewModel.currentDialogId }

    val preparedDialogsCheckboxManager = remember { HideableCheckboxListManager() }

    MainWindowTopBar(
        onClickImportButton = { viewModel.importPreparedDialogs() },
        onClickExportButton = { viewModel.exportPreparedDialogs() },
        onClickParseAllButton = { viewModel.parseAllDialogs() },
        onClickDownloadAttachments = {
            viewModel.downloadAttachments(preparedDialogsCheckboxManager.getSelectedIds())
        },
        onClickAboutButton = { viewModel.showAboutAlertDialog() }
    )

    Scaffold {
        initAlertDialogs(viewModel)

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
                    hideableCheckboxListManager = preparedDialogsCheckboxManager,
                    onPreparedDialogClick = { id ->
                        viewModel.currentDialogId.value = id
                    }
                )
            }
        }
    }
}

@Composable
private fun initAlertDialogs(viewModel: MainWindowViewModel) {
    val aboutAlertDialogState: MutableState<Boolean> =
        remember { viewModel.isShowAboutAlertDialog }
    val currentProcessAlertDialogState =
        remember { viewModel.isShowProcessAlertDialog }

    AboutAlertDialog(
        dialogState = aboutAlertDialogState,
        onDismissRequest = { viewModel.hideAboutAlertDialog() }
    )

    StatusAlertDialog(
        dialogState = currentProcessAlertDialogState,
        text = viewModel.processText.value,
        status = viewModel.status.value,
        onDismissRequest = {
            viewModel.processJob?.cancel()
            viewModel.hideProcessAlertDialog()
        })

    DialogWithContent(dialogWithContentState = viewModel.dialogWithContentState)
}

fun createDownloadDialog(
    hideDownloadDialog: () -> Unit,
    downloadAttachments: (attachmentTypes: Set<String>) -> Unit
) = DialogWithContentState(
    title = "Select type of attachments",
    content = {
        val items =
            remember { AttachmentType.getDownloadable().map { it.first() } }
        val checkboxListManager = remember { DefaultCheckboxListManager() }
        Box {
            Box(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    for (itemText in items) {
                        checkboxListManager.createCheckboxState(itemText)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    checkboxListManager.updateCheckboxState(
                                        itemText,
                                        !checkboxListManager.getCheckboxState(itemText).value
                                    )
                                },
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkboxListManager.getCheckboxState(itemText).value,
                                onCheckedChange = {
                                    checkboxListManager.updateCheckboxState(itemText, it)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colors.primary
                                )
                            )
                            Text(itemText)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    downloadAttachments(checkboxListManager.getSelectedIds())
                    hideDownloadDialog()
                }) {
                    Text("Submit")
                }
                Button(onClick = { hideDownloadDialog() }) {
                    Text("Cancel")
                }
            }
        }
    },
    onDismissClick = { hideDownloadDialog() }
)

@OptIn(ExperimentalUnitApi::class)
@Composable
private fun ChosenFolderContent(
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
    hideableCheckboxListManager: HideableCheckboxListManager,
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
                hideableCheckboxListManager,
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
    hideableCheckboxListManager: HideableCheckboxListManager,
    onPreparedDialogClick: (String) -> Unit
) {
    for (dialog in dialogs) {
        hideableCheckboxListManager.createCheckboxState(dialog.id)
        item {
            DialogItemAfter(
                dialog.id,
                dialog.name,
                dialog.messages.size.toLong(),
                dialog.messages.sumOf { it.attachments.size }.toLong(),
                hideableCheckboxListManager.getCheckboxState(dialog.id),
                hideableCheckboxListManager::updateCheckboxState,
                hideableCheckboxListManager::hideCheckbox,
                hideableCheckboxListManager::selectAllCheckboxes,
                hideableCheckboxListManager.showCheckboxes,
                { onPreparedDialogClick(dialog.id) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
