package com.example.rescatitas

import androidx.lifecycle.viewModelScope
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Models.User
import com.example.rescatitas.Services.UserService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object LogoutSuccess : ProfileState()
}

// Clase derivada que hereda de BaseViewModel
class ProfileViewModel(
    private val userService: UserService,
    private val sessionManager: SessionManager
) : BaseViewModel<ProfileState>(ProfileState.Idle) {
    
    val profileState: StateFlow<ProfileState> = _state

    fun fetchProfile() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                val user = userService.getProfile()
                _state.value = ProfileState.Success(user)
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message ?: "Error al cargar perfil")
            }
        }
    }

    // Sobrecarga de metodos: logout basico
    fun logout() {
        logout(skipApi = false)
    }

    // Sobrecarga de metodos: logout con opcion de saltar la llamada al API
    fun logout(skipApi: Boolean) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                if (!skipApi) {
                    userService.logout()
                }
            } catch (e: Exception) {
                // Si el API falla, igual queremos limpiar la sesion local
            } finally {
                sessionManager.clearAuthToken()
                _state.value = ProfileState.LogoutSuccess
            }
        }
    }
}

class ProfileViewModelFactory(
    private val userService: UserService,
    private val sessionManager: SessionManager
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userService, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
