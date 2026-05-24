<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Publicacion extends Model
{
    protected $table = 'publicacion';

    protected $primaryKey = 'id_publicacion';

    protected $fillable = [
        'id_usuario',
        'nombre_mascota',
        'descripcion',
        'fecha_desaparicion',
        'lugar_desaparicion',
        'longitud',
        'latitud',
        'estado_mascota',
        'genero',
        'estado_publicacion',
        'tipo_publicacion',
        'id_rescatista',
    ];

    public $timestamps = true;

    public function usuario(){
        return $this->belongsTo(User::class, 'id_usuario');
    }

    public function comentarios(){
        return $this->hasMany(Comentario::class, 'id_publicacion');
    }

    public function imagenes(){
        return $this->hasMany(PublicacionImagen::class, 'id_publicacion');
    }
}
