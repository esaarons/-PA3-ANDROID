package com.isil.android.pa3.data.repository

import com.isil.android.pa3.data.datasource.ApiService
import com.isil.android.pa3.data.model.toDto
import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.domain.repository.VehiculoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class VehiculoRepositoryImpl(
    private val api: ApiService
) : VehiculoRepository {

    override suspend fun obtenerTodos(): Result<List<Vehiculo>> = withContext(Dispatchers.IO) {
        runCatching { api.getVehiculos().map { it.toDomain() } }
    }

    override suspend fun crear(vehiculo: Vehiculo): Result<Vehiculo> = withContext(Dispatchers.IO) {
        runCatching { api.createVehiculo(vehiculo.toDto()).toDomain() }
    }

    override suspend fun actualizar(vehiculo: Vehiculo): Result<Vehiculo> =
        withContext(Dispatchers.IO) {
            runCatching {
                val id = requireNotNull(vehiculo.id) { "El vehículo no tiene id" }
                api.updateVehiculo(id, vehiculo.toDto()).toDomain()
            }
        }

    override suspend fun eliminar(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val respuesta = api.deleteVehiculo(id)
            if (!respuesta.isSuccessful) throw HttpException(respuesta)
        }
    }
}
