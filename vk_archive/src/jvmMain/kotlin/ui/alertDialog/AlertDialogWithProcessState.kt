package ui.alertDialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class AlertDialogWithProcessState(
    val isShow: MutableState<Boolean> = mutableStateOf(false),
    val status: MutableState<String> = mutableStateOf(""),
    val processText: MutableState<String> = mutableStateOf(""),
)
