<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('nombre', 128);
            $table->string('apellido_paterno', 128);
            $table->string('apellido_materno', 128)->nullable();
            $table->string('telefono', 10);
            $table->integer('codigo_postal');
            $table->string('colonia', 128);
            $table->string('municipio', 128);
            $table->string('estado', 128);
            $table->date('fecha_registro')->nullable();
            $table->date('fecha_nacimiento')->nullable();
            $table->boolean('confirmada')->default(false);
            $table->integer('nivel')->default(1);
            $table->enum('rol', ['admin', 'usuario'])->default('usuario');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->dropColumn([
                'nombre',
                'apellido_paterno',
                'apellido_materno',
                'telefono',
                'codigo_postal',
                'colonia',
                'municipio',
                'estado',
                'fecha_registro',
                'fecha_nacimiento',
                'confirmada',
                'nivel',
                'rol'
            ]);
        });
    }
};
