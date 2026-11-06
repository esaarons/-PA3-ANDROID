package com.isil.android.pa3.domain.repository

import com.isil.android.pa3.domain.model.Vehiculo

// Contrato del CRUD de vehículos
interface VehiculoRepository {
    suspend fun obtenerTodos(): Result<List<Vehiculo>>
    suspend fun crear(vehiculo: Vehiculo): Result<Vehiculo>
    suspend fun actualizar(vehiculo: Vehiculo): Result<Vehiculo>
    suspend fun eliminar(id: Int): Result<Unit>
}
