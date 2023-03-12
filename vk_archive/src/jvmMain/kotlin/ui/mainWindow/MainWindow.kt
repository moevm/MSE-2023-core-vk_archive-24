package ui.mainWindow

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
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
import ui.aboutAlertDialog.AboutAlertDialog
import ui.dialogItem.DialogItemBefore
import java.io.File

@Composable
@Preview
fun MainWindow() {
    val viewModel = MainWindowViewModel()

    val chosenFolderState = remember { viewModel.currentDirectory }
    val folderState: MutableState<File?> = remember { viewModel.currentFolder }
    val aboutAlertDialogState: MutableState<Boolean> =
        remember { viewModel.isShowAboutAlertDialog }

    Scaffold(
        topBar = {
            MainWindowTopBar(
                onClickAboutButton = { viewModel.showAboutAlertDialog() }
            )
        }
    ) {
        if (aboutAlertDialogState.value)
            AboutAlertDialog { viewModel.hideAboutAlertDialog() }

        Column(
            modifier = Modifier
                .width(700.dp)
        ) {
            ChosenFolderContent(
                chosenFolderState,
                onChooseFolderClick = { viewModel.chooseFolder() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp,)
            )
            ListOfDialogs(
                folderState.value?.listFiles()
                    ?.map { viewModel.getFriendUserName(it.name) ?: it.name } ?: listOf(),
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
    dialogs: List<String>,
    modifier: Modifier = Modifier
) {
    val lazyColumnState = rememberLazyListState()

    Box(
        modifier = modifier
            .border(
                width = 3.dp,
                color = Color.Gray
            )
    ) {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)

        ) {
            fillDialogList(dialogs)
        }
    }
}

private fun LazyListScope.fillDialogList(dialogs: List<String>) {
    for (dialogTitle in dialogs) {
        item {
            DialogItemBefore(
                title = dialogTitle,
                onParsingClick = {},
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
            )
            Divider(Modifier.fillMaxWidth())
        }
    }
}



