package com.isil.android.pa3.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isil.android.pa3.presentation.navigation.BottomBarScreen
import com.isil.android.pa3.presentation.viewmodel.AuthViewModel
import com.isil.android.pa3.presentation.viewmodel.VehiculoViewModel

// Contenedor con la barra inferior de 3 menús: Inicio, Vehículos y Ajustes
@Composable
fun MainContainerScreen(
    authViewModel: AuthViewModel,
    vehiculoViewModel: VehiculoViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val authEstado by authViewModel.estado.collectAsStateWithLifecycle()

    val pantallas = listOf(BottomBarScreen.Home, BottomBarScreen.Vehiculos, BottomBarScreen.Ajustes)

    // Lee el token guardado apenas se entra al contenedor
    LaunchedEffect(Unit) { authViewModel.cargarToken() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                pantallas.forEach { pantalla ->
                    NavigationBarItem(
                        selected = rutaActual == pantalla.route,
                        onClick = {
                            navController.navigate(pantalla.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(pantalla.icon, contentDescription = pantalla.title) },
                        label = { Text(pantalla.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(token = authEstado.token)
            }
            composable(BottomBarScreen.Vehiculos.route) {
                VehiculosScreen(viewModel = vehiculoViewModel)
            }
            composable(BottomBarScreen.Ajustes.route) {
                AjustesScreen(onCerrarSesion = { authViewModel.cerrarSesion(onLogout) })
            }
        }
    }
}
