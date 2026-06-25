<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;

class BaseController extends Controller
{
    protected function currentUser(): ?User 
    {
        return request()->user();
    }

    protected function isAuthenticated(): bool 
    {
        return request()->auth()->check();
    }

    protected function currentUserId(): ?int 
    {
        return request()->user()?->id;
    }

    protected function currentUserName(): ?string 
    {
        return request()->user()?->nombre;
    }
}
