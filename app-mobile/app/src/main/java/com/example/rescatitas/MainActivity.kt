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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Uso de una clase adicional para gestionar la logica de dependencias
        val container = DependencyContainer(this)
        
        val authViewModelFactory = AuthViewModelFactory(container.userService, container.sessionManager)
        val petViewModelFactory = PetViewModelFactory(container.petService, container.sessionManager)
        val profileViewModelFactory = ProfileViewModelFactory(container.userService, container.sessionManager)

        enableEdgeToEdge()
        setContent {
            RescatitasTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
                val petViewModel: PetViewModel = viewModel(factory = petViewModelFactory)
                val profileViewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)

                NavHost(navController = navController, startDestination = "splash") {
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
                    composable("home") {
                        HomeScreen(
                            viewModel = petViewModel,
                            onNavigateToLostPet = { 
                                petViewModel.resetState()
                                navController.navigate("create_pet") 
                            },
                            onNavigateToHelp = { },
                            onNavigateToMap = { },
                            onNavigateToAlerts = { },
                            onNavigateToProfile = { navController.navigate("perfil") },
                            onNavigateToPetDetail = { petId ->
                                petViewModel.resetState()
                                navController.navigate("pet_detail/$petId")
                            }
                        )
                    }
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
                    composable("create_pet") {
                        CreatePetScreen(
                            viewModel = petViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("perfil") {
                        ProfileScreen(
                            viewModel = profileViewModel,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToMap = { },
                            onNavigateToAlerts = { }
                        )
                    }
                }
            }
        }
    }
}
