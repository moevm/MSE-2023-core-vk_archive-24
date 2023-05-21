package ui.mainWindow

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import model.AttachmentType
import model.Dialog
import model.UsersNameId
import ui.alertDialog.AboutDialog
import ui.alertDialog.DialogWithContent
import ui.alertDialog.DialogWithContentState
import ui.alertDialog.StatusAlertDialog
import ui.dialogItem.DialogItemAfter
import ui.dialogItem.DialogItemBefore
import utils.DefaultCheckboxListManager
import utils.HideableCheckboxListManager
import utils.languages.StringResources
import utils.languages.StringResourcesEN
import utils.languages.StringResourcesRU
import windowManager.WindowsManager

@Composable
fun FrameWindowScope.MainWindow() {
    val viewModel = remember { MainWindowViewModel() }

    val chosenFolderState = viewModel.vkArchiveData.currentDirectory.value
        .ifEmpty { StringResources.currentDirectoryPlaceholder.updatableString() }
    val dialogs = remember { viewModel.filteredDialogs }
    val preparedDialogs = remember { viewModel.filteredPreparedDialogs }
    val currentDialogId = remember { viewModel.currentDialogId }

    val preparedDialogsCheckboxManager = remember { HideableCheckboxListManager() }

    MainWindowTopBar(
        onClickImportButton = { viewModel.importPreparedDialogs() },
        onClickExportButton = { viewModel.exportPreparedDialogs() },
        onClickChangeLanguageToEnglishButton = {
            StringResources.currentLanguage = StringResourcesEN
        },
        onClickChangeLanguageToRussianButton = {
            StringResources.currentLanguage = StringResourcesRU
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
                    viewModel.updateVkArchiveFolder()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ListOfDialogsBefore(
                    dialogs = dialogs,
                    topbarActions = mapOf(
                        StringResources.parseAll.updatableString() to { viewModel.parseAllDialogs() }
                    ),
                    onDialogParsingClick = { id -> viewModel.parseDialog(id) },
                    updateDialogsFilter = { newFilter ->
                        viewModel.nameFilterForDialogs = newFilter
                    })

                ListOfDialogsAfter(
                    preparedDialogs = preparedDialogs,
                    hideableCheckboxListManager = preparedDialogsCheckboxManager,
                    topbarActions = mapOf(
                        StringResources.downloadAttachments.updatableString() to
                                { viewModel.downloadAttachments(preparedDialogsCheckboxManager.getSelectedIds()) }
                    ),
                    onPreparedDialogClick = { id ->
                        viewModel.currentDialogId.value = id
                    },
                    updatePreparedDialogsFilter = { newFilter ->
                        viewModel.nameFilterForPreparedDialogs = newFilter
                    }
                )
            }
        }
    }
}

@Composable
private fun initAlertDialogs(viewModel: MainWindowViewModel) {
    val aboutDialogState: MutableState<Boolean> =
        remember { viewModel.isShowAboutDialog }
    val currentProcessAlertDialogState =
        remember { viewModel.isShowProcessAlertDialog }

    AboutDialog(
        dialogState = aboutDialogState,
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
    title = StringResources.selectTypeOfAttachments.updatableString(),
    content = {
        val items = remember { AttachmentType.getDownloadable(StringResources.currentLanguage.languageType) }
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
                    for ((itemText, isSupported) in items) {
                        checkboxListManager.createCheckboxState(itemText)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable(enabled = isSupported) {
                                    checkboxListManager.updateCheckboxState(
                                        itemText,
                                        !checkboxListManager.getCheckboxState(itemText).value
                                    )
                                },
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                enabled = isSupported,
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
                    Text(StringResources.submit.updatableString())
                }
                Button(onClick = { hideDownloadDialog() }) {
                    Text(StringResources.cancel.updatableString())
                }
            }
        }
    },
    onDismissClick = { hideDownloadDialog() }
)

