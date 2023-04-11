package ui.dialogItem

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
    checkboxState: State<Boolean>,
    updateCheckboxState: (id: String, state: Boolean) -> Unit,
    hideCheckboxes: () -> Unit,
    selectAllCheckboxes: () -> Unit,
    showingCheckbox: State<Boolean>,
    onNavigateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ContextMenuArea(items = {
        listOf(ContextMenuItem("Select") {
            updateCheckboxState(id, !checkboxState.value)
        }, ContextMenuItem("Select all") {
            selectAllCheckboxes()
        }, ContextMenuItem("Clear all") {
            hideCheckboxes()
        })
    }) {
        Row(
            modifier = modifier
                .height(IntrinsicSize.Min)
                .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(14))
                .clickable(onClick = {
                    if (!showingCheckbox.value)
                        onNavigateClick()
                    else
                        updateCheckboxState(id, !checkboxState.value)
                }),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (showingCheckbox.value) {
                Checkbox(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically),
                    checked = checkboxState.value,
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary),
                    onCheckedChange = {
                        updateCheckboxState(id, it)
                    })
            }
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
}
