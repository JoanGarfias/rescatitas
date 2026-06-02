<?php

namespace App\Http\Requests;

use Illuminate\Contracts\Validation\ValidationRule;
use Illuminate\Foundation\Http\FormRequest;

class CreateCommentRequest extends FormRequest
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
     **
     * @return array<string, ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'id_publicacion' => 'required|integer|exists:publicacion,id_publicacion',
            'mensaje' => 'required|string|max:255',
        ];
    }

    public function messages(): array
    {
        return [
            'id_publicacion.required' => 'El ID de la publicación es obligatorio.',
            'id_publicacion.integer' => 'El ID de la publicación debe ser un número entero.',
            'id_publicacion.exists' => 'La publicación especificada no existe.',
            'mensaje.required' => 'El mensaje es obligatorio.',
            'mensaje.string' => 'El mensaje debe ser una cadena de texto.',
            'mensaje.max' => 'El mensaje no puede exceder los 255 caracteres.',
        ];
    }


}
