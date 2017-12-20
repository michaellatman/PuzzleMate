<?php

namespace App\Http\Controllers;

use App\Puzzle;
use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Collection;
use Storage;


class PuzzleController extends Controller
{
    function __construct()
    {
        $this->middleware('auth:api', ['except' => ['userPuzzleIndex', 'getPuzzle', 'getPuzzleTags', 'globalSearch']]);
        $this->middleware('puzzle.collection.permission', ['only' => ['deletePuzzle', 'createPuzzle', 'setPuzzleTags']]);
    }

    function createPuzzle(Request $request, User $user) {
        $validatedData = $request->validate([
            'name' => 'required|unique:puzzles|max:255|min:4',
            'description' => 'required',
            'image' => 'required|file|image',
        ]);

        $path = $request->file('image')->storePublicly('public/puzzles');

        $puzzle = new Puzzle($validatedData);
        $puzzle->image_path = $path;



        $user->puzzles()->save($puzzle);

        if($request->has("tags")){
            $puzzle->retag($request->get("tags"));
        }

        return response()->json(["message"=>"The puzzle has been created!","data"=>$puzzle]);
    }

    function canModifyPuzzleCollection(User $user){
        if(request()->user() != $user){
            return false;
        }
        return true;
    }

    function deletePuzzle(Request $request, User $user, Puzzle $puzzle) {
        $deleted = $puzzle->delete();
        if(!$deleted){
            return response()->json(["message"=>"Puzzle could not be deleted!","errors"=>["deleted"=>false]], 500);
        }
        Storage::delete($puzzle->image_path);
        return response()->json(["message"=>"The puzzle has been deleted!", "data"=>["deleted"=>true]]);
    }

    function userPuzzleIndex(User $user) {
        return response()->json(['data' => $user->puzzles()->getResults()]);
    }

    function getPuzzle(User $user, Puzzle $puzzle) {
        if(request()->has("include")){
            $includes = new Collection(explode(",",request()->get("include")));
            if($includes->contains("tags")){
                $puzzle->load("tags");
            }
        }

        return response()->json(['data' => $puzzle->toArray()]);
    }

    function getPuzzleTags(User $user, Puzzle $puzzle) {
        return response()->json(['data' => $puzzle->tags->toArray()]);
    }

    function setPuzzleTags(Request $request, User $user, Puzzle $puzzle) {
        $puzzle->retag($request->get("tags"));

        return ['data' => $puzzle->tags->toArray()];
    }

    function globalSearch (Request $request) {
        $query = $request->get("q");
        $tags = implode(",",explode(" ",$query));
        $puzzles = \App\Puzzle::where("name","like","%".$query."%")->orWhere("description","like","%".$query."%")->with("tags")->get();
        $taggedPuzzles = \App\Puzzle::withAnyTags($tags)->with("tags")->get();
        $collection = new Collection($puzzles);
        $collection = $collection->merge(new Collection($taggedPuzzles));
        $collection = $collection->unique("id");

        return response()->json(['data' => $collection]);
    }
}
