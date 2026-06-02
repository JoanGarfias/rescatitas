package com.example.rescatitas.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rescatitas.PetState
import com.example.rescatitas.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePetScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipoMascota by remember { mutableStateOf("perro") }
    var genero by remember { mutableStateOf("macho") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    var tipoExpanded by remember { mutableStateOf(false) }
    var generoExpanded by remember { mutableStateOf(false) }

    val petState by viewModel.petState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(petState) {
        if (petState is PetState.ReportSuccess) {
            Toast.makeText(context, (petState as PetState.ReportSuccess).message, Toast.LENGTH_LONG).show()
            viewModel.resetState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Reporte", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color(0xFFD32F2F))
                    }
                },
                actions = {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.padding(end = 16.dp))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Subir foto", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8F9FA))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(40.dp))
                                Text("Toca para subir una foto clara", fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))
                                Text("Formatos: JPG, PNG (Max 5MB)", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }

                    FormTextField(label = "Nombre de la mascota", value = nombre, onValueChange = { nombre = it }, placeholder = "Ej: Firulais")
                    FormTextField(label = "Raza", value = raza, onValueChange = { raza = it }, placeholder = "Ej: Golden Retriever")
                    FormTextField(label = "Fecha de extravío", value = fecha, onValueChange = { fecha = it }, placeholder = "aaaa-mm-dd")
                    FormTextField(label = "Dirección / Ubicación", value = direccion, onValueChange = { direccion = it }, placeholder = "Ej: Parque México, Condesa")
                    
                    Text("Tipo de mascota", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 16.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { tipoExpanded = true },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(tipoMascota.replaceFirstChar { it.uppercase() }, color = Color.Black)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                        DropdownMenu(expanded = tipoExpanded, onDismissRequest = { tipoExpanded = false }) {
                            listOf("perro", "gato", "conejo", "otro").forEach { tipo ->
                                DropdownMenuItem(text = { Text(tipo.replaceFirstChar { it.uppercase() }) }, onClick = {
                                    tipoMascota = tipo
                                    tipoExpanded = false
                                })
                            }
                        }
                    }

                    Text("Género", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 16.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { generoExpanded = true },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(genero.replaceFirstChar { it.uppercase() }, color = Color.Black)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                        DropdownMenu(expanded = generoExpanded, onDismissRequest = { generoExpanded = false }) {
                            listOf("macho", "hembra", "desconocido").forEach { g ->
                                DropdownMenuItem(text = { Text(g.replaceFirstChar { it.uppercase() }) }, onClick = {
                                    genero = g
                                    generoExpanded = false
                                })
                            }
                        }
                    }

                    FormTextField(label = "Descripción", value = descripcion, onValueChange = { descripcion = it }, placeholder = "Señas particulares, collar, etc.", singleLine = false)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (imageUri == null) {
                                Toast.makeText(context, "Por favor selecciona una imagen", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.createPetReport(context, nombre, tipoMascota, raza, descripcion, fecha, genero, direccion, imageUri!!)
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
                        shape = RoundedCornerShape(12.dp),
                        enabled = petState !is PetState.Loading
                    ) {
                        if (petState is PetState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Send, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Publicar Reporte", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (petState is PetState.Error) {
                        Text(
                            text = (petState as PetState.Error).message,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFF00796B))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Consejo de búsqueda", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF00796B))
                        Text(
                            "Incluye señas particulares como collares, manchas específicas o si responde a algún silbido. Esto acelera la identificación.",
                            fontSize = 12.sp,
                            color = Color(0xFF004D40)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F5F9),
                focusedContainerColor = Color(0xFFF1F5F9),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 3
        )
    }
}
