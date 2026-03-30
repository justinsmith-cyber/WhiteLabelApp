package com.velsol.feature.certifications

internal class GetCertificationUseCase(private val repository: CertificationsRepository) {
    suspend operator fun invoke(name: String): CertRecord? = repository.getCertification(name)
}
