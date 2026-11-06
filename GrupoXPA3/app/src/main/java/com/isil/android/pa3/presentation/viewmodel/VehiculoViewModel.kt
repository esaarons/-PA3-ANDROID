package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.domain.usecase.CreateVehiculoUseCase
import com.isil.android.pa3.domain.usecase.DeleteVehiculoUseCase
import com.isil.android.pa3.domain.usecase.GetVehiculosUseCase
import com.isil.android.pa3.domain.usecase.UpdateVehiculoUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VehiculoUiState(
    val vehiculos: List<Vehiculo> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

// Mensaje que la pantalla muestra como Toast (y si debe cerrar el formulario)
data class VehiculoEvento(
    val mensaje: String,
    val cerrarFormulario: Boolean = false
)

class VehiculoViewModel(
    private val getVehiculosUseCase: GetVehiculosUseCase,
    private val createVehiculoUseCase: CreateVehiculoUseCase,
    private val updateVehiculoUseCase: UpdateVehiculoUseCase,
    private val deleteVehiculoUseCase: DeleteVehiculoUseCase
) : ViewModel() {

    companion object {
        const val MENSAJE_GUARDADO = "Información guardada correctamente"
    }

    private val _estado = MutableStateFlow(VehiculoUiState())
    val estado: StateFlow<VehiculoUiState> = _estado.asStateFlow()

    private val _eventos = Channel<VehiculoEvento>(Channel.BUFFERED)
    val eventos: Flow<VehiculoEvento> = _eventos.receiveAsFlow()

    init {
        cargar()
    }

    // READ: lista todos los vehículos
    fun cargar() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            getVehiculosUseCase()
                .onSuccess { lista -> _estado.update { it.copy(cargando = false, vehiculos = lista) } }
                .onFailure { e -> _estado.update { it.copy(cargando = false, error = e.mensajeAmigable()) } }
        }
    }

    // CREATE (id == null) o UPDATE (id != null)
    fun guardar(vehiculo: Vehiculo) {
        viewModelScope.launch {
            val resultado = if (vehiculo.id == null) {
                createVehiculoUseCase(vehiculo)
            } else {
                updateVehiculoUseCase(vehiculo)
            }
            resultado
                .onSuccess {
                    _eventos.send(VehiculoEvento(MENSAJE_GUARDADO, cerrarFormulario = true))
                    cargar()
                }
                .onFailure { e -> _eventos.send(VehiculoEvento(e.mensajeAmigable())) }
        }
    }

    // DELETE
    fun eliminar(id: Int) {
        viewModelScope.launch {
            deleteVehiculoUseCase(id)
                .onSuccess {
                    _eventos.send(VehiculoEvento("Vehículo eliminado"))
                    cargar()
                }
                .onFailure { e -> _eventos.send(VehiculoEvento(e.mensajeAmigable())) }
        }
    }
}
