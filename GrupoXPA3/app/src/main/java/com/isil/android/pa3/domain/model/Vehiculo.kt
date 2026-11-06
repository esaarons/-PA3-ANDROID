package com.isil.android.pa3.domain.model

// Entidad de dominio: lo único que la interfaz y los casos de uso conocen
data class Vehiculo(
    val id: Int? = null,
    val placa: String,
    val marca: String,
    val modelo: String,
    val anio: Int
)
