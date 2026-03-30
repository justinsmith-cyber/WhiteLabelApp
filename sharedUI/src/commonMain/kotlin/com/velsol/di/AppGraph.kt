package com.velsol.di

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.di.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface AppGraph {
    val brandConfig: BrandConfig
}
