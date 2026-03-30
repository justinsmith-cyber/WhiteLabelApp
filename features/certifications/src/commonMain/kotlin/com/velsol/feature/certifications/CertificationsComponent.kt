package com.velsol.feature.certifications

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface CertificationsComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ListChild(val component: CertListComponent) : Child()
        class DetailChild(val component: CertDetailComponent) : Child()
    }
}
