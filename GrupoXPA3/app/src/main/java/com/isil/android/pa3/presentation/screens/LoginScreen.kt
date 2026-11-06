package com.isil.android.pa3.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isil.android.pa3.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // Cuando el login es correcto, navega al contenedor principal
    LaunchedEffect(Unit) {
        viewModel.loginExitoso.collect { onLoginSuccess() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = "Logo de la aplicación",
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Gestión de Vehículos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        estado.error?.let { mensaje ->
            Spacer(Modifier.height(12.dp))
            Text(text = mensaje, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { viewModel.iniciarSesion(usuario, clave) },
            enabled = !estado.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (estado.cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Iniciar sesión")
            }
        }
    }
}
