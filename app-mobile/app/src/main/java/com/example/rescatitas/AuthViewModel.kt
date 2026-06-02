package com.example.rescatitas

import androidx.lifecycle.viewModelScope
import com.example.rescatitas.Classes.RetrofitClient
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Models.LoginRequest
import com.example.rescatitas.Models.RegisterUserRequest
import com.example.rescatitas.Models.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

// Clase derivada que hereda de BaseViewModel
class AuthViewModel(private val sessionManager: SessionManager) : BaseViewModel<AuthState>(AuthState.Idle) {
    
    val authState: StateFlow<AuthState> = _state

    // Sobrecarga de metodos: login con parametros individuales
    fun login(email: String, password: String) {
        val request = LoginRequest(email, password, "android_device")
        login(request)
    }

    // Sobrecarga de metodos: login con objeto request completo
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val response = RetrofitClient.getUserService().login(request)
                sessionManager.saveAuthToken(response.token)
                _state.value = AuthState.Success(response.user)
            } catch (e: retrofit2.HttpException) {
                val message = when (e.code()) {
                    401 -> "Correo o contraseña incorrectos. Por favor, verifica tus datos."
                    422 -> "Los datos ingresados no son válidos."
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
                val response = RetrofitClient.getUserService().register(request)
                sessionManager.saveAuthToken(response.token)
                _state.value = AuthState.Success(response.user)
            } catch (e: retrofit2.HttpException) {
                val message = when (e.code()) {
                    422 -> "El correo ya está registrado o los datos son inválidos."
                    500 -> "Error en el servidor al crear la cuenta. Inténtalo más tarde."
                    else -> "No se pudo crear la cuenta. (Error ${e.code()})"
                }
                _state.value = AuthState.Error(message)
            } catch (e: Exception) {
                _state.value = AuthState.Error("Error de conexión. Revisa tu internet.")
            }
        }
    }
    
    fun resetState() {
        resetToIdle(AuthState.Idle)
    }
}
