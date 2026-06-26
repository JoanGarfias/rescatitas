<?php

namespace App\Http\Controllers;

use App\Http\Requests\RegisterPetPostRequest;
use App\Services\PetService;
use Exception;
use Illuminate\Http\Request;

class PetController extends BaseController
{
    protected PetService $petService;

    public function __construct(PetService $petService)
    {
        $this->petService = $petService;
    }

    public function search(Request $request)
    {
        $nombre = $request->input('nombre');
        $direccion = $request->input('direccion');
        $results = $this->petService->searchPets($nombre, $direccion);

        return response()->json($results);
    }

    public function store(RegisterPetPostRequest $request)
    {

        $data = $request->validated();

        $data['id_usuario'] = $this->currentUserId(); 
        $data['estado_mascota'] = 'perdido';
        $data['estado_publicacion'] = 'activa';
        $data['fecha_registro'] = now();

        try{
            $image = $request->file('imagen');
            $pet = $this->petService->registerPet($data, $image);
        } catch (Exception $e) {
            return response()->json([
                'message' => 'Error al registrar la mascota' . $e->getMessage()
            ], 500);
        }

        return response()->json([
            'message' => 'Mascota registrada exitosamente',
            'pet' => $pet
        ], 201);
    }

    public function recent()
    {
        $pets = $this->petService->getRecentPets();

        return response()->json([
            'pets' => $pets
        ]);
    }

    public function show(int $id)
    {
        $pet = $this->petService->getPetById($id);

        if (!$pet) {
            return response()->json([
                'message' => 'Mascota no encontrada'
            ], 404);
        }

        return response()->json([
            'pet' => $pet
        ]);
    }

    public function getPets(Request $request){

        $filters = $request->only(['nombre_mascota', 'direccion', 'raza', 'genero', 'tipo_mascota']);
        $pets = $this->petService->getPets($filters);

        return response()->json([
            'pets' => $pets
        ]);
    }

    public function destroy(int $id){

        $userId = $this->currentUserId();
        $pet = $this->petService->getPetById($id);
        if (!$pet) {
            return response()->json([
                'message' => 'Mascota no encontrada'
            ], 404);
        }

        $canDelete = $this->petService->destroy($id, $userId);
        if(!$canDelete){
            return response()->json([
                'message' => 'No tienes permisos para eliminar esta mascota'
            ], 403);
        }

        return response()->json([
            'message' => 'Mascota eliminada exitosamente'
        ]);
    }

}
