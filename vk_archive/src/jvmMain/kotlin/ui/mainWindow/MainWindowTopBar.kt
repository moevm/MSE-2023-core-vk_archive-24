package ui.mainWindow

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun MainWindowTopBar(
    onClickImportButton: () -> Unit,
    onClickExportButton: () -> Unit,
    onClickParseAllButton: () -> Unit,
    onClickAboutButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = Color.White
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onClickImportButton) {
                Text("Import")
            }
            Button(onClick = onClickExportButton) {
                Text("Export")
            }
            Button(onClick = onClickParseAllButton) {
                Text("Parse All")
            }
            Button(onClick = onClickAboutButton) {
                Text("About")
            }
        }
    }
}
