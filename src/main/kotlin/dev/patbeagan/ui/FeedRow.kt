package dev.patbeagan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@ExperimentalUnitApi
@Composable
fun FeedRow(item: FeedItem, isSelected: Boolean, setSelected: (FeedItem) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Row(
        Modifier
            .wrapContentHeight()
            .background(determineBackgroundColor(isHovered, isSelected))
            .hoverable(interactionSource)
            .clickable { setSelected(item) }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            Text(
                text = item.title
            )
            Text(
                text = item.description,
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )
        }
    }
}

private fun determineBackgroundColor(isHovered: Boolean, isSelected: Boolean): Color = when {
    isSelected && isHovered -> Color.Blue
    isSelected && !isHovered -> Color.White
    !isSelected && isHovered -> Color.Gray
    !isSelected && !isHovered -> Color.LightGray
    else -> Color.Red
}
