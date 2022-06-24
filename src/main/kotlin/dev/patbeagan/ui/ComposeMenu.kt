package dev.patbeagan.main.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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