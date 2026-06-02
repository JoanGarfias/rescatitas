package com.example.rescatitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Services.PetService
import com.example.rescatitas.Services.UserService
import com.example.rescatitas.ui.*
import com.example.rescatitas.ui.theme.RescatitasTheme
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)
        val authViewModelFactory = AuthViewModelFactory(sessionManager)
        
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Este interceptor le pega el token a cada peticion si es que el usuario ya entro
        val authInterceptor = Interceptor { chain ->
            val token = sessionManager.fetchAuthToken()
            val request = chain.request().newBuilder()
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        val petService = retrofit.create(PetService::class.java)
        val userService = retrofit.create(UserService::class.java)
        
        val petViewModelFactory = PetViewModelFactory(petService)
        val profileViewModelFactory = ProfileViewModelFactory(userService, sessionManager)

        enableEdgeToEdge()
        setContent {
            RescatitasTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
                val petViewModel: PetViewModel = viewModel(factory = petViewModelFactory)
                val profileViewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)

                // Aqui controlamos hacia donde va el usuario en toda la app
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(
                            sessionManager = sessionManager,
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
