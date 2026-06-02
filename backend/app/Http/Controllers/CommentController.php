<?php

namespace App\Http\Controllers;

use App\Http\Requests\CreateCommentRequest;
use App\Services\CommentService;
use Exception;

class CommentController extends BaseController
{
    protected CommentService $commentService;

    public function __construct(CommentService $commentService)
    {
        $this->commentService = $commentService;
    }

    public function create(CreateCommentRequest $request){

        $data = $request->validated();

        try {
            $comentario = $this->commentService->createComment(
                $this->currentUserId(),
                $data['id_publicacion'],
                $data['mensaje']
            );
        } catch (Exception $e) {
            return response()->json([
                'message' => 'Error al crear el comentario: ' . $e->getMessage()
            ], 500);
        }

        return response()->json([
            'message' => 'Comentario creado exitosamente',
            'comentario' => $comentario
        ], 201);
    }

    public function getComments(int $id_publicacion)
    {
        try {
            $comentarios = $this->commentService->getCommentsByPublicationId($id_publicacion);
        } catch (Exception $e) {
            return response()->json([
                'message' => 'Error al obtener los comentarios: ' . $e->getMessage()
            ], 500);
        }

        return response()->json([
            'comentarios' => $comentarios
        ]);
    }
}
