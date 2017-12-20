<?php

use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Collection;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::prefix('/users/{user}')->group(function () {

    Route::get('/', "UserController@profilePage");

    Route::get('/puzzles', 'PuzzleController@userPuzzleIndex');


    Route::get('/statistics', 'StatisticsController@index');
    Route::post('/statistics/start/{puzzle_id}', 'StatisticsController@startPuzzle');
    Route::post('/statistics/complete/{puzzle_id}', 'StatisticsController@completePuzzle');

    Route::post('/puzzles', 'PuzzleController@createPuzzle');

    Route::prefix('/puzzles/{puzzle}')->group(function () {
        Route::get('', 'PuzzleController@getPuzzle');
        Route::delete('', 'PuzzleController@deletePuzzle');
        Route::get('/tags', 'PuzzleController@getPuzzleTags');
        Route::put('/tags', 'PuzzleController@setPuzzleTags');
    });

});

Route::get('/puzzles', 'PuzzleController@globalSearch');

Route::post('register', 'Auth\RegisterController@register');
Route::post('login', 'Auth\LoginController@login');
Route::post('logout', 'Auth\LoginController@logout')->middleware("auth:api");