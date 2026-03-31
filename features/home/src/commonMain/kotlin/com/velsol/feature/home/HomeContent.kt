package com.velsol.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.theme.LocalBrandConfig
import com.velsol.feature.home.generated.resources.Res
import com.velsol.feature.home.generated.resources.accent_color
import com.velsol.feature.home.generated.resources.api_label
import com.velsol.feature.home.generated.resources.brand_identity
import com.velsol.feature.home.generated.resources.color_swatch_description
import com.velsol.feature.home.generated.resources.dark_mode
import com.velsol.feature.home.generated.resources.feature_status_description
import com.velsol.feature.home.generated.resources.hvac_certifications
import com.velsol.feature.home.generated.resources.ic_dark_mode
import com.velsol.feature.home.generated.resources.ic_light_mode
import com.velsol.feature.home.generated.resources.light_mode
import com.velsol.feature.home.generated.resources.open_github
import com.velsol.feature.home.generated.resources.platform_features
import com.velsol.feature.home.generated.resources.plumbing_inventory
import com.velsol.feature.home.generated.resources.primary_color
import com.velsol.feature.home.generated.resources.status_active
import com.velsol.feature.home.generated.resources.status_inactive
import com.velsol.feature.home.generated.resources.support_label
import com.velsol.feature.home.generated.resources.theme
import com.velsol.feature.home.generated.resources.whitelabel_platform
import com.velsol.feature.home.generated.resources.work_item
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeContent(
    isDark: Boolean,
    onToggleDarkMode: () -> Unit,
    onOpenGithub: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)
    val secondary = Color(brandConfig.secondaryColorArgb)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            HeroBrandCard(
                appName = brandConfig.appName,
                tagline = brandConfig.tagline,
                taskLabel = brandConfig.taskLabel,
                apiBaseUrl = brandConfig.apiBaseUrl,
                logoUrl = brandConfig.logoUrl,
                primary = primary,
                onPrimary = onPrimary,
                secondary = secondary,
            )
        }
        item {
            FeaturesSection(
                features = brandConfig.features,
                primary = primary,
            )
        }
        item {
            BrandIdentityCard(
                supportEmail = brandConfig.supportEmail,
                apiBaseUrl = brandConfig.apiBaseUrl,
                primary = primary,
                secondary = secondary,
            )
        }
        item {
            ActionsRow(
                isDark = isDark,
                onToggleDarkMode = onToggleDarkMode,
                onOpenGithub = onOpenGithub,
            )
        }
    }
}

@Composable
private fun HeroBrandCard(
    appName: String,
    tagline: String,
    taskLabel: String,
    apiBaseUrl: String,
    logoUrl: String,
    primary: Color,
    onPrimary: Color,
    secondary: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = primary),
    ) {
        Column(Modifier.padding(28.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(secondary),
                    )
                    Text(
                        text = stringResource(Res.string.whitelabel_platform),
                        color = onPrimary.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                    )
                }
                if (logoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = logoUrl,
                        contentDescription = appName,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = appName,
                color = onPrimary,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                lineHeight = 42.sp,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = tagline,
                color = onPrimary.copy(alpha = 0.82f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.work_item, taskLabel),
                color = onPrimary.copy(alpha = 0.72f),
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(22.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = onPrimary.copy(alpha = 0.15f),
            ) {
                Text(
                    text = apiBaseUrl.removePrefix("https://"),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = onPrimary.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun FeaturesSection(features: FeatureToggles, primary: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(Res.string.platform_features),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FeatureTile(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.hvac_certifications),
                isEnabled = features.hasHvacCertifications,
                activeColor = primary,
            )
            FeatureTile(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.plumbing_inventory),
                isEnabled = features.hasPlumbingInventory,
                activeColor = primary,
            )
        }
    }
}

@Composable
private fun FeatureTile(
    label: String,
    isEnabled: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isEnabled) {
        activeColor.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isEnabled) {
        activeColor
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    }

    val statusText = if (isEnabled) {
        stringResource(Res.string.status_active)
    } else {
        stringResource(Res.string.status_inactive)
    }
    val featureStatusDesc = stringResource(Res.string.feature_status_description, label, statusText)
    val indicator = if (isEnabled) "●" else "○"

    Card(
        modifier = modifier.clearAndSetSemantics {
            contentDescription = featureStatusDesc
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = indicator,
                color = contentColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isEnabled) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isEnabled) contentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.75f),
                letterSpacing = 0.5.sp,
            )
        }
    }
}

@Composable
private fun BrandIdentityCard(
    supportEmail: String,
    apiBaseUrl: String,
    primary: Color,
    secondary: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.brand_identity),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ColorSwatch(label = stringResource(Res.string.primary_color), color = primary)
                ColorSwatch(label = stringResource(Res.string.accent_color), color = secondary)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            BrandInfoRow(label = stringResource(Res.string.support_label), value = supportEmail)
            BrandInfoRow(label = stringResource(Res.string.api_label), value = apiBaseUrl)
        }
    }
}

@Composable
private fun ColorSwatch(label: String, color: Color) {
    val hex = remember(color) {
        val r = (color.red * 255).toInt().coerceIn(0, 255)
        val g = (color.green * 255).toInt().coerceIn(0, 255)
        val b = (color.blue * 255).toInt().coerceIn(0, 255)
        val packed = (r shl 16) or (g shl 8) or b
        "#" + packed.toString(16).uppercase().padStart(6, '0')
    }
    val swatchDesc = stringResource(Res.string.color_swatch_description, label, hex)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.clearAndSetSemantics {
            contentDescription = swatchDesc
        },
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color),
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = hex,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun BrandInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
        )
    }
}

@Composable
private fun ActionsRow(
    isDark: Boolean,
    onToggleDarkMode: () -> Unit,
    onOpenGithub: () -> Unit,
) {
    val themeIcon = if (isDark) Res.drawable.ic_light_mode else Res.drawable.ic_dark_mode
    val themeState = if (isDark) stringResource(Res.string.dark_mode) else stringResource(Res.string.light_mode)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ElevatedButton(
            modifier = Modifier
                .weight(1f)
                .semantics { stateDescription = themeState },
            onClick = onToggleDarkMode,
        ) {
            Crossfade(targetState = themeIcon) { icon ->
                Icon(vectorResource(icon), contentDescription = null)
            }
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(Res.string.theme))
        }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onOpenGithub,
        ) {
            Text(stringResource(Res.string.open_github))
        }
    }
}
