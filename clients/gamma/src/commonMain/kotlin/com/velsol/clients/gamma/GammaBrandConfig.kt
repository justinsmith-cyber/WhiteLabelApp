package com.velsol.clients.gamma

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.core.domain.di.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class GammaBrandConfig : BrandConfig {
    override val appName = "Gamma Field Tech"
    override val tagline = "Complete field operations platform"
    override val taskLabel = "Delivery"
    override val apiBaseUrl = "https://api.gamma-fieldtech.com"
    override val supportEmail = "support@gamma-fieldtech.com"
    override val primaryColorArgb = 0xFF2E7D32L
    override val onPrimaryColorArgb = 0xFFFFFFFFL
    override val secondaryColorArgb = 0xFFF9A825L
    override val onSecondaryColorArgb = 0xFF1A1A1AL
    override val tertiaryColorArgb = 0xFF00695CL
    override val onTertiaryColorArgb = 0xFFFFFFFFL
    override val logoUrl = "https://placehold.co/48x48/2E7D32/FFFFFF.png?text=GF"
    override val features = FeatureToggles(hasHvacCertifications = true, hasPlumbingInventory = true)
}
