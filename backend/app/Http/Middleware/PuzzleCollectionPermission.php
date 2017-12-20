<?php

namespace App\Http\Middleware;

use App\Puzzle;
use Closure;
use Illuminate\Support\Facades\Auth;

class PuzzleCollectionPermission
{

    public function handle($request, Closure $next)
    {
        if (Auth::guard("api")->check()) {
            if(Auth::user()->id==$request->user->id){
                return $next($request);
            }
        }

        return response()->json(["message"=>"You do not have permission to do that!","errors"=>[]], 403);
    }
}
