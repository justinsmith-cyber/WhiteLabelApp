package com.velsol.feature.certifications

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import com.velsol.feature.certifications.generated.resources.Res
import com.velsol.feature.certifications.generated.resources.back_arrow
import com.velsol.feature.certifications.generated.resources.certification_detail
import com.velsol.feature.certifications.generated.resources.expiry_date
import com.velsol.feature.certifications.generated.resources.hvac_certification
import com.velsol.feature.certifications.generated.resources.navigate_back
import com.velsol.feature.certifications.generated.resources.status
import com.velsol.feature.certifications.generated.resources.technician
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.stringResource

@Composable
fun CertificationDetailContent(
    component: CertDetailComponent,
    modifier: Modifier = Modifier,
) {
    val brandConfig = LocalBrandConfig.current
    val state by component.state.collectAsState()

    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)
    val secondary = Color(brandConfig.secondaryColorArgb)

    Crossfade(targetState = state.cert, modifier = modifier) { cert ->
        if (cert == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primary)
            }
        } else {
            val (statusLabel, statusColor) = cert.status.toUiModel(secondary)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    val navigateBackDesc = stringResource(Res.string.navigate_back)
                    IconButton(
                        onClick = { component.onIntent(CertDetailIntent.Back) },
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
                        text = stringResource(Res.string.certification_detail),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = primary),
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text(
                            text = stringResource(Res.string.hvac_certification),
                            color = onPrimary.copy(alpha = 0.65f),
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 2.sp,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = cert.name,
                            color = onPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = statusColor.copy(alpha = 0.25f),
                        ) {
                            Text(
                                text = statusLabel,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        DetailRow(label = stringResource(Res.string.technician), value = cert.technician)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        DetailRow(label = stringResource(Res.string.expiry_date), value = cert.expires)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        DetailRow(
                            label = stringResource(Res.string.status),
                            value = statusLabel,
                            valueColor = statusColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
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
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
    }
}
