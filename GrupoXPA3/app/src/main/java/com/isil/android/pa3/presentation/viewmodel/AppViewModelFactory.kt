package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isil.android.pa3.domain.repository.AuthRepository
import com.isil.android.pa3.domain.repository.VehiculoRepository
import com.isil.android.pa3.domain.usecase.CreateVehiculoUseCase
import com.isil.android.pa3.domain.usecase.DeleteVehiculoUseCase
import com.isil.android.pa3.domain.usecase.GetTokenUseCase
import com.isil.android.pa3.domain.usecase.GetVehiculosUseCase
import com.isil.android.pa3.domain.usecase.LoginUseCase
import com.isil.android.pa3.domain.usecase.LogoutUseCase
import com.isil.android.pa3.domain.usecase.UpdateVehiculoUseCase

// Arma los ViewModels con sus casos de uso a partir de los repositorios
class AppViewModelFactory(
    private val authRepository: AuthRepository,
    private val vehiculoRepository: VehiculoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(
            LoginUseCase(authRepository),
            GetTokenUseCase(authRepository),
            LogoutUseCase(authRepository)
        ) as T

        modelClass.isAssignableFrom(VehiculoViewModel::class.java) -> VehiculoViewModel(
            GetVehiculosUseCase(vehiculoRepository),
            CreateVehiculoUseCase(vehiculoRepository),
            UpdateVehiculoUseCase(vehiculoRepository),
            DeleteVehiculoUseCase(vehiculoRepository)
        ) as T

        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
