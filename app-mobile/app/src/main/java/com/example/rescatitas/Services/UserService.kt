package com.example.rescatitas.Services

import com.example.rescatitas.Models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterUserRequest): RegisterResponse

    @GET("perfil")
    suspend fun getProfile(): User

    @POST("logout")
    suspend fun logout(): Unit
}
