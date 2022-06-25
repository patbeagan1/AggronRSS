package dev.patbeagan.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import dev.patbeagan.data.NetworkConfig
import dev.patbeagan.data.RemoteRssDataSource
import dev.patbeagan.data.RssRepository
import kotlinx.coroutines.launch

@ExperimentalUnitApi
@Composable
@Preview
fun App() {
    val repository = remember { RssRepository(RemoteRssDataSource(NetworkConfig.httpClient)) }
    val scope = rememberCoroutineScope()
    scope.launch {
        val fetchBasic = repository.fetchBasic()
        println(fetchBasic)
    }
    val scaffoldState = rememberScaffoldState()
    var selectedContent by remember { mutableStateOf(Content(-1, "default")) }
    MaterialTheme {
        Scaffold(
            topBar = { TopNav() },
            snackbarHost = { SnackbarHost(scaffoldState.snackbarHostState) }
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Blue)
                ) {
                    DrawerPane()
                    SelectionPane(selectedContent) { selectedContent = it }
                    ContentPane(scaffoldState.snackbarHostState, selectedContent)
                }
            }
        }
    }
}

