package com.example.rescatitas.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rescatitas.PetState
import com.example.rescatitas.PetViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PetDetailScreen(
    petId: Int,
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val petState by viewModel.petState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(petId) {
        viewModel.fetchPetById(petId)
    }

    LaunchedEffect(petState) {
        if (petState is PetState.DeleteSuccess) {
            android.widget.Toast.makeText(context, "Publicación eliminada", android.widget.Toast.LENGTH_SHORT).show()
            onNavigateBack()
        }
    }

    fun makeCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.widget.Toast.makeText(context, "No se pudo abrir el marcador", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    fun sendSms(phoneNumber: String, petName: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", "Hola, tengo información sobre $petName")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.widget.Toast.makeText(context, "No se pudo abrir la app de mensajes", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    fun openMap(latitude: String?, longitude: String?, address: String) {
        try {
            val gmmIntentUri = if (latitude != null && longitude != null) {
                Uri.parse("geo:$latitude,$longitude?q=${Uri.encode(address)}")
            } else {
                Uri.parse("geo:0,0?q=${Uri.encode(address)}")
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback si Google Maps no está instalado
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}"))
            context.startActivity(browserIntent)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (petState) {
            is PetState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFD32F2F))
            }
            is PetState.SinglePetSuccess -> {
                val pet = (petState as PetState.SinglePetSuccess).pet
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    // Image Header
                    Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                        AsyncImage(
                            model = pet.imagen ?: "https://via.placeholder.com/600x400",
                            contentDescription = pet.nombre_mascota,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Top Buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .statusBarsPadding(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier.background(Color.White.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                            }
                            
                            Row {
                                if (pet.id_usuario == viewModel.getCurrentUserId()) {
                                    IconButton(
                                        onClick = { showDeleteDialog = true },
                                        modifier = Modifier.background(Color.White.copy(alpha = 0.5f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                IconButton(
                                    onClick = { viewModel.toggleFavorite(petId) },
                                    modifier = Modifier.background(Color.White.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(
                                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorito",
                                        tint = if (isFavorite) Color.Red else Color.Black
                                    )
                                }
                            }
                        }
                    }

                    // Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-24).dp)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                            .background(Color.White)
                            .padding(24.dp)
                    ) {
                        // Handle bar
                        Box(
                            modifier = Modifier
                                .size(40.dp, 4.dp)
                                .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                                .align(Alignment.CenterHorizontally)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = pet.nombre_mascota,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = pet.raza,
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "Visto hace poco",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text("DESCRIPCIÓN", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = pet.descripcion,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp),
                            color = Color(0xFF455A64)
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Characteristics
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val genderIcon = if (pet.genero == "macho") Icons.Default.Male else Icons.Default.Female
                            val genderText = pet.genero.replaceFirstChar { it.uppercase() }
                            InfoTag(icon = genderIcon, text = genderText)
                            
                            val isEncontrado = pet.estado_mascota == "encontrado"
                            val stateIcon = if (isEncontrado) Icons.Default.Check else Icons.Default.QuestionMark
                            val stateText = if (isEncontrado) "Encontrado" else "Perdido"
                            val stateContainerColor = if (isEncontrado) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                            val stateContentColor = if (isEncontrado) Color(0xFF2E7D32) else Color(0xFFEF6C00)
                            
                            InfoTag(
                                icon = stateIcon,
                                text = stateText,
                                containerColor = stateContainerColor,
                                contentColor = stateContentColor
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ÚLTIMA UBICACIÓN CONOCIDA", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                            TextButton(onClick = { openMap(pet.latitud, pet.longitud, pet.direccion) }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Ver detalles", color = Color(0xFF4DB6AC))
                                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF4DB6AC))
                                }
                            }
                        }

                        // Map View
                        val lat = pet.latitud?.toDoubleOrNull()
                        val lng = pet.longitud?.toDoubleOrNull()
                        
                        if (lat != null && lng != null) {
                            val petLocation = LatLng(lat, lng)
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(petLocation, 15f)
                            }
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    Marker(
                                        state = rememberMarkerState(position = petLocation),
                                        title = pet.nombre_mascota
                                    )
                                }
                            }
                        } else {
                            // Map Placeholder if no coordinates
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFE0F2F1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(40.dp))
                                Text("Ubicación no disponible", modifier = Modifier.padding(top = 60.dp), color = Color.Gray)
                            }
                        }
                        
                        Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = pet.direccion, fontSize = 12.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Owner Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Color.LightGray) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(8.dp))
                                }
                                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                    Text("Publicado por " + (pet.usuario?.nombre ?: "Usuario"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Dueño de ${pet.nombre_mascota}", fontSize = 12.sp, color = Color.Gray)
                                    pet.telefono?.let {
                                        Text(it, fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                                pet.telefono?.let { tel ->
                                    IconButton(onClick = { makeCall(tel) }) {
                                        Icon(Icons.Default.Phone, contentDescription = "Llamar", tint = Color(0xFF4DB6AC))
                                    }
                                    IconButton(onClick = { sendSms(tel, pet.nombre_mascota) }) {
                                        Icon(Icons.Default.Sms, contentDescription = "SMS", tint = Color(0xFF4DB6AC))
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom button
                    }
                }
            }
            is PetState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = (petState as PetState.Error).message, color = Color.Red)
                    Button(onClick = { viewModel.fetchPetById(petId) }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Reintentar")
                    }
                }
            }
            else -> {}
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar publicación") },
                text = { Text("¿Estás seguro de que deseas eliminar este reporte? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deletePet(petId)
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun InfoTag(
    icon: ImageVector,
    text: String,
    containerColor: Color = Color(0xFFF1F8E9),
    contentColor: Color = Color(0xFF558B2F)
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = contentColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 13.sp, color = contentColor, fontWeight = FontWeight.Bold)
        }
    }
}
