package com.velsol.root

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.brand.FeatureToggles
import com.velsol.demo.DemoClientSwitcher
import com.velsol.feature.certifications.CertificationDetailContent
import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.certifications.CertificationsContent
import com.velsol.feature.home.HomeContent
import com.velsol.feature.inventory.InventoryComponent
import com.velsol.feature.inventory.InventoryContent
import com.velsol.feature.inventory.InventoryDetailContent
import com.velsol.generated.resources.Res
import com.velsol.generated.resources.ic_tab_certifications
import com.velsol.generated.resources.ic_tab_home
import com.velsol.generated.resources.ic_tab_inventory
import com.velsol.theme.LocalBrandConfig
import com.velsol.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private enum class Tab { Home, Certifications, Inventory }

// Single source of tab definitions for bottom bar and navigation rail so feature flags and assets stay aligned.
private data class TabDestination(
    val tab: Tab,
    val label: String,
    val icon: DrawableResource,
    val isVisible: (FeatureToggles) -> Boolean,
)

private val rootTabDestinations =
    listOf(
        TabDestination(Tab.Home, "Home", Res.drawable.ic_tab_home) { true },
        TabDestination(Tab.Certifications, "Certifications", Res.drawable.ic_tab_certifications) {
            it.hasHvacCertifications
        },
        TabDestination(Tab.Inventory, "Inventory", Res.drawable.ic_tab_inventory) {
            it.hasPlumbingInventory
        },
    )

private fun FeatureToggles.visibleRootTabs(): List<TabDestination> = rootTabDestinations.filter { it.isVisible(this) }

private val WideLayoutBreakpoint = 900.dp

@Composable
fun RootContent(
    component: RootComponent,
    onBrandSelect: (BrandConfig) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val stack by component.stack.subscribeAsState()
    var isDark by LocalThemeIsDark.current
    val uriHandler = LocalUriHandler.current
    val brandConfig = LocalBrandConfig.current
    val features = brandConfig.features
    val primary = Color(brandConfig.primaryColorArgb)

    var selectedTab by remember { mutableStateOf(Tab.Home) }
    var showSwitcher by remember { mutableStateOf(false) }

    val hasAnyFeature = features.hasHvacCertifications || features.hasPlumbingInventory

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val useSideNavigation = hasAnyFeature && maxWidth >= WideLayoutBreakpoint
        Row(Modifier.fillMaxSize()) {
            if (useSideNavigation) {
                AppNavigationRail(
                    selectedTab = selectedTab,
                    onSelectTab = { selectedTab = it },
                    features = features,
                    primary = primary,
                    modifier = Modifier.fillMaxHeight(),
                )
            }
            Scaffold(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentWindowInsets = WindowInsets(0),
                bottomBar = {
                    if (hasAnyFeature && !useSideNavigation) {
                        AppNavigationBar(
                            selectedTab = selectedTab,
                            onSelectTab = { selectedTab = it },
                            features = features,
                            primary = primary,
                        )
                    }
                },
            ) { innerPadding ->
                RootTabScenes(
                    selectedTab = selectedTab,
                    component = component,
                    stack = stack,
                    isDark = isDark,
                    onToggleDarkMode = { isDark = !isDark },
                    onOpenGithub = { uriHandler.openUri("https://github.com/terrakok") },
                    onShowDemoSwitcher = { showSwitcher = true },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }

    if (showSwitcher) {
        DemoClientSwitcher(
            onDismiss = { showSwitcher = false },
            onBrandSelect = { config ->
                onBrandSelect(config)
                selectedTab = Tab.Home
            },
        )
    }
}

@Composable
private fun AppNavigationBar(
    selectedTab: Tab,
    onSelectTab: (Tab) -> Unit,
    features: FeatureToggles,
    primary: Color,
) {
    val colors = NavigationBarItemDefaults.colors(
        indicatorColor = primary.copy(alpha = 0.12f),
        selectedIconColor = primary,
        selectedTextColor = primary,
    )
    NavigationBar {
        for (dest in features.visibleRootTabs()) {
            NavigationBarItem(
                selected = selectedTab == dest.tab,
                onClick = { onSelectTab(dest.tab) },
                icon = {
                    TabIcon(
                        iconRes = dest.icon,
                        label = dest.label,
                        isSelected = selectedTab == dest.tab,
                        color = primary,
                    )
                },
                label = { Text(dest.label, style = MaterialTheme.typography.labelSmall) },
                colors = colors,
            )
        }
    }
}

@Composable
private fun AppNavigationRail(
    selectedTab: Tab,
    onSelectTab: (Tab) -> Unit,
    features: FeatureToggles,
    primary: Color,
    modifier: Modifier = Modifier,
) {
    val colors = NavigationRailItemDefaults.colors(
        selectedIconColor = primary,
        selectedTextColor = primary,
        indicatorColor = primary.copy(alpha = 0.12f),
    )
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        for (dest in features.visibleRootTabs()) {
            NavigationRailItem(
                selected = selectedTab == dest.tab,
                onClick = { onSelectTab(dest.tab) },
                icon = {
                    TabIcon(
                        iconRes = dest.icon,
                        label = dest.label,
                        isSelected = selectedTab == dest.tab,
                        color = primary,
                    )
                },
                label = { Text(dest.label, style = MaterialTheme.typography.labelSmall) },
                colors = colors,
            )
        }
    }
}

@Composable
private fun RootTabScenes(
    selectedTab: Tab,
    component: RootComponent,
    stack: ChildStack<*, RootComponent.Child>,
    isDark: Boolean,
    onToggleDarkMode: () -> Unit,
    onOpenGithub: () -> Unit,
    onShowDemoSwitcher: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (selectedTab) {
        Tab.Home -> Children(
            stack = stack,
            modifier = modifier,
            animation = stackAnimation(fade()),
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.HomeChild -> HomeContent(
                    component = instance.component,
                    isDark = isDark,
                    onToggleDarkMode = onToggleDarkMode,
                    onOpenGithub = onOpenGithub,
                    onShowDemoSwitcher = onShowDemoSwitcher,
                )
            }
        }

        Tab.Certifications -> {
            val certsStack by component.certifications.stack.subscribeAsState()
            Children(
                stack = certsStack,
                modifier = modifier,
                animation = stackAnimation(fade()),
            ) { child ->
                when (val instance = child.instance) {
                    is CertificationsComponent.Child.ListChild -> CertificationsContent(
                        component = instance.component,
                    )

                    is CertificationsComponent.Child.DetailChild -> CertificationDetailContent(
                        component = instance.component,
                    )
                }
            }
        }

        Tab.Inventory -> {
            val inventoryStack by component.inventory.stack.subscribeAsState()
            Children(
                stack = inventoryStack,
                modifier = modifier,
                animation = stackAnimation(fade()),
            ) { child ->
                when (val instance = child.instance) {
                    is InventoryComponent.Child.ListChild -> InventoryContent(
                        component = instance.component,
                    )

                    is InventoryComponent.Child.DetailChild -> InventoryDetailContent(
                        component = instance.component,
                    )
                }
            }
        }
    }
}

@Composable
private fun TabIcon(iconRes: DrawableResource, label: String, isSelected: Boolean, color: Color) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = label,
        modifier = Modifier.size(24.dp),
        tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
