package com.example.rescatitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Models.User
import com.example.rescatitas.Services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object LogoutSuccess : ProfileState()
}

class ProfileViewModel(
    private val userService: UserService,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    fun fetchProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val user = userService.getProfile()
                _profileState.value = ProfileState.Success(user)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Error al cargar perfil")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                userService.logout()
            } catch (e: Exception) {
                // Ignore error on logout but clear session anyway
            } finally {
                sessionManager.clearAuthToken()
                _profileState.value = ProfileState.LogoutSuccess
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
