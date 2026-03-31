package com.velsol.feature.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velsol.feature.inventory.generated.resources.Res
import com.velsol.feature.inventory.generated.resources.back_arrow
import com.velsol.feature.inventory.generated.resources.category
import com.velsol.feature.inventory.generated.resources.in_stock
import com.velsol.feature.inventory.generated.resources.item_detail
import com.velsol.feature.inventory.generated.resources.low_stock
import com.velsol.feature.inventory.generated.resources.navigate_back
import com.velsol.feature.inventory.generated.resources.out_of_stock
import com.velsol.feature.inventory.generated.resources.quantity
import com.velsol.feature.inventory.generated.resources.quantity_unit
import com.velsol.feature.inventory.generated.resources.sku
import com.velsol.feature.inventory.generated.resources.stock_level
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.stringResource

@Composable
fun InventoryDetailContent(
    component: InventoryDetailComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()
    val item = state.item ?: return

    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)
    val secondary = Color(brandConfig.secondaryColorArgb)

    val stockUiModel = item.stockLevel.toUiModel(
        inStockLabel = stringResource(Res.string.in_stock),
        lowStockLabel = stringResource(Res.string.low_stock),
        outOfStockLabel = stringResource(Res.string.out_of_stock),
        secondaryColor = secondary,
        errorColor = MaterialTheme.colorScheme.error,
    )
    val statusColor = stockUiModel.color
    val statusLabel = stockUiModel.label

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            val navigateBackDesc = stringResource(Res.string.navigate_back)
            IconButton(
                onClick = { component.onIntent(InventoryDetailIntent.Back) },
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = navigateBackDesc
                },
            ) {
                Text(
                    text = stringResource(Res.string.back_arrow),
                    style = MaterialTheme.typography.titleLarge,
                    color = primary,
                )
            }
            Text(
                text = stringResource(Res.string.item_detail),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = primary),
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    text = item.category.uppercase(),
                    color = onPrimary.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.sp,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = item.name,
                    color = onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.25f),
                ) {
                    Text(
                        text = statusLabel,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DetailRow(label = stringResource(Res.string.sku), value = item.sku)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailRow(label = stringResource(Res.string.category), value = item.category)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailRow(
                    label = stringResource(Res.string.quantity),
                    value = stringResource(Res.string.quantity_unit, item.quantity, item.unit),
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailRow(
                    label = stringResource(Res.string.stock_level),
                    value = statusLabel,
                    valueColor = statusColor,
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
    }
}
