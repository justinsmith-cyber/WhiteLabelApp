package com.velsol.di

import com.velsol.core.domain.brand.BrandConfig
import com.velsol.core.domain.di.AppScope
import com.velsol.core.network.createHttpClient
import com.velsol.feature.certifications.CertificationsRepository
import com.velsol.feature.certifications.createCertificationsRepository
import com.velsol.feature.inventory.InventoryRepository
import com.velsol.feature.inventory.createInventoryRepository
import com.velsol.feature.login.AuthRepository
import com.velsol.feature.login.MockAuthRepository
import com.velsol.feature.login.MockSupportRepository
import com.velsol.feature.login.SupportRepository
import com.velsol.feature.messages.MessagesRepository
import com.velsol.feature.messages.MockMessagesRepository
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient

@DependencyGraph(AppScope::class)
interface AppGraph {
    val brandConfig: BrandConfig
    val httpClient: HttpClient
    val certificationsRepository: CertificationsRepository
    val inventoryRepository: InventoryRepository
    val authRepository: AuthRepository
    val supportRepository: SupportRepository
    val messagesRepository: MessagesRepository

    @Provides
    fun provideHttpClient(): HttpClient = createHttpClient()

    @Provides
    fun provideCertificationsRepository(httpClient: HttpClient, brandConfig: BrandConfig): CertificationsRepository =
        createCertificationsRepository(httpClient, brandConfig.apiBaseUrl)

    @Provides
    fun provideInventoryRepository(): InventoryRepository = createInventoryRepository()

    @Provides
    fun provideAuthRepository(): AuthRepository = MockAuthRepository()

    @Provides
    fun provideSupportRepository(brandConfig: BrandConfig): SupportRepository = MockSupportRepository(brandConfig)

    @Provides
    fun provideMessagesRepository(): MessagesRepository = MockMessagesRepository()
}
