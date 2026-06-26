package com.example.rescatitas.Services

import com.example.rescatitas.Models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interfaz UserService: Gestiona los endpoints de usuario y autenticación.
 * Comunicación directa con el sistema de seguridad del backend (Sanctum/JWT).
 */
interface UserService {
    /**
     * login: Envía credenciales y recibe el token de sesión y datos del usuario.
     * @Body: Convierte el objeto LoginRequest a JSON automáticamente.
     */
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /**
     * register: Crea una nueva cuenta de usuario en el sistema.
     */
    @POST("register")
    suspend fun register(@Body request: RegisterUserRequest): RegisterResponse

    /**
     * getProfile: Recupera la información del usuario autenticado actualmente.
     */
    @GET("perfil")
    suspend fun getProfile(): User

    /**
     * logout: Notifica al servidor el cierre de sesión para invalidar el token.
     */
    @POST("logout")
    suspend fun logout(): Unit
}
