<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class PublicacionImagen extends Model
{
    use HasFactory;

    protected $table = 'publicacion_imagen';

    protected $primaryKey = 'id_publicacion_imagen';

    protected $fillable = [
        'id_publicacion',
        'url_imagen',
    ];

    public function publicacion(){
        return $this->belongsTo(Publicacion::class, 'id_publicacion');
    }
}
