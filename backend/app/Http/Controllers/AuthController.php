<?php

// app/Http/Controllers/AuthController.php
namespace App\Http\Controllers;

use App\Http\Requests\RegisterRequest;
use App\Http\Requests\LoginRequest;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    // Registro
    public function register(RegisterRequest $request)
    {
        $data = $request->validated();

        // También debe tener el campo "password_confirmation"
        $user = User::create([
            'nombre'     => $data["nombre"],
            'apellido_paterno' => $data["apellido_paterno"],
            'apellido_materno' => $data["apellido_materno"],
            'telefono'      => $data["telefono"],
            'codigo_postal' => $data["codigo_postal"],
            'colonia'       => $data["colonia"],
            'municipio'     => $data["municipio"],
            'estado'        => $data["estado"],
            'email'    => $data["email"],
            'password' => Hash::make($data["password"]),
        ]);

        $token = $user->createToken($data["device_name"])->plainTextToken;

        return response()->json([
            'user'  => $user,
            'token' => $token,
        ], 201);
    }

    // Login
    public function login(LoginRequest $request)
    {
        $data = $request->validated();

        $user = User::where('email', $data["email"])->first();

        if (! $user || ! Hash::check($data["password"], $user->password)) {
            throw ValidationException::withMessages([
                'email' => ['Las credenciales proporcionadas son incorrectas.'],
            ]);
        }

        $token = $user->createToken($data["device_name"])->plainTextToken;

        return response()->json([
            'user'  => $user,
            'token' => $token,
        ]);
    }

    // Perfil del usuario autenticado
    public function profile(Request $request)
    {
        return response()->json($request->user());
    }

    // Logout — revoca el token actual
    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();

        return response()->json(['message' => 'Sesión cerrada correctamente.']);
    }

    // Logout de todos los dispositivos
    public function logoutAll(Request $request)
    {
        $request->user()->tokens()->delete();

        return response()->json(['message' => 'Sesión cerrada en todos los dispositivos.']);
    }
}