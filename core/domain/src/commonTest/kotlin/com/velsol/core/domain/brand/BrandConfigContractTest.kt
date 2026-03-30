package com.velsol.core.domain.brand

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private data class FakeBrandConfig(
    override val appName: String = "App",
    override val tagline: String = "Tag",
    override val taskLabel: String = "Task",
    override val apiBaseUrl: String = "https://api.example.com",
    override val supportEmail: String = "support@example.com",
    override val primaryColorArgb: Long = 0xFF000000L,
    override val onPrimaryColorArgb: Long = 0xFFFFFFFFL,
    override val secondaryColorArgb: Long = 0xFF111111L,
    override val onSecondaryColorArgb: Long = 0xFFFFFFFFL,
    override val tertiaryColorArgb: Long = 0xFF222222L,
    override val onTertiaryColorArgb: Long = 0xFFFFFFFFL,
    override val logoUrl: String = "",
    override val features: FeatureToggles = FeatureToggles(),
) : BrandConfig

private object OtherFakeBrandConfig : BrandConfig {
    override val appName = "App"
    override val tagline = "Tag"
    override val taskLabel = "Task"
    override val apiBaseUrl = "https://api.example.com"
    override val supportEmail = "support@example.com"
    override val primaryColorArgb = 0xFF000000L
    override val onPrimaryColorArgb = 0xFFFFFFFFL
    override val secondaryColorArgb = 0xFF111111L
    override val onSecondaryColorArgb = 0xFFFFFFFFL
    override val tertiaryColorArgb = 0xFF222222L
    override val onTertiaryColorArgb = 0xFFFFFFFFL
    override val logoUrl = ""
    override val features = FeatureToggles()
}

class BrandConfigContractTest {

    private val base = FakeBrandConfig()

    @Test
    fun reflexive() {
        assertTrue(base.sameBrandContentAs(base))
    }

    @Test
    fun symmetric_acrossImplementations() {
        assertTrue(base.sameBrandContentAs(OtherFakeBrandConfig))
        assertTrue(OtherFakeBrandConfig.sameBrandContentAs(base))
    }

    @Test
    fun differs_when_appName_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(appName = "Other")))
    }

    @Test
    fun differs_when_tagline_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(tagline = "Other")))
    }

    @Test
    fun differs_when_taskLabel_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(taskLabel = "Job")))
    }

    @Test
    fun differs_when_apiBaseUrl_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(apiBaseUrl = "https://other.com")))
    }

    @Test
    fun differs_when_supportEmail_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(supportEmail = "other@example.com")))
    }

    @Test
    fun differs_when_primaryColorArgb_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(primaryColorArgb = 0xFFFFFFFFL)))
    }

    @Test
    fun differs_when_onPrimaryColorArgb_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(onPrimaryColorArgb = 0L)))
    }

    @Test
    fun differs_when_secondaryColorArgb_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(secondaryColorArgb = 0xFFFFFFFFL)))
    }

    @Test
    fun differs_when_onSecondaryColorArgb_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(onSecondaryColorArgb = 0L)))
    }

    @Test
    fun differs_when_tertiaryColorArgb_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(tertiaryColorArgb = 0xFFFFFFFFL)))
    }

    @Test
    fun differs_when_onTertiaryColorArgb_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(onTertiaryColorArgb = 0L)))
    }

    @Test
    fun differs_when_logoUrl_changes() {
        assertFalse(base.sameBrandContentAs(base.copy(logoUrl = "https://x")))
    }

    @Test
    fun differs_when_hasHvacCertifications_changes() {
        assertFalse(
            base.sameBrandContentAs(
                base.copy(features = FeatureToggles(hasHvacCertifications = true))
            )
        )
    }

    @Test
    fun differs_when_hasPlumbingInventory_changes() {
        assertFalse(
            base.sameBrandContentAs(
                base.copy(features = FeatureToggles(hasPlumbingInventory = true))
            )
        )
    }
}
