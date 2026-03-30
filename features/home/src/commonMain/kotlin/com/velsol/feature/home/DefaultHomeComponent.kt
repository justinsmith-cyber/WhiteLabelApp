package com.velsol.feature.home

import com.arkivanov.decompose.ComponentContext

class DefaultHomeComponent(
    componentContext: ComponentContext,
) : HomeComponent,
    ComponentContext by componentContext
