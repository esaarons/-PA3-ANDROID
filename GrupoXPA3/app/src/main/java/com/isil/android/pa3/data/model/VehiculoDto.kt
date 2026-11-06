package com.isil.android.pa3.data.model

import com.isil.android.pa3.domain.model.Vehiculo

// DTO: espejo del JSON de vehículo que entrega y recibe la API
data class VehiculoDto(
    val id: Int? = null,
    val placa: String,
    val marca: String,
    val modelo: String,
    val anio: Int
) {
    // Convierte el dato de la nube en la entidad limpia del dominio
    fun toDomain() = Vehiculo(
        id = id,
        placa = placa,
        marca = marca,
        modelo = modelo,
        anio = anio
    )
}

// Convierte la entidad del dominio en el DTO que viaja a la API
fun Vehiculo.toDto() = VehiculoDto(
    id = id,
    placa = placa,
    marca = marca,
    modelo = modelo,
    anio = anio
)
