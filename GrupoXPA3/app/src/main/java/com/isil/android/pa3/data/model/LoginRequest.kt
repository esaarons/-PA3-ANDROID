package com.isil.android.pa3.data.model

// DTO: cuerpo que se envía a la API para iniciar sesión
data class LoginRequest(
    val username: String,
    val password: String
)
