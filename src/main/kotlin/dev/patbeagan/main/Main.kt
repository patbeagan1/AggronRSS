package  dev.patbeagan.main

import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val scrollState = rememberScrollState()

    MaterialTheme {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Blue)

        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .background(Color.Green)
            ) {
                Text("Drawer pane")
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .verticalScroll(scrollState)
            ) {
                Text("Content pane")
                Button(onClick = {
                    text = "Hello, Desktop!"
                }) {
                    Text(text)
                }
                (1..100).forEach {
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Text("Content $it")
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
