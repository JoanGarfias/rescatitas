package com.example.rescatitas

/**
 * Objeto Constants: Contenedor global para valores constantes que se repiten en el proyecto.
 * Centralizar la URL base aquí facilita cambios en el entorno de desarrollo o producción.
 */
object Constants {
    /**
     * BASE_URL: Dirección principal del backend API. 
     * Se usa en la configuración de RetrofitClient.
     */
    const val BASE_URL = "https://rescatitas-backend.joangarfias.me/api/"
}
