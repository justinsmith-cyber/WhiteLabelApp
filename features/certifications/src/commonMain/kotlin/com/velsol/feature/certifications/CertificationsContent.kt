package com.velsol.feature.certifications

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
import com.velsol.core.domain.brand.BrandConfig

@Composable
fun CertificationsContent(
    component: CertListComponent,
    brandConfig: BrandConfig,
    modifier: Modifier = Modifier,
) {
    val primary = Color(brandConfig.primaryColorArgb)
    val onPrimary = Color(brandConfig.onPrimaryColorArgb)
    val secondary = Color(brandConfig.secondaryColorArgb)
    val state by component.state.collectAsState()

    if (state.isLoading && state.certs === mockCertifications) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = primary)
        }
        return
    }

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
                        text = "HVAC CERTIFICATIONS",
                        color = onPrimary.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Team Credentials",
                        color = onPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${state.activeCount} active · ${state.certs.size} total",
                        color = onPrimary.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatChip(
                            label = "${state.activeCount} Active",
                            containerColor = onPrimary.copy(alpha = 0.18f),
                            contentColor = onPrimary,
                        )
                        StatChip(
                            label = "${state.expiringCount} Expiring",
                            containerColor = onPrimary.copy(alpha = 0.18f),
                            contentColor = onPrimary,
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "All Certifications",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }

        items(state.certs) { cert ->
            CertificationCard(
                cert = cert,
                secondary = secondary,
                onClick = { component.onCertSelected(cert.name) },
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
    val (statusColor, statusLabel) = when (cert.status) {
        CertStatus.Active -> secondary to "Active"
        CertStatus.Expiring -> Color(0xFFF59E0B) to "Expiring"
        CertStatus.Expired -> MaterialTheme.colorScheme.error to "Expired"
    }

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
                text = "Expires ${cert.expires}",
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
