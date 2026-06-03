package com.example.rescatitas.Models

data class User(
    val id: Int? = null,
    val email: String? = null,
    val email_verified_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val nombre: String? = null,
    val apellido_paterno: String? = null,
    val apellido_materno: String? = null,
    val telefono: String? = null,
    val codigo_postal: String? = null,
    val colonia: String? = null,
    val municipio: String? = null,
    val estado: String? = null,
    val fecha_registro: String? = null,
    val fecha_nacimiento: String? = null,
    val confirmada: Boolean? = false,
    val nivel: Int? = 1,
    val rol: String? = "usuario",
)

data class RegisterUserRequest(
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val telefono: String,
    val codigo_postal: String,
    val colonia: String,
    val municipio: String,
    val estado: String,
    val fecha_nacimiento: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val device_name: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
    val device_name: String
)

data class RegisterResponse(
    val user: User? = null,
    val token: String? = null
)

data class LoginResponse(
    val user: User,
    val token: String
)

data class RegisterErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>
)
