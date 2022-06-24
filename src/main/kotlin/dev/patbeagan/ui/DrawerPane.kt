package dev.patbeagan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun DrawerPane() {
    var currentWidth by remember { mutableStateOf(100f) }
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
            .background(Color.Green)
    ) {
        Text("Drawer pane")
    }
}