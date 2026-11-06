package com.isil.android.pa3.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Ajustes: cerrar sesión (elimina el token guardado)
@Composable
fun AjustesScreen(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Al cerrar sesión se elimina el token guardado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onCerrarSesion) {
            Text("Cerrar sesión")
        }
    }
}
