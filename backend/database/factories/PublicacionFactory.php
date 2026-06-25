<?php

namespace Database\Factories;

use App\Models\Publicacion;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends Factory<Publicacion>
 */
class PublicacionFactory extends Factory
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
            'nombre_mascota' => fake()->firstName(),
            'tipo_mascota' => fake()->randomElement(['perro', 'gato', 'conejo', 'hamster', 'otro']),
            'telefono' => fake()->phoneNumber(),
            'raza' => fake()->word(),
            'descripcion' => fake()->paragraph(),
            'fecha_desaparicion' => fake()->date(),
            'longitud' => fake()->longitude(-118, -86),
            'latitud' => fake()->latitude(14, 32),
            'estado_mascota' => fake()->randomElement(['perdido', 'encontrado', 'rescatado']),
            'direccion' => fake()->address(),
            'genero' => fake()->randomElement(['macho', 'hembra', 'desconocido']),
            'estado_publicacion' => fake()->randomElement(['activa', 'cerrada', 'archivada']),
            'id_rescatista' => null,
            'imagen' => null,
        ];
    }
}
