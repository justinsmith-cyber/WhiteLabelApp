package com.velsol.feature.certifications

import androidx.compose.ui.graphics.Color

private val ExpiringCertStatusColor = Color(0xFFF59E0B)

data class CertStatusUiModel(val label: String, val color: Color)

/**
 * Maps a [CertStatus] to its display label and brand-aware color for use in UI.
 * Callers pass [errorColor] from the composable surface so this stays free of MaterialTheme.
 */
fun CertStatus.toUiModel(secondaryColor: Color, errorColor: Color): CertStatusUiModel = when (this) {
    CertStatus.Active -> CertStatusUiModel(label = "Active", color = secondaryColor)
    CertStatus.Expiring -> CertStatusUiModel(label = "Expiring", color = ExpiringCertStatusColor)
    CertStatus.Expired -> CertStatusUiModel(label = "Expired", color = errorColor)
}
