package com.velsol.feature.certifications

import io.ktor.client.HttpClient

interface CertificationsRepository {
    suspend fun getCertifications(): List<CertRecord>

    suspend fun getCertification(name: String): CertRecord?
}

fun createCertificationsRepository(httpClient: HttpClient, apiBaseUrl: String): CertificationsRepository =
    DefaultCertificationsRepository(httpClient, apiBaseUrl)

internal class DefaultCertificationsRepository(
    httpClient: HttpClient,
    apiBaseUrl: String,
) : CertificationsRepository {
    private val networkDataSource = CertificationsNetworkDataSource(httpClient, apiBaseUrl)

    override suspend fun getCertifications(): List<CertRecord> = try {
        networkDataSource.fetchCertifications().takeIf { it.isNotEmpty() } ?: mockCertifications
    } catch (_: Exception) {
        mockCertifications
    }

    override suspend fun getCertification(name: String): CertRecord? =
        getCertifications().find { it.name == name }
}
