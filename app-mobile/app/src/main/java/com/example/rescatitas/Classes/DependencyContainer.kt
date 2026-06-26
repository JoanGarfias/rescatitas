package com.example.rescatitas.Classes

import android.content.Context
import com.example.rescatitas.Constants
import com.example.rescatitas.Services.PetService
import com.example.rescatitas.Services.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * DependencyContainer: Clase encargada de la Inyección de Dependencias manual.
 * Centraliza la creación de objetos como SessionManager, Servicios de Retrofit y Clientes HTTP.
 * Facilita que MainActivity y los ViewModels obtengan sus dependencias de forma organizada.
 */
class DependencyContainer(context: Context) {
    
    /**
     * sessionManager: Atributo que gestiona SharedPreferences para persistencia local.
     * Se instancia usando el contexto de la aplicación.
     */
    val sessionManager = SessionManager(context)
    
    /**
     * logging: Interceptor de OkHttp para imprimir las peticiones y respuestas en el Logcat.
     * Muy útil para el feedback de depuración durante el desarrollo.
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * authInterceptor: Interceptor personalizado que inyecta automáticamente el token JWT
     * en cada petición que salga de la app si el usuario está autenticado.
     */
    private val authInterceptor = Interceptor { chain ->
        val token = sessionManager.fetchAuthToken()
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(request.build())
    }

    /**
     * client: Instancia de OkHttpClient configurada con los interceptores definidos.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    /**
     * retrofit: Configuración principal del cliente Retrofit para conectar con el backend.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    /**
     * petService y userService: Instancias finales de las interfaces de servicio listas para usar.
     */
    val petService: PetService = retrofit.create(PetService::class.java)
    val userService: UserService = retrofit.create(UserService::class.java)
}
