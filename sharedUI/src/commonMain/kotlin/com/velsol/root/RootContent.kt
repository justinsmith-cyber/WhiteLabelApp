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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.velsol.feature.certifications.CertificationsContent
import com.velsol.feature.home.HomeContent
import com.velsol.feature.inventory.InventoryContent
import com.velsol.generated.resources.Res
import com.velsol.generated.resources.ic_tab_certifications
import com.velsol.generated.resources.ic_tab_home
import com.velsol.generated.resources.ic_tab_inventory
import com.velsol.theme.LocalBrandConfig
import com.velsol.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val WideLayoutBreakpoint = 900.dp

/** Maps a tab to its label and icon resource — purely a UI asset lookup, not a visibility rule. */
private fun tabAssets(tab: RootComponent.Tab): Pair<String, DrawableResource> = when (tab) {
    RootComponent.Tab.Home -> "Home" to Res.drawable.ic_tab_home
    RootComponent.Tab.Certifications -> "Certifications" to Res.drawable.ic_tab_certifications
    RootComponent.Tab.Inventory -> "Inventory" to Res.drawable.ic_tab_inventory
}

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val themeState = LocalThemeIsDark.current
    val isDark by themeState
    val uriHandler = LocalUriHandler.current
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    // Keep a stable callback reference so nav items can skip unnecessary recomposition work.
    val onSelectTab = remember(component) {
        { tab: RootComponent.Tab -> component.onIntent(RootIntent.SelectTab(tab)) }
    }
    // Keep root-level callbacks stable so active tab content can be skipped during layout-only recompositions.
    val onToggleDarkMode = remember(themeState) { { themeState.value = !themeState.value } }
    val onOpenGithub = remember(uriHandler) { { uriHandler.openUri("https://github.com/terrakok") } }
    val state by component.state.collectAsState()
    val selectedTab = state.selectedTab

    val visibleTabs = component.visibleTabs
    val showNavigation = visibleTabs.size > 1

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val useSideNavigation = showNavigation && maxWidth >= WideLayoutBreakpoint
        Row(Modifier.fillMaxSize()) {
            if (useSideNavigation) {
                AppNavigationRail(
                    selectedTab = selectedTab,
                    onSelectTab = onSelectTab,
                    visibleTabs = visibleTabs,
                    primary = primary,
                    modifier = Modifier.fillMaxHeight(),
                )
            }
            Scaffold(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentWindowInsets = WindowInsets(0),
                bottomBar = {
                    if (showNavigation && !useSideNavigation) {
                        AppNavigationBar(
                            selectedTab = selectedTab,
                            onSelectTab = onSelectTab,
                            visibleTabs = visibleTabs,
                            primary = primary,
                        )
                    }
                },
            ) { innerPadding ->
                RootTabScenes(
                    selectedTab = selectedTab,
                    component = component,
                    isDark = isDark,
                    onToggleDarkMode = onToggleDarkMode,
                    onOpenGithub = onOpenGithub,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }

}

@Composable
private fun AppNavigationBar(
    selectedTab: RootComponent.Tab,
    onSelectTab: (RootComponent.Tab) -> Unit,
    visibleTabs: List<RootComponent.Tab>,
    primary: Color,
) {
    val colors = NavigationBarItemDefaults.colors(
        indicatorColor = primary.copy(alpha = 0.12f),
        selectedIconColor = primary,
        selectedTextColor = primary,
    )
    NavigationBar {
        for (tab in visibleTabs) {
            val (label, icon) = tabAssets(tab)
            val onTabClick = remember(tab, onSelectTab) { { onSelectTab(tab) } }
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = onTabClick,
                icon = {
                    TabIcon(
                        iconRes = icon,
                        isSelected = selectedTab == tab,
                        color = primary,
                    )
                },
                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                colors = colors,
            )
        }
    }
}

@Composable
private fun AppNavigationRail(
    selectedTab: RootComponent.Tab,
    onSelectTab: (RootComponent.Tab) -> Unit,
    visibleTabs: List<RootComponent.Tab>,
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
        for (tab in visibleTabs) {
            val (label, icon) = tabAssets(tab)
            val onTabClick = remember(tab, onSelectTab) { { onSelectTab(tab) } }
            NavigationRailItem(
                selected = selectedTab == tab,
                onClick = onTabClick,
                icon = {
                    TabIcon(
                        iconRes = icon,
                        isSelected = selectedTab == tab,
                        color = primary,
                    )
                },
                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                colors = colors,
            )
        }
    }
}

@Composable
private fun RootTabScenes(
    selectedTab: RootComponent.Tab,
    component: RootComponent,
    isDark: Boolean,
    onToggleDarkMode: () -> Unit,
    onOpenGithub: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (selectedTab) {
        RootComponent.Tab.Home -> HomeContent(
            component = component.home,
            isDark = isDark,
            onToggleDarkMode = onToggleDarkMode,
            onOpenGithub = onOpenGithub,
            modifier = modifier,
        )

        RootComponent.Tab.Certifications -> CertificationsContent(
            component = component.certifications,
            modifier = modifier,
        )

        RootComponent.Tab.Inventory -> InventoryContent(
            component = component.inventory,
            modifier = modifier,
        )
    }
}

@Composable
private fun TabIcon(iconRes: DrawableResource, isSelected: Boolean, color: Color) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null, // NavigationBarItem/NavigationRailItem already provides semantics via label
        modifier = Modifier.size(24.dp),
        tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
