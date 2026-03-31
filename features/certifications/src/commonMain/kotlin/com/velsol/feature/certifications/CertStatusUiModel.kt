package com.velsol.feature.certifications

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class CertStatusUiModel(val label: String, val color: Color)

/** Maps a [CertStatus] to its display label and brand-aware color for use in UI. */
@Composable
fun CertStatus.toUiModel(secondaryColor: Color): CertStatusUiModel = when (this) {
    CertStatus.Active -> CertStatusUiModel(label = "Active", color = secondaryColor)
    CertStatus.Expiring -> CertStatusUiModel(label = "Expiring", color = Color(0xFFF59E0B))
    CertStatus.Expired -> CertStatusUiModel(label = "Expired", color = MaterialTheme.colorScheme.error)
}
