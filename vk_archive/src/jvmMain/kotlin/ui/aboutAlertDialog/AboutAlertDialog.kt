package ui.aboutAlertDialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutAlertDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        modifier = Modifier
            .width(350.dp)
            .height(IntrinsicSize.Min),
        text = {
            Text(modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
                text = """
                    VK Archive
                    This program can parsing Vk Archive and display it for you in a convenient format
                    
                    Instruction: 
                    1) Unzip VK Archive in folder
                    2) Choose your folder by "Choose folder"
                    3) ...
                """.trimIndent(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center)
        },
        onDismissRequest = onDismissRequest,
        buttons = {}
    )
}