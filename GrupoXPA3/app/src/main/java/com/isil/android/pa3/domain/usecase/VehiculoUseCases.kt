package com.isil.android.pa3.domain.usecase

import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.domain.repository.VehiculoRepository

class GetVehiculosUseCase(private val repository: VehiculoRepository) {
    suspend operator fun invoke(): Result<List<Vehiculo>> = repository.obtenerTodos()
}

class CreateVehiculoUseCase(private val repository: VehiculoRepository) {
    suspend operator fun invoke(vehiculo: Vehiculo): Result<Vehiculo> = repository.crear(vehiculo)
}

class UpdateVehiculoUseCase(private val repository: VehiculoRepository) {
    suspend operator fun invoke(vehiculo: Vehiculo): Result<Vehiculo> = repository.actualizar(vehiculo)
}

class DeleteVehiculoUseCase(private val repository: VehiculoRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> = repository.eliminar(id)
}
