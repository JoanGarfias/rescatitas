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

@Composable
fun PetDetailScreen(
    petId: Int,
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val petState by viewModel.petState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(petId) {
        viewModel.fetchPetById(petId)
    }

    fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }

    fun sendSms(phoneNumber: String, petName: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", "Hola, tengo información sobre $petName")
        }
        context.startActivity(intent)
    }

    fun openMap(latitude: String?, longitude: String?, address: String) {
        val gmmIntentUri = if (latitude != null && longitude != null) {
            Uri.parse("geo:$latitude,$longitude?q=${Uri.encode(address)}")
        } else {
            Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        }
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
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
                                IconButton(
                                    onClick = { /* Share */ },
                                    modifier = Modifier.background(Color.White.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Compartir")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
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

                        // Status Tag
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 80.dp, end = 16.dp),
                            color = Color(0xFFFF5252),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "• PERDIDO",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
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
                        
                        // Characteristics (Mock Tags based on image)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoTag(icon = Icons.Default.Inventory, text = "Collar azul")
                            InfoTag(icon = Icons.Default.Grid4x4, text = "Microchip")
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

                        // Map Placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE0F2F1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(40.dp))
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
                                    Text("Publicado por Usuario", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
                                IconButton(onClick = { /* Chat */ }) {
                                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color(0xFF4DB6AC))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom button
                    }
                }

                // Fixed Bottom Button
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { pet.telefono?.let { makeCall(it) } },
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
                            shape = RoundedCornerShape(30.dp),
                            enabled = pet.telefono != null
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Llamar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        
                        Button(
                            onClick = { pet.telefono?.let { sendSms(it, pet.nombre_mascota) } },
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(30.dp),
                            enabled = pet.telefono != null
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Sms, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SMS", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
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
    }
}

@Composable
fun InfoTag(icon: ImageVector, text: String) {
    Surface(
        color = Color(0xFFF1F8E9),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF558B2F))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 13.sp, color = Color(0xFF558B2F))
        }
    }
}
