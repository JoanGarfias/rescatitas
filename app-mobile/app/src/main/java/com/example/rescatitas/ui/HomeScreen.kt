package com.example.rescatitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.example.rescatitas.Models.Pet
import com.example.rescatitas.PetState
import com.example.rescatitas.PetViewModel

@Composable
fun HomeScreen(
    viewModel: PetViewModel,
    onNavigateToLostPet: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit
) {
    val petState by viewModel.petState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecentPets()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "inicio",
                onNavigateToInicio = { },
                onNavigateToMap = onNavigateToMap,
                onNavigateToAlerts = onNavigateToAlerts,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Rescatitas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "¡Perdí mi mascota!",
                    icon = Icons.Default.Campaign,
                    containerColor = Color(0xFFFF7043),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToLostPet
                )
                ActionButton(
                    text = "Quiero ayudar",
                    icon = Icons.Default.Pets,
                    containerColor = Color(0xFF4DB6AC),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToHelp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mascotas Perdidas Recientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (petState) {
                is PetState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF4CAF50))
                    }
                }
                is PetState.Success -> {
                    val pets = (petState as PetState.Success).pets
                    if (pets.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay mascotas recientes")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(pets) { pet ->
                                PetCard(pet, onClick = { onNavigateToPetDetail(pet.id_publicacion) })
                            }
                        }
                    }
                }
                is PetState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = (petState as PetState.Error).message, color = Color.Red)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PetCard(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = pet.imagen ?: "https://via.placeholder.com/400x200",
                    contentDescription = pet.nombre_mascota,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    color = Color(0xFFFFF176),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Urgente",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = pet.nombre_mascota,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = pet.direccion, fontSize = 14.sp, color = Color.Gray)
                }
                Text(
                    text = pet.descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF455A64),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val timeSince = remember(pet.fecha_desaparicion) {
                        try {
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val disappearanceDate = LocalDate.parse(pet.fecha_desaparicion.substringBefore(" "), formatter)
                            val today = LocalDate.now()
                            val days = ChronoUnit.DAYS.between(disappearanceDate, today)
                            
                            when {
                                days == 0L -> "Hoy"
                                days == 1L -> "Hace 1 día"
                                days < 30L -> "Hace $days días"
                                days < 365L -> "Hace ${days / 30} meses"
                                else -> "Hace ${days / 365} años"
                            }
                        } catch (e: Exception) {
                            pet.fecha_desaparicion
                        }
                    }
                    Text(text = timeSince, fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "Ver detalles",
                        fontSize = 14.sp,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigateToInicio: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "inicio",
            onClick = onNavigateToInicio,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFD32F2F),
                selectedTextColor = Color(0xFFD32F2F),
                indicatorColor = Color(0xFFFFEBEE)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Map, contentDescription = "Mapa") },
            label = { Text("Mapa") },
            selected = currentRoute == "mapa",
            onClick = onNavigateToMap
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas") },
            label = { Text("Alertas") },
            selected = currentRoute == "alertas",
            onClick = onNavigateToAlerts
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "perfil",
            onClick = onNavigateToProfile
        )
    }
}
