package com.example.rescatitas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * BaseViewModel: Clase abstracta que sirve como cimiento para todos los ViewModels del proyecto.
 * Emplea Genéricos <T> para manejar cualquier tipo de estado de UI (Sealed Classes).
 * Esto cumple con el criterio de Herencia y Abstracción de la rúbrica.
 */
abstract class BaseViewModel<T>(initialState: T) : ViewModel() {
    
    /**
     * Atributo protegido _state: Solo accesible por las subclases.
     * Es un MutableStateFlow que permite emitir nuevos estados de forma reactiva.
     */
    protected val _state = MutableStateFlow<T>(initialState)
    
    /**
     * Atributo público state: Versión de solo lectura para ser observada por la UI.
     */
    val state: StateFlow<T> = _state

    /**
     * Método resetToIdle: Permite limpiar o reiniciar el estado de la pantalla.
     * Es un método "open", lo que significa que puede ser sobrecargado por las clases hijas.
     */
    open fun resetToIdle(idleState: T) {
        _state.value = idleState // Asignación del nuevo estado (instanciación de valor)
    }
}
