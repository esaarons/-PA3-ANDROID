package com.isil.android.pa3.data.repository

import com.isil.android.pa3.data.datasource.ApiService
import com.isil.android.pa3.data.datasource.TokenManager
import com.isil.android.pa3.data.model.LoginRequest
import com.isil.android.pa3.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val respuesta = api.login(LoginRequest(username, password))
                tokenManager.saveToken(respuesta.accessToken)
            }
        }

    override suspend fun getToken(): String? = tokenManager.getToken.first()

    override suspend fun logout() {
        tokenManager.clearToken()
    }
}
