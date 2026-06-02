<?php

namespace Database\Factories;

use App\Models\Comentario;
use App\Models\Publicacion;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends Factory<Comentario>
 */
class ComentarioFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'id_usuario' => User::factory(),
            'id_publicacion' => Publicacion::factory(),
            'contenido' => fake()->sentence(),
        ];
    }
}
