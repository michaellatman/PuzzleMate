<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\PuzzleStats
 *
 * @property int $user_id
 * @property int $puzzle_id
 * @property string $progress
 * @method static \Illuminate\Database\Eloquent\Builder|\App\PuzzleStats whereProgress($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\PuzzleStats wherePuzzleId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\PuzzleStats whereUserId($value)
 * @mixin \Eloquent
 * @property \Carbon\Carbon|null $created_at
 * @property \Carbon\Carbon|null $updated_at
 * @method static \Illuminate\Database\Eloquent\Builder|\App\PuzzleStats whereCreatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\PuzzleStats whereUpdatedAt($value)
 * @property int $id
 * @property-read \App\Puzzle $puzzle
 * @property-read \App\User $user
 * @method static \Illuminate\Database\Eloquent\Builder|\App\PuzzleStats whereId($value)
 */
class PuzzleStats extends Model
{
    //
    protected $table = 'puzzle_stats';

    public function user()
    {
        return $this->hasOne('App\User');
    }

    public function puzzle()
    {
        return $this->hasOne('App\Puzzle');
    }

}
