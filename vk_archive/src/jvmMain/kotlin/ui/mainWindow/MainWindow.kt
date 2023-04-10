package ui.mainWindow

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
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
import data.UsersNameId
import model.Dialog
import ui.alertDialog.AboutAlertDialog
import ui.alertDialog.StatusAlertDialog
import ui.dialogItem.DialogItemAfter
import ui.dialogItem.DialogItemBefore
import windowManager.WindowsManager

@Composable
@Preview
fun MainWindow() {
    val viewModel = MainWindowViewModel()

    val chosenFolderState = remember { viewModel.vkArchiveData.currentDirectory }
    val aboutAlertDialogState: MutableState<Boolean> =
        remember { viewModel.isShowAboutAlertDialog }
    val currentProcessAlertDialogState =
        remember { viewModel.isShowProcessAlertDialog }
    val dialogs = remember { viewModel.filteredDialogs }
    val preparedDialogs = remember { viewModel.filteredPreparedDialogs }
    val currentDialogId = remember { viewModel.currentDialogId }

    Scaffold(
        topBar = {
            MainWindowTopBar(
                onClickImportButton = { viewModel.importPreparedDialogs() },
                onClickExportButton = { viewModel.exportPreparedDialogs() },
                onClickParseAllButton = { viewModel.parseAllDialogs() },
                onClickAboutButton = { viewModel.showAboutAlertDialog() }
            )
        }
    ) {
        if (aboutAlertDialogState.value)
            AboutAlertDialog(onDismissRequest = { viewModel.hideAboutAlertDialog() })

        if (currentProcessAlertDialogState.value)
            StatusAlertDialog(
                viewModel.processText.value,
                viewModel.processProgress.value,
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
                    viewModel.updateVkArchiveFolder()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp,)
            )
            ListOfDialogs(
                dialogs,
                preparedDialogs,
                onDialogParsingClick = { id -> viewModel.parseDialog(id) },
                onPreparedDialogClick = { id -> viewModel.currentDialogId.value = id },
                updateDialogsFilter = { newFilter ->
                    viewModel.nameFilterForDialogs = newFilter
                },
                updatePreparedDialogsFilter = { newFilter ->
                    viewModel.nameFilterForPreparedDialogs = newFilter
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
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
fun ListOfDialogs(
    dialogs: List<UsersNameId>,
    preparedDialogs: List<Dialog>,
    onDialogParsingClick: (String) -> Unit,
    onPreparedDialogClick: (String) -> Unit,
    updateDialogsFilter: (String) -> Unit,
    updatePreparedDialogsFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyColumnDialogBeforeState = rememberLazyListState()
    val lazyColumnDialogAfterState = rememberLazyListState()

    val nameFilterDialogs = remember { mutableStateOf("") }
    val nameFilterPreparedDialogs = remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(50f)
                .border(
                    width = 3.dp,
                    color = Color.Gray
                )
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    value = nameFilterDialogs.value,
                    onValueChange = {
                        nameFilterDialogs.value = it
                        updateDialogsFilter(it)
                    },
                    singleLine = true,
                    label = { Text("Поиск по всем") },
                    placeholder = { Text("Здесь пока пусто...") }
                )
                Divider(Modifier.fillMaxWidth())
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

        Box(
            modifier = Modifier
                .weight(50f)
                .border(
                    width = 3.dp,
                    color = Color.Green
                )
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    value = nameFilterPreparedDialogs.value,
                    onValueChange = {
                        nameFilterPreparedDialogs.value = it
                        updatePreparedDialogsFilter(it)
                    },
                    singleLine = true,
                    label = { Text("Поиск по всем") },
                    placeholder = { Text("Здесь пока пусто...") }
                )
                Divider(Modifier.fillMaxWidth())
                LazyColumn(
                    state = lazyColumnDialogAfterState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    fillDialogAfterList(preparedDialogs, onPreparedDialogClick)
                }
            }
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
    onPreparedDialogClick: (String) -> Unit
) {
    for (dialog in dialogs) {
        item {
            DialogItemAfter(
                dialog.id,
                dialog.name,
                dialog.messages.size.toLong(),
                dialog.messages.sumOf { it.attachments.size }.toLong(),
                { onPreparedDialogClick(dialog.id) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
