<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

use App\Models\User;
use App\Models\Publicacion;
use App\Models\PublicacionImagen;
use App\Models\Comentario;
//use App\Models\BitacoraAportacion;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // Crear un usuario de prueba fijo solo si no existe
        User::firstOrCreate(
            ['email' => 'admin@rescatitas.com'],
            [
                'nombre' => 'Admin',
                'apellido_paterno' => 'Rescatista',
                'password' => \Illuminate\Support\Facades\Hash::make('password'),
                'rol' => 'admin',
                'telefono' => '5512345678',
                'codigo_postal' => 54000,
                'colonia' => 'Centro',
                'municipio' => 'Tlalnepantla',
                'estado' => 'Estado de México',
            ]
        );

        // Crear 10 usuarios aleatorios
        $users = User::factory(10)->create();

        // Crear 15 publicaciones relacionadas con usuarios aleatorios
        $publicaciones = Publicacion::factory(15)->recycle($users)->create();

        // Crear imágenes para cada publicación
        $publicaciones->each(function ($publicacion) {
            PublicacionImagen::factory(fake()->numberBetween(1, 3))->create([
                'id_publicacion' => $publicacion->id_publicacion,
            ]);
        });

        // Crear 30 comentarios aleatorios en las publicaciones
        Comentario::factory(30)->recycle($users)->recycle($publicaciones)->create();

        // Crear 20 entradas en la bitácora de aportaciones
        //BitacoraAportacion::factory(20)->recycle($users)->create();
    }
}
