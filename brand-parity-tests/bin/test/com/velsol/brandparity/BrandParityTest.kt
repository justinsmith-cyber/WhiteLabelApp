package com.velsol.brandparity

import com.velsol.clients.acme.AcmeBrandConfig
import com.velsol.clients.beta.BetaBrandConfig
import com.velsol.clients.default.DefaultBrandConfig
import com.velsol.clients.gamma.GammaBrandConfig
import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.sameBrandContentAs
import com.velsol.demo.allDemoConfigs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BrandParityTest {

    @Test
    fun each_client_binding_matches_its_demo_row() {
        val live: List<BrandConfig> = listOf(
            DefaultBrandConfig(),
            AcmeBrandConfig(),
            BetaBrandConfig(),
            GammaBrandConfig(),
        )
        val liveByName = live.associateBy { it.appName }
        assertEquals(4, liveByName.size)
        assertEquals(4, allDemoConfigs.size)
        for (demo in allDemoConfigs) {
            val client = liveByName[demo.appName]
            assertNotNull(client, "No client for demo appName=${demo.appName}")
            assertTrue(
                demo.sameBrandContentAs(client),
                "Demo vs client mismatch for ${demo.appName}",
            )
        }
    }
}
