package com.velsol.feature.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velsol.feature.inventory.generated.resources.Res
import com.velsol.feature.inventory.generated.resources.in_stock
import com.velsol.feature.inventory.generated.resources.in_stock_count
import com.velsol.feature.inventory.generated.resources.items_tracked
import com.velsol.feature.inventory.generated.resources.low_stock
import com.velsol.feature.inventory.generated.resources.low_stock_count
import com.velsol.feature.inventory.generated.resources.out_of_stock
import com.velsol.feature.inventory.generated.resources.parts_materials
import com.velsol.feature.inventory.generated.resources.plumbing_inventory
import com.velsol.feature.inventory.generated.resources.quantity_unit
import com.velsol.feature.inventory.generated.resources.sku_label
import com.velsol.feature.inventory.generated.resources.stock_levels
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.stringResource

@Composable
fun InventoryContent(
    component: InventoryListComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)
    val secondary = Color(brandConfig.secondaryColorArgb)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = primary),
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(Res.string.plumbing_inventory),
                        color = onPrimary.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.parts_materials),
                        color = onPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.items_tracked, state.items.size),
                        color = onPrimary.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatBadge(
                            label = stringResource(Res.string.in_stock_count, state.inStockCount),
                            containerColor = onPrimary.copy(alpha = 0.18f),
                            contentColor = onPrimary,
                        )
                        StatBadge(
                            label = stringResource(Res.string.low_stock_count, state.lowCount),
                            containerColor = onPrimary.copy(alpha = 0.18f),
                            contentColor = onPrimary,
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(Res.string.stock_levels),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }

        // Stable key by SKU prevents full list rebind when items are added, removed, or reordered.
        items(items = state.items, key = { it.sku }) { item ->
            val sku = item.sku
            // Per-row stable onClick: same SKU + stable component keeps click lambda instance
            // identical across recompositions driven only by other list/header state.
            val onItemClick = remember(sku, component) {
                { component.onIntent(InventoryListIntent.SelectItem(sku)) }
            }
            InventoryCard(
                item = item,
                primary = primary,
                secondary = secondary,
                onClick = onItemClick,
            )
        }
    }
}

@Composable
private fun StatBadge(label: String, containerColor: Color, contentColor: Color) {
    Surface(shape = RoundedCornerShape(50), color = containerColor) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

@Composable
private fun InventoryCard(
    item: InventoryItem,
    primary: Color,
    secondary: Color,
    onClick: () -> Unit,
) {
    val inStockLabel = stringResource(Res.string.in_stock)
    val lowStockLabel = stringResource(Res.string.low_stock)
    val outOfStockLabel = stringResource(Res.string.out_of_stock)
    val (statusLabel, statusColor) =
        item.stockLevel.toUiModel(
            inStockLabel = inStockLabel,
            lowStockLabel = lowStockLabel,
            outOfStockLabel = outOfStockLabel,
            secondaryColor = secondary,
            errorColor = MaterialTheme.colorScheme.error,
        )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = primary.copy(alpha = 0.8f),
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.12f),
                ) {
                    Text(
                        text = statusLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.sku_label, item.sku),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(Res.string.quantity_unit, item.quantity, item.unit),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.stockLevel == StockLevel.OutOfStock) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                )
            }
        }
    }
}
