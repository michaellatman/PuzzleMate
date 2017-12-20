<?php

namespace App;

use Illuminate\Database\Eloquent\Model;


/**
 * App\Puzzle
 *
 * @property int $id
 * @property string $name
 * @property string $description
 * @property int $user_id
 * @property \Carbon\Carbon|null $created_at
 * @property \Carbon\Carbon|null $updated_at
 * @property-read \App\User $user
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereCreatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereDescription($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereName($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereUpdatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereUserId($value)
 * @mixin \Eloquent
 * @property string $image_path
 * @property-read array $tag_array
 * @property-read array $tag_array_normalized
 * @property-read string $tag_list
 * @property-read string $tag_list_normalized
 * @property-read \Illuminate\Database\Eloquent\Collection|\Cviebrock\EloquentTaggable\Models\Tag[] $tags
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle isNotTagged()
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle isTagged()
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle whereImagePath($value)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle withAllTags($tags)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle withAnyTags($tags)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle withoutAllTags($tags, $includeUntagged = false)
 * @method static \Illuminate\Database\Eloquent\Builder|\App\Puzzle withoutAnyTags($tags, $includeUntagged = false)
 */
use Cviebrock\EloquentTaggable\Taggable;
use Storage;

/**
 * @property mixed id
 */
class Puzzle extends Model
{
    use Taggable;
    //

    protected $fillable = [
        'name', 'description'
    ];

    protected $hidden = [
        'image_path'
    ];

    public function user()
    {
        return $this->belongsTo('App\User');
    }

    public function toArray() {
        $this->image_url = asset(Storage::url($this->image_path));
        return parent::toArray();
    }
}
