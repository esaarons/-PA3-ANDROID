package com.isil.android.pa3.domain.repository

// Contrato de autenticación: el dominio lo define y la capa data lo implementa
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun getToken(): String?
    suspend fun logout()
}
