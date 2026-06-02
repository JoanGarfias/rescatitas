<?php

namespace App\Services;

use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Storage;

use Exception;

class FileCloudService
{

    public static function uploadFile(UploadedFile $file, $filename)
    {
        try{
        $disk = Storage::disk('r2');
        
        // Subir archivo al bucket
        $path = $disk->putFileAs('', $file, $filename);

        // Usamos la URL base definida en el archivo de configuración o en el .env
        $baseUrl = config('filesystems.disks.r2.url');
        
        if ($baseUrl) {
            return rtrim($baseUrl, '/') . '/' . ltrim($path, '/');
        }

        // Si no hay URL base, retornamos el path relativo dentro del bucket
        return $path;
        } catch (Exception $e) {
            // Manejar errores de subida
            throw new Exception('Error al subir el archivo: ' . $e->getMessage());
        }
    }
}