package com.velsol.core.domain.brand

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeatureTogglesTest {

    @Test
    fun defaults_are_false() {
        val toggles = FeatureToggles()
        assertFalse(toggles.hasHvacCertifications)
        assertFalse(toggles.hasPlumbingInventory)
    }

    @Test
    fun copies_respect_flags() {
        val hvac = FeatureToggles(hasHvacCertifications = true)
        assertTrue(hvac.hasHvacCertifications)
        assertFalse(hvac.hasPlumbingInventory)

        val plumbing = FeatureToggles(hasPlumbingInventory = true)
        assertFalse(plumbing.hasHvacCertifications)
        assertTrue(plumbing.hasPlumbingInventory)
    }
}
