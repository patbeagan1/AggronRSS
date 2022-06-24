package dev.patbeagan.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TopNav() {
    TopAppBar(
        title = {
            Row(Modifier
                .wrapContentHeight()) {
                Icon(
                    painter = painterResource("aggron-small.png"),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                )
                Text(
                    text = "MyComposeApp",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }, actions = {
            Text(
                text = "Option1",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Option2",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        })
}