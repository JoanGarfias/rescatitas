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
        Schema::create('publicacion_imagen', function (Blueprint $table) {
            $table->id('id_publicacion_imagen');
            $table->unsignedBigInteger('id_publicacion');
            $table->foreign('id_publicacion')
                ->references('id_publicacion')->on('publicacion')
                ->onDelete('cascade');
            $table->text('url_imagen');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('publicacion_imagen');
    }
};