@Composable
private fun ChosenFolderContent(
    chosenFolder: String,
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
                text = "${StringResources.chosenFolder.updatableString()}: ",
                fontSize = fontSize,
                fontFamily = fontFamily
            )

            Text(
                modifier = Modifier
                    .width(380.dp),
                text = chosenFolder,
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
            Text(StringResources.chooseFolder.updatableString())
        }
    }
}

@Composable
private fun RowScope.ListOfDialogsBefore(
    dialogs: List<UsersNameId>,
    topbarActions: Map<String, () -> Unit>,
    onDialogParsingClick: (String) -> Unit,
    updateDialogsFilter: (String) -> Unit
) {
    val lazyColumnDialogBeforeState = rememberLazyListState()
    val nameFilterDialogs = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .weight(50f)
            .border(
                width = 3.dp,
                color = Color.Gray
            )
            .padding(3.dp)
    ) {
        Column {
            DialogListTopbar(
                nameFilterDialogs,
                updateFilter = { filterValue ->
                    nameFilterDialogs.value = filterValue
                    updateDialogsFilter(filterValue)
                },
                topbarActions
            )
            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 12.dp),
                    state = lazyColumnDialogBeforeState,
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    fillDialogBeforeList(dialogs, onDialogParsingClick)
                }
                VerticalScrollbar(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    adapter = rememberScrollbarAdapter(lazyColumnDialogBeforeState)
                )
            }
        }
    }
}

@Composable
private fun RowScope.ListOfDialogsAfter(
    preparedDialogs: List<Dialog>,
    hideableCheckboxListManager: HideableCheckboxListManager,
    topbarActions: Map<String, () -> Unit>,
    onPreparedDialogClick: (String) -> Unit,
    updatePreparedDialogsFilter: (String) -> Unit
) {
    val lazyColumnDialogAfterState = rememberLazyListState()
    val nameFilterPreparedDialogs = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .weight(50f)
            .border(
                width = 3.dp,
                color = Color.Green
            )
            .padding(3.dp)
    ) {
        Column {
            DialogListTopbar(
                nameFilterPreparedDialogs,
                updateFilter = { filterValue ->
                    nameFilterPreparedDialogs.value = filterValue
                    updatePreparedDialogsFilter(filterValue)
                },
                topbarActions
            )
            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 12.dp),
                    state = lazyColumnDialogAfterState,
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    fillDialogAfterList(
                        preparedDialogs,
                        hideableCheckboxListManager,
                        onPreparedDialogClick
                    )
                }
                VerticalScrollbar(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    adapter = rememberScrollbarAdapter(
                        lazyColumnDialogAfterState
                    )
                )
            }
        }
    }
}

@Composable
private fun DialogListTopbar(
    filterValueState: State<String>,
    updateFilter: (String) -> Unit,
    topbarActions: Map<String, () -> Unit>
) {
    var dropdownMenuExpandedState by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
            value = filterValueState.value,
            onValueChange = { updateFilter(it) },
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Default),
                    onClick = { updateFilter("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
            singleLine = true,
            label = { Text(StringResources.searchAll.updatableString()) },
            placeholder = { Text("${StringResources.nothingHere.updatableString()}...") }
        )
        Column {
            Row(Modifier.fillMaxHeight()) {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                        .padding(top = 16.dp, bottom = 8.dp)
                    ,
                    onClick = {
                        dropdownMenuExpandedState = !dropdownMenuExpandedState
                    }) {
                    Text(StringResources.actions.updatableString())
                }
            }
            DropdownMenu(
                expanded = dropdownMenuExpandedState,
                onDismissRequest = { dropdownMenuExpandedState = false }) {
                for ((text, callback) in topbarActions) {
                    DropdownMenuItem(onClick = {
                        callback()
                        dropdownMenuExpandedState = false
                    }) {
                        Text(text)
                    }
                }
            }
        }
    }
    Divider(Modifier.fillMaxWidth())
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
