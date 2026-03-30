package com.velsol.clients.acme

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.core.domain.di.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class AcmeBrandConfig : BrandConfig {
    override val appName = "Acme Field Services"
    override val tagline = "Certified HVAC Excellence"
    override val taskLabel = "Job"
    override val apiBaseUrl = "https://api.acme.com"
    override val supportEmail = "support@acme.com"
    override val primaryColorArgb = 0xFFE53935L
    override val onPrimaryColorArgb = 0xFFFFFFFFL
    override val secondaryColorArgb = 0xFFF57F17L
    override val onSecondaryColorArgb = 0xFFFFFFFFL
    override val tertiaryColorArgb = 0xFF1565C0L
    override val onTertiaryColorArgb = 0xFFFFFFFFL
    override val logoUrl = "https://placehold.co/48x48/E53935/FFFFFF.png?text=AC"
    override val features = FeatureToggles(hasHvacCertifications = true)
}
