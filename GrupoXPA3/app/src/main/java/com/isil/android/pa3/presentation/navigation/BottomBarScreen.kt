package com.isil.android.pa3.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// Los 3 menús de la barra inferior: ruta, título e ícono
sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Inicio", Icons.Default.Home)
    object Vehiculos : BottomBarScreen("vehiculos", "Vehículos", Icons.Default.DirectionsCar)
    object Ajustes : BottomBarScreen("ajustes", "Ajustes", Icons.Default.Settings)
}
