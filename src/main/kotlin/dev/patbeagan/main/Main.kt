package  dev.patbeagan.main

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.patbeagan.data.config.DatabaseConfig
import dev.patbeagan.ui.App
import kotlinx.coroutines.launch

@ExperimentalUnitApi
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        DatabaseConfig.init()
        val scope = rememberCoroutineScope()
        val viewModel = ViewModel()

        scope.launch {
            viewModel.createTestData()
            viewModel.repo.fetch("https://patbeagan.dev/feed-old.xml")
        }
        App()
    }
}
