package com.example.rescatitas.Models

/**
 * AuthModels: Modelos de datos específicos para el sistema de autenticación y perfiles de usuario.
 */

/**
 * User: Representa la entidad completa de un usuario en el sistema.
 * Contiene datos personales y de cuenta.
 */
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

/**
 * RegisterUserRequest: Datos necesarios para crear un nuevo usuario.
 */
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

/**
 * LoginRequest: Credenciales enviadas para iniciar sesión.
 */
data class LoginRequest(
    val email: String,
    val password: String,
    val device_name: String
)

/**
 * RegisterResponse: Datos devueltos tras un registro exitoso.
 */
data class RegisterResponse(
    val user: User? = null,
    val token: String? = null
)

/**
 * LoginResponse: Datos devueltos tras un inicio de sesión exitoso.
 */
data class LoginResponse(
    val user: User,
    val token: String
)

/**
 * RegisterErrorResponse: Modelo para manejar errores de validación del servidor (ej: email duplicado).
 */
data class RegisterErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>
)
