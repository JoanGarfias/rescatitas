<?php

namespace Database\Factories;

use App\Models\BitacoraAportacion;
use App\Models\Comentario;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends Factory<BitacoraAportacion>
 */
class BitacoraAportacionFactory extends Factory
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
            'id_comentario' => Comentario::factory(),
            'tipo_aportacion' => fake()->randomElement(['comentario', 'rescate', 'publicacion']),
            'nota' => fake()->sentence(),
            'puntos' => fake()->randomFloat(2, 0, 100),
            'fecha_aportacion' => fake()->date(),
        ];
    }
}
