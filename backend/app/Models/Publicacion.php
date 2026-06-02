<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Publicacion extends Model
{
    use HasFactory;
    protected $table = 'publicacion';

    protected $primaryKey = 'id_publicacion';

    protected $fillable = [
        'id_usuario',
        'nombre_mascota',
        'tipo_mascota',
        'raza',
        'descripcion',
        'fecha_desaparicion',
        'longitud',
        'latitud',
        'estado_mascota',
        'direccion',
        'genero',
        'estado_publicacion',
        'imagen',
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
