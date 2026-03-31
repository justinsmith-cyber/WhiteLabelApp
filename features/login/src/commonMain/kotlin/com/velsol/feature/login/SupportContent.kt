package com.velsol.feature.login

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velsol.feature.login.generated.resources.Res
import com.velsol.feature.login.generated.resources.back_arrow
import com.velsol.feature.login.generated.resources.call_support
import com.velsol.feature.login.generated.resources.contact_support_title
import com.velsol.feature.login.generated.resources.loading
import com.velsol.feature.login.generated.resources.mail
import com.velsol.feature.login.generated.resources.navigate_back
import com.velsol.feature.login.generated.resources.ref_number
import com.velsol.feature.login.generated.resources.your_pin
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SupportContent(
    component: SupportComponent,
    modifier: Modifier = Modifier,
) {
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    val state by component.state.collectAsState()

    Crossfade(targetState = state, modifier = modifier.fillMaxSize()) { currentState ->
        when (currentState) {
            SupportState.Loading -> {
                val loadingDesc = stringResource(Res.string.loading)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = primary,
                        modifier = Modifier.clearAndSetSemantics {
                            contentDescription = loadingDesc
                        },
                    )
                }
            }

            is SupportState.Content -> {
                SupportPageContent(
                    data = currentState.data,
                    primary = primary,
                    onBack = component::onBack,
                )
            }
        }
    }
}

@Composable
private fun SupportPageContent(
    data: SupportData,
    primary: Color,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        SupportHeader(primary = primary, onBack = onBack)
        SupportDetailsCard(data = data, primary = primary)
    }
}

@Composable
private fun SupportHeader(primary: Color, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val navigateBackDesc = stringResource(Res.string.navigate_back)
        IconButton(
            onClick = onBack,
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = navigateBackDesc
            },
        ) {
            Text(
                text = stringResource(Res.string.back_arrow),
                style = MaterialTheme.typography.titleLarge,
                color = primary,
            )
        }
        Text(
            text = stringResource(Res.string.contact_support_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun SupportDetailsCard(data: SupportData, primary: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SupportRow(
                label = stringResource(Res.string.call_support),
                value = data.phone,
                valueColor = primary,
                valueFontWeight = FontWeight.SemiBold,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SupportRow(
                label = stringResource(Res.string.your_pin),
                value = data.pin,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SupportRow(
                label = stringResource(Res.string.mail),
                value = data.email,
                valueColor = primary,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SupportRow(
                label = stringResource(Res.string.ref_number),
                value = data.refNumber,
            )
        }
    }
}

@Composable
private fun SupportRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    valueFontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = valueFontWeight,
            color = valueColor,
        )
    }
}
