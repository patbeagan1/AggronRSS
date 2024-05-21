package dev.patbeagan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import dev.patbeagan.ui.state.FeedItemUiState

@ExperimentalUnitApi
@Composable
fun FeedRow(
    item: FeedItemUiState,
    isSelected: Boolean,
    setSelected: (FeedItemUiState) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(Dp(10f))
    Row(
        Modifier
            .wrapContentHeight()
            .padding(Dp(2f))
            .shadow(Dp(2f), shape)
            .hoverable(interactionSource)
            .clickable { setSelected(item) }
            .background(
                determineBackgroundColor(isHovered, isSelected), shape = shape
            )
            .padding(Dp(4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = item.title,
                overflow = TextOverflow.Ellipsis,
                softWrap = false
            )
            Text(
                text = item.description,
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )
        }
    }
}

private fun determineBackgroundColor(isHovered: Boolean, isSelected: Boolean): Color = when {
    isSelected && isHovered -> Color.LightGray.copy(blue = 1f)
    isSelected && !isHovered -> Color.LightGray
    !isSelected && isHovered -> Color.Gray
    !isSelected && !isHovered -> Color.White
    else -> Color.Red
}
