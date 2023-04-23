package ui.alertDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.mikepenz.markdown.Markdown
import utils.StringResources

@Composable
fun AboutDialog(
    dialogState: State<Boolean>,
    onDismissRequest: () -> Unit
) {
    if (dialogState.value) {
        Dialog(
            state = rememberDialogState(
                WindowPosition(Alignment.Center),
                DpSize(600.dp, 600.dp)
            ),
            title = "About",
            onCloseRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Markdown(content = StringResources.aboutText)
            }
        }
    }
}