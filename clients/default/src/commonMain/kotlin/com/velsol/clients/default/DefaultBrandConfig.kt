package com.velsol.clients.default

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.core.domain.di.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class DefaultBrandConfig : BrandConfig {
    override val appName = "WhiteLabel App"
    override val tagline = "Your workflow, your way"
    override val taskLabel = "Task"
    override val apiBaseUrl = "https://api.example.com"
    override val supportEmail = "support@example.com"
    override val primaryColorArgb = 0xFF6750A4L
    override val onPrimaryColorArgb = 0xFFFFFFFFL
    override val secondaryColorArgb = 0xFF00897BL
    override val onSecondaryColorArgb = 0xFFFFFFFFL
    override val tertiaryColorArgb = 0xFF7D5260L
    override val onTertiaryColorArgb = 0xFFFFFFFFL
    override val logoUrl = ""
    override val features = FeatureToggles()
}
