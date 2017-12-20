<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Auth\TokenGuard;
use Illuminate\Http\Request;
use Symfony\Component\CssSelector\Parser\Token;

class UserController extends Controller
{
    //
    function __construct()
    {
        $this->middleware('auth:api', ['except' => ['profilePage']]);
    }

    function profilePage (Request $request, User $user) {
        //$puzzle = new \App\Puzzle(["name"=>"Another awesome puzzle", "description"=>"Beautiful pic of something"]);
        //$user->puzzles()->save($puzzle);
        if ($request->has("include")) {
            $fields = new \Illuminate\Support\Collection(explode(",", $request->get("include")));
            if ($fields->contains("puzzles"))
                $user->load("puzzles");
        }

        return ['data' => $user];
    }
}
