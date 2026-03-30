package com.velsol.demo

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.core.domain.brand.sameBrandContentAs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private object ExpectedDefaultDemo : BrandConfig {
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

private object ExpectedAcmeDemo : BrandConfig {
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

private object ExpectedBetaDemo : BrandConfig {
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

private object ExpectedGammaDemo : BrandConfig {
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

class DemoBrandConfigsTest {

    @Test
    fun demo_list_has_four_distinct_brands() {
        assertEquals(4, allDemoConfigs.size)
        val names = allDemoConfigs.map { it.appName }
        assertEquals(names.distinct().size, names.size)
    }

    @Test
    fun each_demo_row_matches_expected_contract() {
        val expectedByName = mapOf(
            ExpectedDefaultDemo.appName to ExpectedDefaultDemo,
            ExpectedAcmeDemo.appName to ExpectedAcmeDemo,
            ExpectedBetaDemo.appName to ExpectedBetaDemo,
            ExpectedGammaDemo.appName to ExpectedGammaDemo,
        )
        for (config in allDemoConfigs) {
            val expected = expectedByName[config.appName]
            assertTrue(
                expected != null && config.sameBrandContentAs(expected),
                "Unexpected demo row for appName=${config.appName}",
            )
        }
    }
}
