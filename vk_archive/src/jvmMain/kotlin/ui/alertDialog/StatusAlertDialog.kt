package ui.alertDialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatusAlertDialog(
    dialogState: State<Boolean>,
    text: String,
    status: String,
    onDismissRequest: () -> Unit
) {
    if (dialogState.value) {
        AlertDialog(
            modifier = Modifier
                .width(350.dp)
                .height(IntrinsicSize.Min),
            text = {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    text = """$text 
                    |Status: $status""".trimMargin(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            onDismissRequest = {},
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}