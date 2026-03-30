package com.velsol.feature.certifications

import io.ktor.client.HttpClient

internal class CertificationsRepository(
    httpClient: HttpClient,
    apiBaseUrl: String,
) {
    private val networkDataSource = CertificationsNetworkDataSource(httpClient, apiBaseUrl)

    suspend fun getCertifications(): List<CertRecord> = try {
        networkDataSource.fetchCertifications().takeIf { it.isNotEmpty() } ?: mockCertifications
    } catch (_: Exception) {
        mockCertifications
    }
}
