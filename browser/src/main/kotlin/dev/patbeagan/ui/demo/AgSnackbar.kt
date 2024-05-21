package dev.patbeagan.ui.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun AgSnackbar(showSnackbar: Boolean, setShowSnackbar: (Boolean) -> Unit) {
    AnimatedVisibility(
        showSnackbar,
    ) {
        Snackbar(modifier = Modifier.wrapContentHeight()) {
            Text("snack")
        }
    }
    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            delay(3000)
            setShowSnackbar(false)
        }
    }
}