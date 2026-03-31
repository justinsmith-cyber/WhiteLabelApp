package com.velsol.feature.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.velsol.feature.login.generated.resources.Res
import com.velsol.feature.login.generated.resources.app_logo_description
import com.velsol.feature.login.generated.resources.contact_support
import com.velsol.feature.login.generated.resources.remember_name
import com.velsol.feature.login.generated.resources.sign_in_error
import com.velsol.feature.login.generated.resources.sign_in_with_sso
import com.velsol.feature.login.generated.resources.signing_in
import com.velsol.feature.login.generated.resources.version_label
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.stringResource

private const val APP_VERSION = "1.0.0"

@Composable
fun LoginContent(
    component: LoginComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.stack.subscribeAsState()
    Children(
        stack = stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            is LoginComponent.Child.LoginScreenChild -> LoginScreenContent(component = instance.component)
            is LoginComponent.Child.SupportChild -> SupportContent(component = instance.component)
        }
    }
}

@Composable
internal fun LoginScreenContent(
    component: LoginScreenComponent,
    modifier: Modifier = Modifier,
) {
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)

    val loginState by component.loginState.collectAsState()
    val rememberName by component.rememberName.collectAsState()

    val isLoading = loginState is LoginState.Loading
    val error = (loginState as? LoginState.Error)?.message

    val onSignIn = remember(component) { { component.onIntent(LoginIntent.SignIn) } }
    val onToggleRemember = remember(component) {
        { enabled: Boolean -> component.onIntent(LoginIntent.SetRememberName(enabled)) }
    }
    val onContactSupport = remember(component) { { component.onIntent(LoginIntent.NavigateToSupport) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        HeroSection(
            appName = brandConfig.appName,
            tagline = brandConfig.tagline,
            logoUrl = brandConfig.logoUrl,
            primary = primary,
            onPrimary = onPrimary,
        )

        ActionsSection(
            isLoading = isLoading,
            error = error,
            rememberName = rememberName,
            primary = primary,
            onPrimary = onPrimary,
            onSignIn = onSignIn,
            onToggleRemember = onToggleRemember,
        )

        BottomSection(
            primary = primary,
            onContactSupport = onContactSupport,
        )
    }
}

@Composable
private fun HeroSection(
    appName: String,
    tagline: String,
    logoUrl: String,
    primary: Color,
    onPrimary: Color,
) {
    val logoDesc = stringResource(Res.string.app_logo_description, appName)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 56.dp),
    ) {
        if (logoUrl.isNotEmpty()) {
            AsyncImage(
                model = logoUrl,
                contentDescription = logoDesc,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp)),
            )
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = appName.take(2).uppercase(),
                    color = onPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = appName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = tagline,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ActionsSection(
    isLoading: Boolean,
    error: String?,
    rememberName: Boolean,
    primary: Color,
    onPrimary: Color,
    onSignIn: () -> Unit,
    onToggleRemember: (Boolean) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = onSignIn,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
                contentColor = onPrimary,
            ),
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
            ) { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        color = onPrimary,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp,
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.sign_in_with_sso),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    )
                }
            }
        }

        RememberNameToggle(
            checked = rememberName,
            primary = primary,
            onCheckedChange = onToggleRemember,
        )

        Crossfade(targetState = error) { msg ->
            if (msg != null) {
                Text(
                    text = stringResource(Res.string.sign_in_error),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun RememberNameToggle(
    checked: Boolean,
    primary: Color,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.remember_name),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = primary,
                checkedTrackColor = primary.copy(alpha = 0.3f),
            ),
        )
    }
}

@Composable
private fun BottomSection(
    primary: Color,
    onContactSupport: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(bottom = 24.dp),
    ) {
        TextButton(onClick = onContactSupport) {
            Text(
                text = stringResource(Res.string.contact_support),
                style = MaterialTheme.typography.labelLarge,
                color = primary,
            )
        }
        Text(
            text = stringResource(Res.string.version_label, APP_VERSION),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )
    }
}
