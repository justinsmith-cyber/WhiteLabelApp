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
    modifier: Modifier = Modifier,
    onBrandSelect: (BrandConfig) -> Unit = {},
    onThemeChange: @Composable (isDark: Boolean) -> Unit = {},
) = AppTheme(brandConfig = brandConfig, onThemeChange = onThemeChange) {
    RootContent(
        component = rootComponent,
        onBrandSelect = onBrandSelect,
        modifier = modifier.fillMaxSize(),
    )
}
