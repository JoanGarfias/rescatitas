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
        Schema::create('publicacion', function (Blueprint $table) {
            $table->id('id_publicacion');
            $table->unsignedBigInteger('id_usuario');
            $table->foreign('id_usuario')
                ->references('id')->on('users')
                ->onDelete('cascade');
            $table->string('nombre_mascota', 128);
            $table->enum('tipo_mascota', ['perro', 'gato', 'conejo', 'hamster', 'otro']);
            $table->string('raza', 64);
            $table->text('descripcion');
            $table->date('fecha_desaparicion');
            $table->decimal('latitud', 10, 7)->nullable();
            $table->string("direccion", 256)->nullable();
            $table->decimal('longitud', 10, 7)->nullable();
            //Pendiente apodos en array (sql)
            $table->enum('estado_mascota', [
                'perdido',
                'encontrado',
                'rescatado',
            ]);
            $table->enum('genero', ['macho', 'hembra', 'desconocido']);
            $table->enum('estado_publicacion', [
                'activa',
                'cerrada',
                'archivada'
            ])->default('activa');
            $table->unsignedBigInteger('id_rescatista')->nullable();
            $table->string('imagen')->nullable();
            $table->foreign('id_rescatista')
                ->references('id')->on('users')
                ->onDelete('cascade');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('publicacion');
    }
};
