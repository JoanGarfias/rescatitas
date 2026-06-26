package com.example.rescatitas.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
            onClick = onNavigateToMap,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFD32F2F),
                selectedTextColor = Color(0xFFD32F2F),
                indicatorColor = Color(0xFFFFEBEE)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas") },
            label = { Text("Alertas") },
            selected = currentRoute == "alertas",
            onClick = onNavigateToAlerts,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFD32F2F),
                selectedTextColor = Color(0xFFD32F2F),
                indicatorColor = Color(0xFFFFEBEE)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "perfil",
            onClick = onNavigateToProfile,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFD32F2F),
                selectedTextColor = Color(0xFFD32F2F),
                indicatorColor = Color(0xFFFFEBEE)
            )
        )
    }
}
