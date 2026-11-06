package com.isil.android.pa3.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.presentation.viewmodel.VehiculoViewModel

// CRUD de vehículos: listar, agregar, modificar y eliminar
@Composable
fun VehiculosScreen(viewModel: VehiculoViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var mostrarFormulario by remember { mutableStateOf(false) }
    var vehiculoEnEdicion by remember { mutableStateOf<Vehiculo?>(null) }
    var vehiculoAEliminar by remember { mutableStateOf<Vehiculo?>(null) }

    // Muestra el Toast de cada evento y cierra el formulario cuando corresponde
    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            Toast.makeText(context, evento.mensaje, Toast.LENGTH_SHORT).show()
            if (evento.cerrarFormulario) {
                mostrarFormulario = false
                vehiculoEnEdicion = null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vehículos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                vehiculoEnEdicion = null
                mostrarFormulario = true
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Agregar")
            }
        }
        Spacer(Modifier.height(12.dp))

        estado.error?.let { mensaje ->
            Text(text = mensaje, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        if (estado.cargando && estado.vehiculos.isEmpty()) {
            CircularProgressIndicator()
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(estado.vehiculos, key = { it.id ?: 0 }) { vehiculo ->
                VehiculoItem(
                    vehiculo = vehiculo,
                    onEditar = {
                        vehiculoEnEdicion = vehiculo
                        mostrarFormulario = true
                    },
                    onEliminar = { vehiculoAEliminar = vehiculo }
                )
            }
        }
    }

    if (mostrarFormulario) {
        VehiculoFormDialog(
            vehiculo = vehiculoEnEdicion,
            onCancelar = {
                mostrarFormulario = false
                vehiculoEnEdicion = null
            },
            onGuardar = { viewModel.guardar(it) }
        )
    }

    vehiculoAEliminar?.let { vehiculo ->
        AlertDialog(
            onDismissRequest = { vehiculoAEliminar = null },
            title = { Text("Eliminar vehículo") },
            text = { Text("¿Deseas eliminar ${vehiculo.marca} ${vehiculo.modelo} (${vehiculo.placa})?") },
            confirmButton = {
                TextButton(onClick = {
                    vehiculo.id?.let { viewModel.eliminar(it) }
                    vehiculoAEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { vehiculoAEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun VehiculoItem(
    vehiculo: Vehiculo,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${vehiculo.marca} ${vehiculo.modelo}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Placa: ${vehiculo.placa} · Año: ${vehiculo.anio}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar ${vehiculo.placa}")
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar ${vehiculo.placa}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun VehiculoFormDialog(
    vehiculo: Vehiculo?,
    onCancelar: () -> Unit,
    onGuardar: (Vehiculo) -> Unit
) {
    var placa by remember(vehiculo) { mutableStateOf(vehiculo?.placa.orEmpty()) }
    var marca by remember(vehiculo) { mutableStateOf(vehiculo?.marca.orEmpty()) }
    var modelo by remember(vehiculo) { mutableStateOf(vehiculo?.modelo.orEmpty()) }
    var anio by remember(vehiculo) { mutableStateOf(vehiculo?.anio?.toString().orEmpty()) }

    val anioNumero = anio.toIntOrNull()
    val formularioValido = placa.isNotBlank() && marca.isNotBlank() && modelo.isNotBlank() &&
        anioNumero != null && anioNumero in 1950..2100

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (vehiculo == null) "Nuevo vehículo" else "Editar vehículo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = placa, onValueChange = { placa = it },
                    label = { Text("Placa") }, singleLine = true
                )
                OutlinedTextField(
                    value = marca, onValueChange = { marca = it },
                    label = { Text("Marca") }, singleLine = true
                )
                OutlinedTextField(
                    value = modelo, onValueChange = { modelo = it },
                    label = { Text("Modelo") }, singleLine = true
                )
                OutlinedTextField(
                    value = anio, onValueChange = { anio = it },
                    label = { Text("Año") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = formularioValido,
                onClick = {
                    onGuardar(
                        Vehiculo(
                            id = vehiculo?.id,
                            placa = placa.trim().uppercase(),
                            marca = marca.trim(),
                            modelo = modelo.trim(),
                            anio = anioNumero ?: 0
                        )
                    )
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}
