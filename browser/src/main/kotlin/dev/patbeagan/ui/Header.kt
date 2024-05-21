package dev.patbeagan.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
 fun Header() {
    Row {
        var selectedIndex by remember { mutableStateOf(0) }
        ComposeMenu(
            listOf("Reddit", "Twitter", "Github"),
            selectedIndex
        ) { selectedIndex = it }
    }
}