<?php

namespace Database\Factories;

use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Str;

/**
 * @extends Factory<User>
 */
class UserFactory extends Factory
{
    /**
     * The current password being used by the factory.
     */
    protected static ?string $password;

    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'nombre' => fake()->firstName(),
            'apellido_paterno' => fake()->lastName(),
            'apellido_materno' => fake()->lastName(),
            'email' => fake()->unique()->safeEmail(),
            'email_verified_at' => now(),
            'password' => static::$password ??= Hash::make('password'),
            'telefono' => substr(fake()->phoneNumber(), 0, 10),
            'codigo_postal' => fake()->numberBetween(10000, 99999),
            'colonia' => fake()->citySuffix(),
            'municipio' => fake()->city(),
            'estado' => fake()->state(),
            'fecha_registro' => fake()->date(),
            'fecha_nacimiento' => fake()->date(),
            'confirmada' => fake()->boolean(),
            'nivel' => fake()->numberBetween(1, 5),
            'rol' => fake()->randomElement(['admin', 'usuario']),
            'remember_token' => Str::random(10),
        ];
    }

    /**
     * Indicate that the model's email address should be unverified.
     */
    public function unverified(): static
    {
        return $this->state(fn (array $attributes) => [
            'email_verified_at' => null,
        ]);
    }
}
