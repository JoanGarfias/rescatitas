package com.example.rescatitas.Classes

import com.example.rescatitas.Constants
import com.example.rescatitas.Services.PetService
import com.example.rescatitas.Services.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient: Objeto Singleton encargado de configurar y proveer las instancias de los servicios API.
 * Gestiona la comunicación de red y la inyección del token de autorización en las cabeceras.
 */
object RetrofitClient {
    // Atributos privados para el patrón Singleton
    private var userService: UserService? = null
    private var petService: PetService? = null

    /**
     * getClient: Configura el cliente de Retrofit con OkHttp.
     * @param token: Token de sesión opcional para peticiones autenticadas.
     * Implementa un Interceptor para añadir la cabecera "Authorization" automáticamente.
     */
    private fun getClient(token: String? = null): Retrofit {
        val httpClient = OkHttpClient.Builder() // Instanciación de librería externa
        
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Atributo de la clase Constants
            .addConverterFactory(GsonConverterFactory.create()) // Conversión JSON a Objetos Kotlin
            .client(httpClient.build())
            .build()
    }

    /**
     * getUserService: Retorna la instancia para servicios de usuario.
     */
    fun getUserService(): UserService {
        if (userService == null) {
            userService = getClient().create(UserService::class.java)
        }
        return userService!!
    }

    /**
     * getPetService: Retorna la instancia para servicios de mascotas.
     * @param token: Requiere el token para identificar al usuario que realiza acciones.
     */
    fun getPetService(token: String?): PetService {
        return getClient(token).create(PetService::class.java)
    }
}
