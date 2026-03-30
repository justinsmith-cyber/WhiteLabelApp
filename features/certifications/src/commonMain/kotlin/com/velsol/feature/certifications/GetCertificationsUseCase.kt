package com.velsol.feature.certifications

internal class GetCertificationsUseCase(private val repository: CertificationsRepository) {
    suspend operator fun invoke(): List<CertRecord> = repository.getCertifications()
}
