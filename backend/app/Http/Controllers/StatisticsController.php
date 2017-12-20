<?php

namespace App\Http\Controllers;

use App\Puzzle;
use App\PuzzleStats;
use App\User;
use Illuminate\Http\Request;

class StatisticsController extends Controller
{
    //

    function index(User $user) {
        if(request()->user() != null && request()->user()->id == $user->id){

            $completedPuzzles = PuzzleStats::where("user_id",request()->user()->id)->where("progress","completed")->get();
            $numOfCompleted = $completedPuzzles->count();

            $startedPuzzles = PuzzleStats::where("user_id",request()->user()->id)->where("progress","started")->get();
            $numOfStated = $startedPuzzles->count();

            return response()->json(["data"=>array("completed"=>$numOfCompleted,"started"=>$numOfStated)], 200);
        }
        else{
            return response()->json(["errors"=>["user_id"=>"User not authorized to view this resource"]], 403);
        }
    }

    function startPuzzle(User $user, $puzzle_id) {
        if(request()->user() != null && request()->user()->id == $user->id){
            $puzzle = Puzzle::where("id",$puzzle_id)->first();
            if($puzzle!=null) {
                return $this->setProgress($user, $puzzle, "started");
            }
        }
        else{
            return response()->json(["errors"=>["user_id"=>"User not authorized to view this resource"]], 403);
        }
    }


    function completePuzzle(User $user, $puzzle_id) {
        if(request()->user() != null && request()->user()->id == $user->id){
            $puzzle = Puzzle::where("id",$puzzle_id)->first();
            if($puzzle!=null) {
                return $this->setProgress($user,$puzzle,"completed");
            }
        }
        else{
            return response()->json(["errors"=>["user_id"=>"User not authorized to view this resource"]], 403);
        }
    }

    function setProgress(User $user, Puzzle $puzzle, $progress){
        $existingRecord = PuzzleStats::where("puzzle_id",$puzzle->id)->where("user_id",$user->id)->first();
        if($existingRecord == null){
            $existingRecord = new PuzzleStats();
            $existingRecord->puzzle_id = $puzzle->id;
            $existingRecord->user_id = $user->id;
            $existingRecord->progress = $progress;
        }
        else{
            if($existingRecord->progress != "completed")
            {
                $existingRecord->progress = $progress;
            }
        }
        $existingRecord->save();
        return response()->json(["data"=>$existingRecord], 200);
    }

}
