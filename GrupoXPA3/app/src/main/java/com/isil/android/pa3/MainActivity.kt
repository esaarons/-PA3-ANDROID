package com.isil.android.pa3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.isil.android.pa3.data.datasource.RetrofitClient
import com.isil.android.pa3.data.datasource.TokenManager
import com.isil.android.pa3.data.repository.AuthRepositoryImpl
import com.isil.android.pa3.data.repository.VehiculoRepositoryImpl
import com.isil.android.pa3.presentation.navigation.Rutas
import com.isil.android.pa3.presentation.screens.LoginScreen
import com.isil.android.pa3.presentation.screens.MainContainerScreen
import com.isil.android.pa3.presentation.viewmodel.AppViewModelFactory
import com.isil.android.pa3.presentation.viewmodel.AuthViewModel
import com.isil.android.pa3.presentation.viewmodel.VehiculoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // El splash se instala ANTES de super.onCreate
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Mantiene el splash visible 1,5 segundos para que se aprecie en el video
        var mantenerSplash = true
        Handler(Looper.getMainLooper()).postDelayed({ mantenerSplash = false }, 1500)
        splashScreen.setKeepOnScreenCondition { mantenerSplash }

        // Construcción manual de las dependencias (de la capa data hacia arriba)
        val tokenManager = TokenManager(applicationContext)
        val apiService = RetrofitClient.provideApiService(tokenManager)
        val authRepository = AuthRepositoryImpl(apiService, tokenManager)
        val vehiculoRepository = VehiculoRepositoryImpl(apiService)
        val factory = AppViewModelFactory(authRepository, vehiculoRepository)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel(factory = factory)

                    NavHost(navController = navController, startDestination = Rutas.LOGIN) {
                        composable(Rutas.LOGIN) {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Rutas.PRINCIPAL) {
                                        popUpTo(Rutas.LOGIN) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Rutas.PRINCIPAL) {
                            // Se crea al entrar y se descarta al cerrar sesión
                            val vehiculoViewModel: VehiculoViewModel = viewModel(factory = factory)
                            MainContainerScreen(
                                authViewModel = authViewModel,
                                vehiculoViewModel = vehiculoViewModel,
                                onLogout = {
                                    navController.navigate(Rutas.LOGIN) {
                                        popUpTo(Rutas.PRINCIPAL) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
