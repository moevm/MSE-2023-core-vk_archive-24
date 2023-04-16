package ui.alertDialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.window.Dialog

@Composable
fun DialogWithContent(dialogWithContentState: State<DialogWithContentState?>) {
    if (dialogWithContentState.value != null) {
        val (title, onDismissClick, content) = dialogWithContentState.value ?: return
        Dialog(
            title = title,
            onCloseRequest = onDismissClick,
            resizable = false
        ) {
            content()
        }
    }
}

data class DialogWithContentState(
    val title: String,
    val onDismissClick: () -> Unit,
    val content: @Composable () -> Unit = {},
)
