package com.isil.android.pa3.domain.usecase

import com.isil.android.pa3.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> =
        repository.login(username, password)
}

class GetTokenUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): String? = repository.getToken()
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
