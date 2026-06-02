package com.example.rescatitas.Classes

import com.example.rescatitas.Constants
import com.example.rescatitas.Services.PetService
import com.example.rescatitas.Services.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var userService: UserService? = null
    private var petService: PetService? = null

    private fun getClient(token: String? = null): Retrofit {
        val httpClient = OkHttpClient.Builder()
        
        if (token != null) {
            httpClient.addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
        }

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    fun getUserService(): UserService {
        if (userService == null) {
            userService = getClient().create(UserService::class.java)
        }
        return userService!!
    }

    fun getPetService(token: String?): PetService {
        return getClient(token).create(PetService::class.java)
    }
}
