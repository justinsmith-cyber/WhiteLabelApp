package com.velsol.clients.beta

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.core.domain.di.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class BetaBrandConfig : BrandConfig {
    override val appName = "Beta Plumbing Co"
    override val tagline = "Precision Plumbing Solutions"
    override val taskLabel = "Plumbing job"
    override val apiBaseUrl = "https://api.beta-plumbing.com"
    override val supportEmail = "hello@beta-plumbing.com"
    override val primaryColorArgb = 0xFF1565C0L
    override val onPrimaryColorArgb = 0xFFFFFFFFL
    override val secondaryColorArgb = 0xFF00ACC1L
    override val onSecondaryColorArgb = 0xFFFFFFFFL
    override val tertiaryColorArgb = 0xFF6A1B9AL
    override val onTertiaryColorArgb = 0xFFFFFFFFL
    override val logoUrl = "https://placehold.co/48x48/1565C0/FFFFFF.png?text=BP"
    override val features = FeatureToggles(hasPlumbingInventory = true)
}
