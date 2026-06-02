<?php

namespace Database\Factories;

use App\Models\Publicacion;
use App\Models\PublicacionImagen;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends Factory<PublicacionImagen>
 */
class PublicacionImagenFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'id_publicacion' => Publicacion::factory(),
            'url_imagen' => fake()->imageUrl(640, 480, 'animals', true),
        ];
    }
}
