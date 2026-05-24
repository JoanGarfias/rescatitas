<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Comentario extends Model
{
    protected $table = 'comentario';

    protected $primaryKey = 'id_comentario';

    protected $fillable = [
        'id_usuario',
        'id_publicacion',
        'contenido',
    ];

    public $timestamps = true;

    public function usuario(){
        return $this->belongsTo(User::class, 'id_usuario');
    }

    public function publicacion(){
        return $this->belongsTo(Publicacion::class, 'id_publicacion');
    }
}
