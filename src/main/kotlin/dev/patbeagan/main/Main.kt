package  dev.patbeagan.main


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun ComposeMenu(options: List<String>, selectedIndex: Int, setSelectedIndex: (Int) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    Column {
        Button(
            modifier = Modifier.wrapContentWidth(),
            border = BorderStroke(width = 1.dp, color = Color.Red),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = { expanded.value = !expanded.value },
            content = { Text(options[selectedIndex]) }
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = !expanded.value },
            modifier = Modifier.background(Color.White)
                .border(BorderStroke(width = 1.dp, color = Color.DarkGray))
                .padding(2.dp)
                .shadow(elevation = 2.dp)
                .width(200.dp),
        ) {
            options.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    setSelectedIndex(index)
                    expanded.value = false
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

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
private fun Header() {
    Row {
        var selectedIndex by remember { mutableStateOf(0) }
        ComposeMenu(
            listOf("Reddit", "Twitter", "Github"),
            selectedIndex
        ) { selectedIndex = it }
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
