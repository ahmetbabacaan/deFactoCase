package com.babacan.defactocase.presentation.components

import androidx.compose.ui.Modifier

inline fun Modifier.conditional(
    condition: Boolean,
    block: Modifier.() -> Modifier
) = if (condition) {
    this.block()
} else {
    this
}