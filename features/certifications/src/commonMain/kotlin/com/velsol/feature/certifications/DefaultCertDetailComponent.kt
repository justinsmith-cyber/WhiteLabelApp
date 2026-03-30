package com.velsol.feature.certifications

import com.arkivanov.decompose.ComponentContext

class DefaultCertDetailComponent(
    componentContext: ComponentContext,
    override val certName: String,
    private val onBackCallback: () -> Unit,
) : CertDetailComponent, ComponentContext by componentContext {

    override fun onBack() {
        onBackCallback()
    }
}
