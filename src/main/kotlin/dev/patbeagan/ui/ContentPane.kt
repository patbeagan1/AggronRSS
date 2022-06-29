package dev.patbeagan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.patbeagan.data.dao.FeedItem
import kotlinx.coroutines.launch

@Composable
fun ContentPane(
    snackbarHostState: SnackbarHostState,
    selectedContent: FeedItem?,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .widthIn(min = 100.dp)
            .background(Color.Gray)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Red)
                .padding(4.dp)
                .background(Color.White, shape = RoundedCornerShape(4.dp))
                .padding(4.dp)
                .verticalScroll(scrollState)
        ) {
            Text("Content pane")

            Text(selectedContent?.description ?: "NONE")

            Button(
                shape = RoundedCornerShape(percent = 25),
                content = {
                    Icon(Icons.Rounded.Info, "info")
                    Text("Hello world")
                },
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Clicked the button")
                    }
                }
            )
        }
    }
}