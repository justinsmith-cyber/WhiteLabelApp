package com.velsol.core.domain.brand

data class FeatureToggles(
    val hasHvacCertifications: Boolean = false,
    val hasPlumbingInventory: Boolean = false,
)

interface BrandConfig {
    val appName: String
    val tagline: String

    /** Label for a unit of field work (e.g. "Task", "Job", "Delivery") — brand-specific terminology. */
    val taskLabel: String
    val apiBaseUrl: String
    val supportEmail: String
    val supportPhone: String
    val primaryColorArgb: Long
    val onPrimaryColorArgb: Long
    val secondaryColorArgb: Long
    val onSecondaryColorArgb: Long
    val tertiaryColorArgb: Long
    val onTertiaryColorArgb: Long
    val logoUrl: String
    val features: FeatureToggles
}
