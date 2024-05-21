package dev.patbeagan.domain

import androidx.compose.ui.Modifier

fun Modifier.thenIf(
    condition: Boolean,
    actionSuccess: Modifier.() -> Modifier,
    actionFailure: Modifier.() -> Modifier,
) = if (condition) this.actionSuccess() else this.actionFailure()