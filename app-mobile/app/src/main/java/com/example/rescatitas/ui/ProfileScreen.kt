package com.example.rescatitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rescatitas.ProfileState
import com.example.rescatitas.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToAlerts: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.LogoutSuccess) {
            onLogout()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "perfil",
                onNavigateToInicio = onNavigateToHome,
                onNavigateToMap = onNavigateToMap,
                onNavigateToAlerts = onNavigateToAlerts,
                onNavigateToProfile = {}
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (profileState) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF4CAF50))
                }
                is ProfileState.Success -> {
                    val user = (profileState as ProfileState.Success).user
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = CircleShape,
                            color = Color(0xFFE8F5E9)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp),
                                    tint = Color(0xFF4CAF50)
                                )
                            }
                        }

                        Text(
                            text = "${user.nombre ?: ""} ${user.apellido_paterno ?: ""}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = user.email ?: "",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        ProfileInfoItem(icon = Icons.Default.Phone, label = "Teléfono", value = user.telefono ?: "No especificado")
                        ProfileInfoItem(icon = Icons.Default.LocationOn, label = "Ubicación", value = "${user.municipio ?: ""}, ${user.estado ?: ""}")
                        ProfileInfoItem(icon = Icons.Default.Cake, label = "Fecha de Nacimiento", value = user.fecha_nacimiento ?: "No especificada")

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { viewModel.logout() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Logout, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cerrar Sesión", fontSize = 16.sp)
                            }
                        }
                    }
                }
                is ProfileState.Error -> {
                    Text(
                        text = (profileState as ProfileState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ProfileInfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
