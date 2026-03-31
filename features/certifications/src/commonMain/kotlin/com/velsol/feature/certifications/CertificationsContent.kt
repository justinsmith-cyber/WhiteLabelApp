package com.velsol.feature.certifications

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velsol.feature.certifications.generated.resources.Res
import com.velsol.feature.certifications.generated.resources.active_count_label
import com.velsol.feature.certifications.generated.resources.active_total_summary
import com.velsol.feature.certifications.generated.resources.all_certifications
import com.velsol.feature.certifications.generated.resources.expiring_count_label
import com.velsol.feature.certifications.generated.resources.expires_label
import com.velsol.feature.certifications.generated.resources.hvac_certifications
import com.velsol.feature.certifications.generated.resources.team_credentials
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.stringResource

@Composable
fun CertificationsContent(
    component: CertListComponent,
    modifier: Modifier = Modifier,
) {
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)
    val secondary = Color(brandConfig.secondaryColorArgb)
    val state by component.state.collectAsState()

    Crossfade(targetState = state, modifier = modifier) { currentState ->
        when (currentState) {
            is CertListState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primary)
                }
            }

            is CertListState.Content -> {
                CertificationsListContent(
                    state = currentState,
                    primary = primary,
                    onPrimary = onPrimary,
                    secondary = secondary,
                    onSelectCert = { component.onIntent(CertListIntent.SelectCert(it)) },
                )
            }
        }
    }
}

@Composable
private fun CertificationsListContent(
    state: CertListState.Content,
    primary: Color,
    onPrimary: Color,
    secondary: Color,
    onSelectCert: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = primary),
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(Res.string.hvac_certifications),
                        color = onPrimary.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.team_credentials),
                        color = onPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.active_total_summary, state.activeCount, state.certs.size),
                        color = onPrimary.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatChip(
                            label = stringResource(Res.string.active_count_label, state.activeCount),
                            containerColor = onPrimary.copy(alpha = 0.18f),
                            contentColor = onPrimary,
                        )
                        StatChip(
                            label = stringResource(Res.string.expiring_count_label, state.expiringCount),
                            containerColor = onPrimary.copy(alpha = 0.18f),
                            contentColor = onPrimary,
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(Res.string.all_certifications),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }

        // Stable key by cert name prevents full list rebind when items are added, removed, or reordered.
        items(items = state.certs, key = { it.name }) { cert ->
            CertificationCard(
                cert = cert,
                secondary = secondary,
                onClick = { onSelectCert(cert.name) },
            )
        }
    }
}

@Composable
private fun StatChip(label: String, containerColor: Color, contentColor: Color) {
    Surface(shape = RoundedCornerShape(50), color = containerColor) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

@Composable
private fun CertificationCard(cert: CertRecord, secondary: Color, onClick: () -> Unit) {
    val (statusLabel, statusColor) = cert.status.toUiModel(secondary)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = cert.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = cert.technician,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.12f),
                ) {
                    Text(
                        text = statusLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                text = stringResource(Res.string.expires_label, cert.expires),
                style = MaterialTheme.typography.labelSmall,
                color = if (cert.status == CertStatus.Expired) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}
