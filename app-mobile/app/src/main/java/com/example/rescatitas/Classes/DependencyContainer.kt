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

// Clase adicional para MainActivity que maneja las dependencias
class DependencyContainer(context: Context) {
    
    val sessionManager = SessionManager(context)
    
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val token = sessionManager.fetchAuthToken()
        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(request.build())
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val petService: PetService = retrofit.create(PetService::class.java)
    val userService: UserService = retrofit.create(UserService::class.java)
}
