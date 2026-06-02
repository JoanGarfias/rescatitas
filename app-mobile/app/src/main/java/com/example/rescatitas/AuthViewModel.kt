package com.example.rescatitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatitas.Classes.RetrofitClient
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Models.LoginRequest
import com.example.rescatitas.Models.RegisterUserRequest
import com.example.rescatitas.Models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val request = LoginRequest(email, password, "android_device")
                val response = RetrofitClient.getUserService().login(request)
                sessionManager.saveAuthToken(response.token)
                _authState.value = AuthState.Success(response.user)
            } catch (e: retrofit2.HttpException) {
                // Aqui manejamos los errores segun lo que responda el servidor
                val message = when (e.code()) {
                    401 -> "Correo o contraseña incorrectos. Por favor, verifica tus datos."
                    422 -> "Los datos ingresados no son válidos."
                    500 -> "Tenemos problemas en nuestro servidor. Inténtalo más tarde."
                    else -> "No se pudo iniciar sesión. (Error ${e.code()})"
                }
                _authState.value = AuthState.Error(message)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error de conexión. Revisa tu internet.")
            }
        }
    }

    fun register(request: RegisterUserRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = RetrofitClient.getUserService().register(request)
                sessionManager.saveAuthToken(response.token)
                _authState.value = AuthState.Success(response.user)
            } catch (e: retrofit2.HttpException) {
                // Si el registro falla, le decimos al usuario por que
                val message = when (e.code()) {
                    422 -> "El correo ya está registrado o los datos son inválidos."
                    500 -> "Error en el servidor al crear la cuenta. Inténtalo más tarde."
                    else -> "No se pudo crear la cuenta. (Error ${e.code()})"
                }
                _authState.value = AuthState.Error(message)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error de conexión. Revisa tu internet.")
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
