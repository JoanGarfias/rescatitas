<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PublicacionImagen extends Model
{
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
