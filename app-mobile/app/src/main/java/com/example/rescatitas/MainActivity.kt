package com.example.rescatitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rescatitas.Classes.DependencyContainer
import com.example.rescatitas.ui.*
import com.example.rescatitas.ui.theme.RescatitasTheme

/**
 * MainActivity: Punto de entrada principal de la aplicación Android.
 * Hereda de ComponentActivity y se encarga de configurar la navegación y el grafo de dependencias.
 */
class MainActivity : ComponentActivity() {
    /**
     * onCreate: Método del ciclo de vida donde se inicializa la actividad.
     * Documentamos la instanciación de objetos de arquitectura Jetpack Compose y Navigation.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Instanciación del contenedor de dependencias para centralizar recursos
        val container = DependencyContainer(this)
        
        // Creación de las Factorías (Factories) para inyectar servicios en los ViewModels
        val authViewModelFactory = AuthViewModelFactory(container.userService, container.sessionManager)
        val petViewModelFactory = PetViewModelFactory(container.petService, container.sessionManager)
        val profileViewModelFactory = ProfileViewModelFactory(container.userService, container.sessionManager)

        enableEdgeToEdge() // Habilitar diseño inmersivo (edge-to-edge)
        
        setContent {
            RescatitasTheme {
                // Instanciación del controlador de navegación
                val navController = rememberNavController()
                
                // Obtención de ViewModels usando las factorías documentadas anteriormente
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
                val petViewModel: PetViewModel = viewModel(factory = petViewModelFactory)
                val profileViewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)

                /**
                 * NavHost: Define el grafo de navegación de la app.
                 * Cada "composable" representa una pantalla diferente.
                 */
                NavHost(navController = navController, startDestination = "splash") {
                    
                    // Pantalla de carga y verificación de sesión
                    composable("splash") {
                        SplashScreen(
                            sessionManager = container.sessionManager,
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            },
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    // Pantalla de inicio de sesión
                    composable("login") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onNavigateToRegister = { 
                                authViewModel.resetState()
                                navController.navigate("register") 
                            },
                            onLoginSuccess = { 
                                navController.navigate("home") { 
                                    popUpTo("login") { inclusive = true } 
                                }
                            }
                        )
                    }

                    // Pantalla de registro de nuevos usuarios
                    composable("register") {
                        RegisterScreen(
                            viewModel = authViewModel,
                            onNavigateToLogin = { 
                                authViewModel.resetState()
                                navController.popBackStack() 
                            },
                            onRegisterSuccess = {
                                navController.navigate("home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                    // Pantalla principal (Lista de mascotas recientes)
                    composable("home") {
                        HomeScreen(
                            viewModel = petViewModel,
                            onNavigateToLostPet = { 
                                petViewModel.resetState()
                                navController.navigate("create_pet") 
                            },
                            onNavigateToHelp = { },
                            onNavigateToMap = { navController.navigate("mapa") },
                            onNavigateToAlerts = { },
                            onNavigateToProfile = { navController.navigate("perfil") },
                            onNavigateToPetDetail = { petId ->
                                petViewModel.resetState()
                                navController.navigate("pet_detail/$petId")
                            },
                            onNavigateToFavorites = {
                                navController.navigate("favorites")
                            }
                        )
                    }

                    // Pantalla de mapa de mascotas
                    composable("mapa") {
                        MapScreen(
                            viewModel = petViewModel,
                            onNavigateToInicio = { navController.navigate("home") },
                            onNavigateToAlerts = { },
                            onNavigateToProfile = { navController.navigate("perfil") },
                            onNavigateToPetDetail = { petId ->
                                petViewModel.resetState()
                                navController.navigate("pet_detail/$petId")
                            }
                        )
                    }

                    // Pantalla de mascotas marcadas como favoritas (Persistencia SharedPreferences)
                    composable("favorites") {
                        FavoritesScreen(
                            viewModel = petViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToPetDetail = { petId ->
                                navController.navigate("pet_detail/$petId")
                            }
                        )
                    }

                    // Pantalla de detalle de una mascota (Inyección de parámetros por URL)
                    composable(
                        "pet_detail/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId") ?: 0
                        PetDetailScreen(
                            petId = petId,
                            viewModel = petViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // Pantalla para crear un nuevo reporte de mascota perdida
                    composable("create_pet") {
                        CreatePetScreen(
                            viewModel = petViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // Pantalla de perfil de usuario y configuración
                    composable("perfil") {
                        ProfileScreen(
                            viewModel = profileViewModel,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToMap = { navController.navigate("mapa") },
                            onNavigateToAlerts = { }
                        )
                    }
                }
            }
        }
    }
}
