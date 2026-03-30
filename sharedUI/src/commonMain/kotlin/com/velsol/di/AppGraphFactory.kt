package com.velsol.di

import com.velsol.core.domain.brand.BrandConfig
import dev.zacsweers.metro.createGraph

private object AppGraphHolder {
    val graph: AppGraph by lazy { createGraph<AppGraph>() }
}

fun appBrandConfig(): BrandConfig = AppGraphHolder.graph.brandConfig
