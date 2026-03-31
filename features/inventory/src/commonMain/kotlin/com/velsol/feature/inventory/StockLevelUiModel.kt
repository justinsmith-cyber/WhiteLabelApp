package com.velsol.feature.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.velsol.feature.inventory.generated.resources.Res
import com.velsol.feature.inventory.generated.resources.in_stock
import com.velsol.feature.inventory.generated.resources.low_stock
import com.velsol.feature.inventory.generated.resources.out_of_stock
import org.jetbrains.compose.resources.stringResource

data class StockLevelUiModel(val label: String, val color: Color)

/**
 * Maps a [StockLevel] to its display label and brand-aware color for use in UI.
 * Callers pass [errorColor] from the composable surface so this stays free of MaterialTheme for colors.
 */
@Composable
fun StockLevel.toUiModel(secondaryColor: Color, errorColor: Color): StockLevelUiModel = when (this) {
    StockLevel.InStock -> StockLevelUiModel(
        label = stringResource(Res.string.in_stock),
        color = secondaryColor,
    )
    StockLevel.LowStock -> StockLevelUiModel(
        label = stringResource(Res.string.low_stock),
        color = Color(0xFFF59E0B),
    )
    StockLevel.OutOfStock -> StockLevelUiModel(
        label = stringResource(Res.string.out_of_stock),
        color = errorColor,
    )
}
