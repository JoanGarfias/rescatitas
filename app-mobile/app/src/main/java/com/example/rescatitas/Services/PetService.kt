package com.example.rescatitas.Services

import com.example.rescatitas.Models.CreateMascotaResponse
import com.example.rescatitas.Models.GetMascotaResponse
import com.example.rescatitas.Models.GetOneMascotaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Interfaz PetService: Define los endpoints relacionados con las mascotas para Retrofit.
 * Emplea anotaciones de Retrofit (@GET, @POST, @Multipart) para mapear las peticiones HTTP.
 */
interface PetService {
    /**
     * getAllPets: Recupera el listado completo de mascotas desde el servidor.
     * Retorna un objeto GetMascotaResponse que contiene la lista.
     */
    @GET("mascotas")
    suspend fun getAllPets(): GetMascotaResponse

    /**
     * getPetById: Obtiene el detalle de una mascota específica usando su ID.
     * @Path("id"): Inyecta el ID del parámetro directamente en la URL.
     */
    @GET("mascotas/{id}")
    suspend fun getPetById(@Path("id") id: Int): GetOneMascotaResponse

    /**
     * getRecents: Obtiene las mascotas reportadas recientemente.
     */
    @GET("mascotas/recientes")
    suspend fun getRecents(): GetMascotaResponse

    /**
     * search: Endpoint planeado para realizar búsquedas con filtros.
     */
    @GET("mascotas/buscar")
    suspend fun search(): GetMascotaResponse

    /**
     * createPet: Envía un nuevo reporte de mascota al servidor.
     * @Multipart: Permite el envío de archivos (fotos) junto con campos de texto.
     * @Part: Especifica cada fragmento de la petición multipart.
     */
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
        @Part("telefono") telefono: RequestBody,
        @Part imagen: MultipartBody.Part, // La foto de la mascota como archivo binario
        @Part("latitud") latitud: RequestBody? = null,
        @Part("longitud") longitud: RequestBody? = null
    ): CreateMascotaResponse
}
