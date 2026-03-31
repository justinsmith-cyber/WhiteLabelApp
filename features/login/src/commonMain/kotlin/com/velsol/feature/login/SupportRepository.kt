package com.velsol.feature.login

import com.velsol.core.domain.brand.BrandConfig

data class SupportData(
    val phone: String,
    val pin: String,
    val email: String,
    val refNumber: String,
)

interface SupportRepository {
    suspend fun getSupportData(): SupportData
}

class MockSupportRepository(private val brandConfig: BrandConfig) : SupportRepository {
    override suspend fun getSupportData(): SupportData = SupportData(
        phone = brandConfig.supportPhone,
        pin = MOCK_PIN,
        email = brandConfig.supportEmail,
        refNumber = MOCK_REF,
    )

    private companion object {
        const val MOCK_PIN = "472"
        const val MOCK_REF = "REF-2026-0042"
    }
}
