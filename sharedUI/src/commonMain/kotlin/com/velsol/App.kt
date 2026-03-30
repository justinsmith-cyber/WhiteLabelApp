package com.velsol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velsol.core.domain.brand.BrandConfig
import com.velsol.root.RootComponent
import com.velsol.root.RootContent
import com.velsol.theme.AppTheme

@Composable
fun App(
    rootComponent: RootComponent,
    brandConfig: BrandConfig,
    onBrandSelected: (BrandConfig) -> Unit = {},
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(brandConfig = brandConfig, onThemeChanged = onThemeChanged) {
    RootContent(
        component = rootComponent,
        onBrandSelected = onBrandSelected,
        modifier = Modifier.fillMaxSize()
    )
}
