package com.example.rescatitas.Models

data class ComentarioUser(
    val nombre: String,
    val apellido_paterno: String
)

data class Comentario(
    val id_comentario: Int,
    val contenido: String,
    val id_usuario: Int? = null,
    val id_publicacion: Int? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val usuario: ComentarioUser? = null
)

data class ComentariosRequest(
    val id_publicacion: Int,
    val mensaje: String
)

data class ComentariosResponse(
    val message: String,
    val comentario: Comentario
)

data class CreatePublicacionComentarioRequest(
    val id_publicacion: Int,
    val mensaje: String
)

data class CreatePublicacionComentarioResponse(
    val message: String,
    val comentario: Comentario
)

data class PublicacionComentarioResponse(
    val comentarios: List<Comentario>
)
