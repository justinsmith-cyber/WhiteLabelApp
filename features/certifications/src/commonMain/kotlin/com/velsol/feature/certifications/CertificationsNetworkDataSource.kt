package com.velsol.feature.certifications

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Serializable
internal data class CertRecordDto(
    val name: String,
    val technician: String,
    val expires: String,
    val status: String,
)

internal class CertificationsNetworkDataSource(
    private val httpClient: HttpClient,
    private val apiBaseUrl: String,
) {
    suspend fun fetchCertifications(): List<CertRecord> {
        val dtos = httpClient.get("$apiBaseUrl/certifications").body<List<CertRecordDto>>()
        return dtos.map { dto ->
            CertRecord(
                name = dto.name,
                technician = dto.technician,
                expires = dto.expires,
                status = when (dto.status.lowercase()) {
                    "expiring" -> CertStatus.Expiring
                    "expired" -> CertStatus.Expired
                    else -> CertStatus.Active
                },
            )
        }
    }
}
