package com.isil.android.pa3.presentation.viewmodel

import retrofit2.HttpException
import java.io.IOException

// Convierte una excepción técnica en un mensaje entendible para el usuario
fun Throwable.mensajeAmigable(): String = when (this) {
    is HttpException -> when (code()) {
        400 -> "Datos inválidos. Revisa la información ingresada."
        401, 403 -> "Usuario o contraseña incorrectos, o sesión vencida."
        404 -> "El registro no existe."
        else -> "Error del servidor (${code()})."
    }
    is IOException -> "No se pudo conectar con el servidor. Revisa la IP y la red."
    else -> "Ocurrió un error inesperado."
}
