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
        Schema::create('bitacora_aportacion', function (Blueprint $table) {
            $table->id('id_bitacora_aportacion');
            $table->unsignedBigInteger('id_usuario')->nullable();
            $table->foreign('id_usuario')
                ->references('id')->on('users')
                ->onDelete('cascade');
            $table->unsignedBigInteger('id_comentario')->nullable();
            $table->foreign('id_comentario')
                ->references('id_comentario')->on('comentario')
                ->onDelete('cascade');
            $table->enum('tipo_aportacion',[
                'comentario',
                'rescate',
                'publicacion',
            ])->default('dinero');
            $table->text('nota');
            $table->decimal('puntos');
            $table->date('fecha_aportacion');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('bitacora_aportacion');
    }
};
