package com.example.rescatitas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rescatitas.PetState
import com.example.rescatitas.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit
) {
    val favoritePets by viewModel.favoritePets.collectAsState()
    val petState by viewModel.petState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFavoritePets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (petState is PetState.Loading && favoritePets.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFD32F2F)
                )
            } else if (favoritePets.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No tienes mascotas favoritas aún",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        "Las mascotas que marques con el corazón aparecerán aquí.",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(favoritePets) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = { onNavigateToPetDetail(pet.id_publicacion) }
                        )
                    }
                }
            }
        }
    }
}
