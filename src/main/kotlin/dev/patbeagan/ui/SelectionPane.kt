package dev.patbeagan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp

@ExperimentalUnitApi
@Composable
fun SelectionPane(selectedContent: Content, setSelectedContent: (Content) -> Unit) {
    var currentWidth by remember { mutableStateOf(0f) }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .widthIn(min = 100.dp)
            .width(with(LocalDensity.current) { currentWidth.toDp() })
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    currentWidth += delta
                }
            )
            .fillMaxHeight()
            .background(Color.Yellow)
            .verticalScroll(scrollState)
    ) {
        (1..100).forEach { each ->
            FeedRow(
                FeedItem(
                    each,
                    "Title $each",
                    "Desc $each"
                ),
                selectedContent.id == each
            ) {
                setSelectedContent(Content(it.id, "Content for ${it.id}"))
            }
        }
    }
}