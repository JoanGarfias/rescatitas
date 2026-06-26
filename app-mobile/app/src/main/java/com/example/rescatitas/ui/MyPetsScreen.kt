package com.example.rescatitas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rescatitas.PetState
import com.example.rescatitas.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetsScreen(
    viewModel: PetViewModel,
    onNavigateToInicio: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit,
    onNavigateToCreatePet: () -> Unit
) {
    val petState by viewModel.petState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMyPets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reportes", fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "mis_mascotas",
                onNavigateToInicio = onNavigateToInicio,
                onNavigateToMap = onNavigateToMap,
                onNavigateToAlerts = { },
                onNavigateToProfile = onNavigateToProfile
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreatePet,
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Reporte")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (petState) {
                is PetState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFD32F2F)
                    )
                }
                is PetState.Success -> {
                    val pets = (petState as PetState.Success).pets
                    if (pets.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Aún no tienes reportes creados", color = Color.Gray)
                            Button(
                                onClick = onNavigateToCreatePet,
                                modifier = Modifier.padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                            ) {
                                Text("Crear mi primer reporte")
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(pets) { pet ->
                                PetCard(pet = pet, onClick = { onNavigateToPetDetail(pet.id_publicacion) })
                            }
                        }
                    }
                }
                is PetState.Error -> {
                    Text(
                        text = (petState as PetState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }
}
