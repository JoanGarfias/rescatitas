package com.example.rescatitas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Clase base que define un comportamiento comun para todos los ViewModels
abstract class BaseViewModel<T>(initialState: T) : ViewModel() {
    
    // Protegido para que solo las clases derivadas puedan cambiar el estado
    protected val _state = MutableStateFlow<T>(initialState)
    val state: StateFlow<T> = _state

    // Metodo base que podria ser sobrecargado o usado por las clases hijas
    open fun resetToIdle(idleState: T) {
        _state.value = idleState
    }
}
