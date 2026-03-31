package com.velsol.feature.login

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface LoginComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class LoginScreenChild(val component: LoginScreenComponent) : Child()
        class SupportChild(val component: SupportComponent) : Child()
    }
}
