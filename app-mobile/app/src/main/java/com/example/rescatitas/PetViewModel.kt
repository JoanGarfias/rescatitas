package com.example.rescatitas

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatitas.Models.Pet
import com.example.rescatitas.Services.PetService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

sealed class PetState {
    object Idle : PetState()
    object Loading : PetState()
    data class Success(val pets: List<Pet>) : PetState()
    data class SinglePetSuccess(val pet: Pet) : PetState()
    data class Error(val message: String) : PetState()
    data class ReportSuccess(val message: String) : PetState()
}

class PetViewModel(private val petService: PetService) : ViewModel() {
    private val _petState = MutableStateFlow<PetState>(PetState.Idle)
    val petState: StateFlow<PetState> = _petState

    fun fetchRecentPets() {
        viewModelScope.launch {
            _petState.value = PetState.Loading
            try {
                val response = petService.getRecents()
                _petState.value = PetState.Success(response.pets ?: emptyList())
            } catch (e: retrofit2.HttpException) {
                // Aqui atrapamos el error del servidor y tratamos de leer que paso
                val errorBody = e.response()?.errorBody()?.string()
                _petState.value = PetState.Error("Error ${e.code()}: ${errorBody ?: e.message()}")
            } catch (e: Exception) {
                _petState.value = PetState.Error(e.message ?: "Error al cargar mascotas")
            }
        }
    }

    fun fetchPetById(id: Int) {
        viewModelScope.launch {
            _petState.value = PetState.Loading
            try {
                val response = petService.getPetById(id)
                // Usamos safe call ?. y manejamos el caso de que pets sea nulo o esté vacío
                val pet = response.pet
                if (pet != null) {
                    _petState.value = PetState.SinglePetSuccess(pet)
                } else {
                    _petState.value = PetState.Error("Mascota no encontrada")
                }
            } catch (e: retrofit2.HttpException) {
                _petState.value = PetState.Error("Error ${e.code()}: No se pudo cargar el detalle")
            } catch (e: Exception) {
                _petState.value = PetState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun createPetReport(
        context: Context,
        nombre: String,
        tipo: String,
        raza: String,
        descripcion: String,
        fecha: String,
        genero: String,
        direccion: String,
        imageUri: Uri
    ) {
        viewModelScope.launch {
            _petState.value = PetState.Loading
            try {
                // Transformamos la URI de la galeria en un archivo que Retrofit pueda subir
                val file = getFileFromUri(context, imageUri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("imagen", file.name, requestFile)

                val response = petService.createPet(
                    nombre.toRequestBody("text/plain".toMediaTypeOrNull()),
                    tipo.toRequestBody("text/plain".toMediaTypeOrNull()),
                    raza.toRequestBody("text/plain".toMediaTypeOrNull()),
                    descripcion.toRequestBody("text/plain".toMediaTypeOrNull()),
                    fecha.toRequestBody("text/plain".toMediaTypeOrNull()),
                    genero.toRequestBody("text/plain".toMediaTypeOrNull()),
                    direccion.toRequestBody("text/plain".toMediaTypeOrNull()),
                    body
                )
                _petState.value = PetState.ReportSuccess(response.message)
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _petState.value = PetState.Error("Error ${e.code()}: ${errorBody ?: "No se pudo crear el reporte"}")
            } catch (e: Exception) {
                _petState.value = PetState.Error(e.message ?: "Error al enviar el reporte")
            }
        }
    }

    // Funcion para copiar la imagen de la galeria a un archivo temporal y poder subirlo
    private fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    fun resetState() {
        _petState.value = PetState.Idle
    }
}

class PetViewModelFactory(private val petService: PetService) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetViewModel(petService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
