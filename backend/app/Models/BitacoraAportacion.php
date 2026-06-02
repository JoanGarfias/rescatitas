<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class BitacoraAportacion extends Model
{
    use HasFactory;

    protected $table = 'bitacora_aportacion';

    protected $primaryKey = 'id_bitacora_aportacion';

    protected $fillable = [
        'id_usuario',
        'id_comentario',
        'tipo_aportacion',
        'nota',
        'puntos',
        'fecha_aportacion',
    ];

    public $timestamps = false;

    public function usuario()
    {
        return $this->belongsTo(User::class, 'id_usuario');
    }

    public function comentario()
    {
        return $this->belongsTo(Comentario::class, 'id_comentario');
    }
}
