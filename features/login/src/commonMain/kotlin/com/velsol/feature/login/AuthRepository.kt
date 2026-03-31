package com.velsol.feature.login

import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun signInWithSso(): Result<Unit>
    suspend fun signInWithUsernamePassword(username: String, password: String): Result<Unit>
}

class MockAuthRepository : AuthRepository {
    override suspend fun signInWithSso(): Result<Unit> {
        delay(MOCK_DELAY_MS)
        return Result.success(Unit)
    }

    override suspend fun signInWithUsernamePassword(username: String, password: String): Result<Unit> {
        delay(MOCK_DELAY_MS)
        return Result.success(Unit)
    }

    private companion object {
        const val MOCK_DELAY_MS = 800L
    }
}
