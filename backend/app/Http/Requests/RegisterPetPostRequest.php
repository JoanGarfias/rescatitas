<?php

namespace App\Http\Requests;

use Illuminate\Contracts\Validation\ValidationRule;
use Illuminate\Foundation\Http\FormRequest;

class RegisterPetPostRequest extends FormRequest
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
            'nombre_mascota' => 'required|string|max:255',
            'descripcion' => 'required|string|max:1600',
            'direccion' => 'required|string|max:255',
            'raza' => 'nullable|string|max:64',
            'fecha_desaparicion' => 'required|date',
            'lugar_desaparicion' => 'nullable|string|max:255',
            'latitud' => 'nullable|numeric|between:-90,90',
            'telefono' => 'nullable|string|max:20',
            'longitud' => 'nullable|numeric|between:-180,180',
            'tipo_mascota' => 'required|in:perro,gato,conejo,hamster,otro',
            //El estado de la mascota se infiere que es perdido, por eso no lo meto en las reglas
            'genero' => 'required|in:macho,hembra,desconocido',
            //El estado de la publicacion siempre será activa para este caso
        ];
    }

    public function messages(): array
    {
        return [
            'nombre_mascota.required' => 'El nombre de la mascota es obligatorio.',
            'nombre_mascota.string' => 'El nombre de la mascota debe ser una cadena de texto.',
            'nombre_mascota.max' => 'El nombre de la mascota no puede exceder los 255 caracteres.',
            'telefono.string' => 'El teléfono debe ser una cadena de texto.',
            'telefono.max' => 'El teléfono no puede exceder los 20 caracteres.',
            'descripcion.required' => 'La descripción es obligatoria.',
            'descripcion.string' => 'La descripción debe ser una cadena de texto.',
            'descripcion.max' => 'La descripción no puede exceder los 1600 caracteres.',
            'direccion.required' => 'La dirección es obligatoria.',
            'direccion.string' => 'La dirección debe ser una cadena de texto.',
            'direccion.max' => 'La dirección no puede exceder los 255 caracteres.',
            'fecha_desaparicion.required' => 'La fecha de desaparición es obligatoria.',
            'fecha_desaparicion.date' => 'La fecha de desaparición debe ser una fecha válida.',
            'lugar_desaparicion.string' => 'El lugar de desaparición debe ser una cadena de texto.',
            'lugar_desaparicion.max' => 'El lugar de desaparición no puede exceder los 255 caracteres.',
            'latitud.numeric' => 'La latitud debe ser un número.',
            'latitud.between' => 'La latitud debe estar entre -90 y 90 grados.',
            'longitud.numeric' => 'La longitud debe ser un número.',
            'longitud.between' => 'La longitud debe estar entre -180 y 180 grados.',
            'genero.required' => 'El género es obligatorio.',
            'genero.in' => 'El género debe ser macho, hembra o desconocido.'
        ];
    }
}
