package com.velsol.feature.home

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultHomeComponentTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeComponent(onShowSwitcher: () -> Unit): DefaultHomeComponent {
        val lifecycle = LifecycleRegistry()
        return DefaultHomeComponent(
            componentContext = DefaultComponentContext(lifecycle),
            onShowDemoSwitcher = onShowSwitcher,
        )
    }

    @Test
    fun three_rapid_taps_trigger_demo_switcher() = runTest(testDispatcher) {
        var callCount = 0
        val component = makeComponent { callCount++ }

        component.onLogoTapped()
        component.onLogoTapped()
        component.onLogoTapped()

        assertEquals(1, callCount)
    }

    @Test
    fun fewer_than_three_taps_do_not_trigger_demo_switcher() = runTest(testDispatcher) {
        var callCount = 0
        val component = makeComponent { callCount++ }

        component.onLogoTapped()
        component.onLogoTapped()

        assertEquals(0, callCount)
    }

    @Test
    fun tap_count_resets_after_timeout_preventing_late_trigger() = runTest(testDispatcher) {
        var callCount = 0
        val component = makeComponent { callCount++ }

        component.onLogoTapped() // tapCount = 1
        component.onLogoTapped() // tapCount = 2; schedules reset after 600ms
        advanceTimeBy(700L)      // reset fires → tapCount = 0
        component.onLogoTapped() // tapCount = 1 (fresh start — only 1 of 3 needed)

        assertEquals(0, callCount)
    }

    @Test
    fun tap_count_resets_after_each_trigger_allowing_subsequent_trigger() = runTest(testDispatcher) {
        var callCount = 0
        val component = makeComponent { callCount++ }

        repeat(3) { component.onLogoTapped() } // triggers; tapCount resets to 0
        assertEquals(1, callCount)

        repeat(3) { component.onLogoTapped() } // triggers again
        assertEquals(2, callCount)
    }
}
