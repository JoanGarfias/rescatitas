<?php

namespace App\Services;

use App\Models\Publicacion;
use Illuminate\Http\UploadedFile;

class PetService
{
    public function searchPets(string | null $nombre, string | null $direccion)
    {
        $query = Publicacion::query();
        if($nombre){
            $query->where('nombre_mascota', 'like', '%' . $nombre . '%');
        }
        if($direccion){
            $query->orWhere('direccion', 'like', '%' . $direccion . '%');
        }
        $results = $query->get();

        return $results;
    }

    public function registerPet(array $data, ?UploadedFile $image = null)
    {
        //guardamos en disco local, luego guardamos en R2
        if ($image) {
            $data['imagen'] = FileCloudService::uploadFile($image, uniqid() . '.' . $image->getClientOriginalExtension());
        }
        $pet = Publicacion::create($data);
        return $pet;
    }

    public function getRecentPets()
    {
        return Publicacion::orderBy('created_at', 'desc')->take(6)->get();
    }

    public function getPetById(int $id)
    {
        return Publicacion::find($id);
    }

    public function getPets(array $filters)
    {

        $query = Publicacion::query();

        if (isset($filters['nombre_mascota'])) {
            $query->where('nombre_mascota', 'like', '%' . $filters['nombre_mascota'] . '%');
        }

        if (isset($filters['direccion'])) {
            $query->where('direccion', 'like', '%' . $filters['direccion'] . '%');
        }

        if (isset($filters['raza'])) {
            $query->where('raza', 'like', '%' . $filters['raza'] . '%');
        }

        if (isset($filters['genero'])) {
            $query->where('genero', '=', $filters['genero']);
        }

        return $query->with('usuario:id,nombre')->get();
    }

    public function destroy(int $id)
    {
        $pet = Publicacion::find($id);
        if ($pet) {
            $pet->delete();
            return true;
        }
        return false;
    }
}