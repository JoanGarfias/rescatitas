package com.example.rescatitas.Classes

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val prefs: SharedPreferences) {

    // Sobrecarga de constructor: permite inicializar con Context o directamente con SharedPreferences
    constructor(context: Context) : this(
        context.getSharedPreferences("rescatitas_prefs", Context.MODE_PRIVATE)
    )

    companion object {
        const val USER_TOKEN = "user_token"
        const val FAVORITE_PETS = "favorite_pets"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

    fun toggleFavoritePet(petId: Int) {
        val favorites = getFavoritePets().toMutableSet()
        val idString = petId.toString()
        if (favorites.contains(idString)) {
            favorites.remove(idString)
        } else {
            favorites.add(idString)
        }
        prefs.edit().putStringSet(FAVORITE_PETS, favorites).apply()
    }

    fun getFavoritePets(): Set<String> {
        return prefs.getStringSet(FAVORITE_PETS, emptySet()) ?: emptySet()
    }

    fun isPetFavorite(petId: Int): Boolean {
        return getFavoritePets().contains(petId.toString())
    }
}
