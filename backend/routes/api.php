<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

use App\Http\Controllers\AuthController;
use App\Http\Controllers\PetController;
use App\Http\Controllers\CommentController;

// Rutas públicas
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);

// Rutas protegidas
Route::middleware('auth:sanctum')->group(function () {
    Route::get('/perfil', [AuthController::class, 'profile']);
    Route::post('/logout', [AuthController::class, 'logout']);

    // Mascotas / Publicaciones
    Route::get('/mascotas', [PetController::class, 'getPets']);
    Route::get('/mascotas/buscar', [PetController::class, 'search']);
    Route::get('/mascotas/recientes', [PetController::class, 'recent']);
    Route::get('/mascotas/{id}', [PetController::class, 'show']);
    Route::post('/mascotas', [PetController::class, 'store']);
    Route::delete('/mascotas/{id}', [PetController::class, 'destroy']);

    Route::get('/mis-mascotas', [PetController::class, 'getMyPets']);

    // Comentarios
    Route::get('/publicaciones/{id_publicacion}/comentarios', [CommentController::class, 'getComments']);
    Route::post('/publicaciones/comentarios', [CommentController::class, 'create']);
});