package ui.dialogItem

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import utils.languages.StringResources

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
            Text(StringResources.parse.updatableString())
        }
    }
}
