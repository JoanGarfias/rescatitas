<?php

namespace App\Services;

use App\Models\Publicacion;
use Illuminate\Http\UploadedFile;
use Exception;

class CommentService
{
    public function createComment(
        int $id_usuario,
        int $id_publicacion,
        string $mensaje
    ){
        $publicacion = Publicacion::find($id_publicacion);
        if (!$publicacion) {
            throw new Exception('Publicación no encontrada');
        }

        $comentario = $publicacion->comentarios()->create([
            'id_usuario' => $id_usuario,
            'contenido' => $mensaje,
        ]);

        return $comentario;   
    }

    public function getCommentsByPublicationId(int $id_publicacion)
    {
        $publicacion = Publicacion::find($id_publicacion);
        if (!$publicacion) {
            throw new Exception('Publicación no encontrada');
        }

        return $publicacion->comentarios()->with('usuario')->get();
    }
}