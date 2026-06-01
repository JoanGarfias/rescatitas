<?php

namespace App\Http\Requests;

use Illuminate\Contracts\Validation\ValidationRule;

class RegisterRequest extends ApiFormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'nombre'        => 'required|string|max:255',
            'apellido_paterno' => 'required|string|max:255',
            'apellido_materno' => 'nullable|string|max:255',
            'telefono'      => 'nullable|string|max:20',
            'codigo_postal' => 'nullable|string|max:10',
            'colonia'       => 'nullable|string|max:255',
            'municipio'     => 'nullable|string|max:255',
            'estado'        => 'nullable|string|max:255',
            'fecha_nacimiento' => 'nullable|date',
            'email'       => 'required|email|unique:users',
            'password'    => 'required|string|min:8|confirmed',
            'device_name' => 'required|string',
        ];
    }
}
