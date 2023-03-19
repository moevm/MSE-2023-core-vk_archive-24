package ui.dialogItem

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalUnitApi::class)
@Composable
fun DialogItemBefore(
    title: String,
    onParsingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(35f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                fontSize = TextUnit(18f, TextUnitType.Sp),
                overflow = TextOverflow.Ellipsis
            )
        }

        Button(
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp),
            onClick = onParsingClick
        ) {
            Text("Parsing")
        }
    }
}

@Preview
@Composable
fun showDialogItemBefore() {
    DialogItemBefore(
        "Mr. Dow",
        {},
        Modifier
            .height(40.dp)
            .width(350.dp)
    )
}