package com.example.rescatitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rescatitas.Classes.SessionManager
import com.example.rescatitas.Services.UserService

class AuthViewModelFactory(
    private val userService: UserService,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userService, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
