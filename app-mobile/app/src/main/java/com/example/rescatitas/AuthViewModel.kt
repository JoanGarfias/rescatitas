package com.example.rescatitas

import androidx.lifecycle.viewModelScope
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Models.LoginRequest
import com.example.rescatitas.Models.RegisterUserRequest
import com.example.rescatitas.Models.User
import com.example.rescatitas.Services.UserService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val userService: UserService,
    private val sessionManager: SessionManager
) : BaseViewModel<AuthState>(AuthState.Idle) {
    
    val authState: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        val request = LoginRequest(email, password, "android_device")
        login(request)
    }

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val response = userService.login(request)
                sessionManager.saveAuthToken(response.token)
                sessionManager.saveUserId(response.user.id ?: -1)
                _state.value = AuthState.Success(response.user)
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val message = when (e.code()) {
                    422 -> {
                        // Intentamos extraer los mensajes de error especificos del JSON de Laravel/Backend
                        if (errorBody != null && errorBody.contains("errors")) {
                            "Error en los datos: $errorBody"
                        } else {
                            "Los datos ingresados no son válidos o el correo ya existe."
                        }
                    }
                    500 -> "Tenemos problemas en nuestro servidor. Inténtalo más tarde."
                    else -> "No se pudo iniciar sesión. (Error ${e.code()})"
                }
                _state.value = AuthState.Error(message)
            } catch (e: Exception) {
                _state.value = AuthState.Error("Error de conexión. Revisa tu internet.")
            }
        }
    }

    fun register(request: RegisterUserRequest) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val response = userService.register(request)
                val token = response.token
                val user = response.user
                
                if (token != null && user != null) {
                    sessionManager.saveAuthToken(token)
                    user.id?.let { sessionManager.saveUserId(it) }
                    _state.value = AuthState.Success(user)
                } else {
                    _state.value = AuthState.Error("Respuesta del servidor incompleta.")
                }
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val message = when (e.code()) {
                    422 -> {
                        if (errorBody?.contains("email") == true && errorBody.contains("taken")) {
                            "El correo electrónico ya está en uso."
                        } else {
                            "Error de validación. Revisa tus datos e intenta de nuevo."
                        }
                    }
                    500 -> "Error interno del servidor. Inténtalo más tarde."
                    else -> "Error en el registro (${e.code()})."
                }
                // Mostramos el mensaje amigable y el detalle tecnico para facilitar el debug
                _state.value = AuthState.Error("$message\nDetalle: ${errorBody ?: e.message()}")
            } catch (e: Exception) {
                _state.value = AuthState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    fun resetState() {
        resetToIdle(AuthState.Idle)
    }
}
