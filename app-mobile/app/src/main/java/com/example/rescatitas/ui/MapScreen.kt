package com.example.rescatitas.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.rescatitas.Models.Pet
import com.example.rescatitas.PetState
import com.example.rescatitas.PetViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    viewModel: PetViewModel,
    onNavigateToInicio: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit
) {
    val petState by viewModel.petState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    var selectedPet by remember { mutableStateOf<Pet?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(19.4326, -99.1332), 10f)
    }

    // Opcional: Recargar mascotas cuando la cámara se deja de mover
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            // Aquí se podría llamar a una función que filtre por coordenadas si el API lo soportara
            // Por ahora refrescamos los recientes
            viewModel.fetchRecentPets()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasLocationPermission = granted
            if (granted) {
                scope.launch {
                    val location = fusedLocationClient.lastLocation.await()
                    location?.let {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(
                                LatLng(it.latitude, it.longitude),
                                15f
                            )
                        )
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    15f
                )
            }
        }
        viewModel.fetchRecentPets()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "mapa",
                onNavigateToInicio = onNavigateToInicio,
                onNavigateToMap = { },
                onNavigateToAlerts = onNavigateToAlerts,
                onNavigateToProfile = onNavigateToProfile
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (hasLocationPermission) {
                        scope.launch {
                            val location = fusedLocationClient.lastLocation.await()
                            location?.let {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngZoom(
                                        LatLng(it.latitude, it.longitude),
                                        15f
                                    )
                                )
                            }
                        }
                    } else {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                containerColor = Color.White,
                contentColor = Color(0xFFD32F2F)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Mi ubicación")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(myLocationButtonEnabled = false)
            ) {
                if (petState is PetState.Success) {
                    val pets = (petState as PetState.Success).pets
                    pets.forEach { pet ->
                        val lat = pet.latitud?.toDoubleOrNull()
                        val lng = pet.longitud?.toDoubleOrNull()
                        if (lat != null && lng != null) {
                            key(pet.id_publicacion) {
                                Marker(
                                    state = rememberMarkerState(position = LatLng(lat, lng)),
                                    title = pet.nombre_mascota,
                                    onClick = {
                                        selectedPet = pet
                                        showBottomSheet = true
                                        true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (showBottomSheet && selectedPet != null) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    PetQuickInfo(
                        pet = selectedPet!!,
                        onDetailClick = {
                            showBottomSheet = false
                            onNavigateToPetDetail(selectedPet!!.id_publicacion)
                        }
                    )
                }
            }

            if (petState is PetState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFD32F2F)
                )
            }
        }
    }
}

@Composable
fun PetQuickInfo(pet: Pet, onDetailClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = pet.imagen ?: "https://via.placeholder.com/150",
                contentDescription = pet.nombre_mascota,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = pet.nombre_mascota,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Text(
                    text = pet.raza,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                
                Surface(
                    modifier = Modifier.padding(top = 8.dp),
                    color = if (pet.estado_mascota == "encontrado") Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = pet.estado_mascota.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pet.estado_mascota == "encontrado") Color(0xFF2E7D32) else Color(0xFFEF6C00)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = pet.descripcion,
            maxLines = 2,
            fontSize = 14.sp,
            color = Color(0xFF455A64),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Button(
            onClick = onDetailClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Ver detalles completos", fontWeight = FontWeight.Bold)
        }
    }
}
