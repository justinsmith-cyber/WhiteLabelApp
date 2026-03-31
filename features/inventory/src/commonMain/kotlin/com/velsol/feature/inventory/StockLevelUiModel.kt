package com.velsol.feature.inventory

import androidx.compose.ui.graphics.Color

data class StockLevelUiModel(val label: String, val color: Color)
private val LowStockColor = Color(0xFFF59E0B)

/**
 * Maps a [StockLevel] to its display label and brand-aware color for use in UI.
 * Callers pass localized labels and colors from the composable surface to keep mapping pure/testable.
 */
fun StockLevel.toUiModel(
    inStockLabel: String,
    lowStockLabel: String,
    outOfStockLabel: String,
    secondaryColor: Color,
    errorColor: Color,
): StockLevelUiModel = when (this) {
    // Structural intent: keep StockLevel mapping pure while composables provide localized resources.
    StockLevel.InStock -> StockLevelUiModel(
        label = inStockLabel,
        color = secondaryColor,
    )
    StockLevel.LowStock -> StockLevelUiModel(
        label = lowStockLabel,
        color = LowStockColor,
    )
    StockLevel.OutOfStock -> StockLevelUiModel(
        label = outOfStockLabel,
        color = errorColor,
    )
}
