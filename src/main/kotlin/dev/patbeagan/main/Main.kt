package  dev.patbeagan.main

import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.patbeagan.ui.App

@ExperimentalUnitApi
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) { App() }
}
