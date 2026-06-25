package com.example.rescatitas.Models

data class CreateMascotaRequest(
    val nombre_mascota: String,
    val tipo_mascota: String,
    val raza: String,
    val descripcion: String,
    val fecha_desaparicion: String,
    val genero: String,
    val direccion: String
)

data class Pet(
    val id_publicacion: Int,
    val id_usuario: Int,
    val nombre_mascota: String,
    val tipo_mascota: String,
    val raza: String,
    val descripcion: String,
    val fecha_desaparicion: String,
    val direccion: String,
    val latitud: String? = null,
    val longitud: String? = null,
    val estado_mascota: String,
    val genero: String,
    val estado_publicacion: String,
    val id_rescatista: Int? = null,
    val imagen: String? = null,
    val telefono: String? = null,
    val created_at: String,
    val updated_at: String
)

data class CreateMascotaResponse(
    val message: String,
    val pet: Pet
)

data class GetMascotaResponse(
    val pets: List<Pet>? = null
)

data class GetOneMascotaResponse(
    val pet: Pet? = null
)
