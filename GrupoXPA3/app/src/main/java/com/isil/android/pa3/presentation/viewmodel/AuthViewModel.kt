package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.usecase.GetTokenUseCase
import com.isil.android.pa3.domain.usecase.LoginUseCase
import com.isil.android.pa3.domain.usecase.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI de autenticación (una sola data class, como indicó el profesor)
data class AuthUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val token: String = ""
)

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado.asStateFlow()

    // Evento de una sola vez: avisa a la pantalla que el login fue correcto
    private val _loginExitoso = Channel<Unit>(Channel.BUFFERED)
    val loginExitoso: Flow<Unit> = _loginExitoso.receiveAsFlow()

    fun iniciarSesion(usuario: String, clave: String) {
        if (usuario.isBlank() || clave.isBlank()) {
            _estado.update { it.copy(error = "Completa usuario y contraseña.") }
            return
        }
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            loginUseCase(usuario.trim(), clave)
                .onSuccess {
                    _estado.update { it.copy(cargando = false, token = getTokenUseCase().orEmpty()) }
                    _loginExitoso.send(Unit)
                }
                .onFailure { e ->
                    _estado.update { it.copy(cargando = false, error = e.mensajeAmigable()) }
                }
        }
    }

    fun cargarToken() {
        viewModelScope.launch {
            _estado.update { it.copy(token = getTokenUseCase().orEmpty()) }
        }
    }

    // Elimina el token guardado y reinicia el estado
    fun cerrarSesion(alTerminar: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            _estado.value = AuthUiState()
            alTerminar()
        }
    }
}
