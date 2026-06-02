package com.example.rescatitas.Services

import com.example.rescatitas.Models.CreateMascotaResponse
import com.example.rescatitas.Models.GetMascotaResponse
import com.example.rescatitas.Models.GetOneMascotaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PetService {
    @GET("mascotas")
    suspend fun getAllPets(): GetMascotaResponse

    @GET("mascotas/{id}")
    suspend fun getPetById(@Path("id") id: Int): GetOneMascotaResponse

    @GET("mascotas/recientes")
    suspend fun getRecents(): GetMascotaResponse

    @GET("mascotas/buscar")
    suspend fun search(): GetMascotaResponse

    @Multipart
    @POST("mascotas")
    suspend fun createPet(
        @Part("nombre_mascota") nombre: RequestBody,
        @Part("tipo_mascota") tipo: RequestBody,
        @Part("raza") raza: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("fecha_desaparicion") fecha: RequestBody,
        @Part("genero") genero: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part imagen: MultipartBody.Part
    ): CreateMascotaResponse
}
