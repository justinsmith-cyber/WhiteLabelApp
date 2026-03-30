package com.velsol.core.domain.brand

/** Structural equality for [BrandConfig] implementations (types may differ). */
fun BrandConfig.sameBrandContentAs(other: BrandConfig): Boolean =
    appName == other.appName &&
        tagline == other.tagline &&
        taskLabel == other.taskLabel &&
        apiBaseUrl == other.apiBaseUrl &&
        supportEmail == other.supportEmail &&
        primaryColorArgb == other.primaryColorArgb &&
        onPrimaryColorArgb == other.onPrimaryColorArgb &&
        secondaryColorArgb == other.secondaryColorArgb &&
        onSecondaryColorArgb == other.onSecondaryColorArgb &&
        tertiaryColorArgb == other.tertiaryColorArgb &&
        onTertiaryColorArgb == other.onTertiaryColorArgb &&
        logoUrl == other.logoUrl &&
        features.hasHvacCertifications == other.features.hasHvacCertifications &&
        features.hasPlumbingInventory == other.features.hasPlumbingInventory
