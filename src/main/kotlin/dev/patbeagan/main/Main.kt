package  dev.patbeagan.main


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.patbeagan.ui.Header

@Composable
@Preview
fun App() {
    val text by remember { mutableStateOf("Hello, World!") }
    val scrollState = rememberScrollState()

    MaterialTheme {
        Column {
            Header()
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Blue)

            ) {
                DrawerPane()
                ContentPane(scrollState, text)
            }
        }
    }
}

@Composable
fun ContentPane(scrollState: ScrollState, text: String) {
    var text1 = text
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
            .verticalScroll(scrollState)
    ) {
        Text("Content pane")
        Button(onClick = {
            text1 = "Hello, Desktop!"
        }) {
            Text(text1)
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

@Composable
private fun DrawerPane() {
    Column(
        Modifier
            .fillMaxHeight()
            .background(Color.Green)
    ) {
        Text("Drawer pane")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
