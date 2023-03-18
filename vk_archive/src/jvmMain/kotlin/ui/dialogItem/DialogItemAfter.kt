package ui.dialogItem

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalUnitApi::class)
@Composable
fun DialogItemAfter(
    id: String,
    title: String,
    amountMessages: Long,
    amountAttachments: Long,
    onNavigateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable(onClick = onNavigateClick)
            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(14)),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(30f)
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = TextUnit(18f, TextUnitType.Sp),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "(id#$id)",
                fontSize = TextUnit(14f, TextUnitType.Sp),
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = Color.Gray,
        )
        Column(
            modifier = Modifier
                .weight(18f)
        ) {
            Text("Amount msg: $amountMessages")
            Text("Amount attchm: $amountAttachments")
        }
    }
}

@Preview
@Composable
fun showDialogItemAfter() {
    DialogItemAfter(
        "123857163284",
        "Mr. Dow",
        120L,
        15L,
        {},
        Modifier
            .width(450.dp)
            .height(40.dp)
    )
}
