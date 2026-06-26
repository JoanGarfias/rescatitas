package com.example.rescatitas.Models

/**
 * PetModels: Contiene las estructuras de datos (Data Classes) para el manejo de mascotas.
 * Estas clases facilitan el mapeo de las respuestas JSON del servidor a objetos de Kotlin.
 */

/**
 * CreateMascotaRequest: Modelo de datos enviado al servidor al reportar una mascota.
 */
data class CreateMascotaRequest(
    val nombre_mascota: String,
    val tipo_mascota: String,
    val raza: String,
    val descripcion: String,
    val fecha_desaparicion: String,
    val genero: String,
    val direccion: String
)

/**
 * UsuarioPet: Representa información básica del usuario dueño de la publicación.
 * Clase anidada conceptualmente dentro de la respuesta de Pet.
 */
data class UsuarioPet(
    val id: Int,
    val nombre: String
)

/**
 * Pet: Representación completa de una mascota en el sistema.
 * Contiene todos los atributos que regresan de la base de datos.
 */
data class Pet(
    val id_publicacion: Int,
    val id_usuario: Int,
    val nombre_mascota: String,
    val tipo_mascota: String,
    val raza: String,
    val descripcion: String,
    val fecha_desaparicion: String,
    val direccion: String,
    val latitud: String? = null, // Puede ser nulo si no hay GPS
    val longitud: String? = null,
    val estado_mascota: String, // ej: "perdido" o "encontrado"
    val genero: String,
    val estado_publicacion: String,
    val id_rescatista: Int? = null,
    val imagen: String? = null, // URL de la foto en el servidor
    val telefono: String? = null,
    val usuario: UsuarioPet? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

/**
 * CreateMascotaResponse: Respuesta del servidor tras una creación exitosa.
 */
data class CreateMascotaResponse(
    val message: String,
    val pet: Pet
)

/**
 * GetMascotaResponse: Estructura para recibir una lista de mascotas (Home/Buscar).
 */
data class GetMascotaResponse(
    val pets: List<Pet>? = null
)

/**
 * GetOneMascotaResponse: Estructura para recibir el detalle de una sola mascota.
 */
data class GetOneMascotaResponse(
    val pet: Pet? = null
)
