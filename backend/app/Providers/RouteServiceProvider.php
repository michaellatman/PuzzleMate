<?php

namespace App\Providers;

use App\Puzzle;
use App\User;
use Illuminate\Foundation\Support\Providers\RouteServiceProvider as ServiceProvider;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Route;

class RouteServiceProvider extends ServiceProvider
{
    /**
     * This namespace is applied to your controller routes.
     *
     * In addition, it is set as the URL generator's root namespace.
     *
     * @var string
     */
    protected $namespace = 'App\Http\Controllers';

    /**
     * Define your route model bindings, pattern filters, etc.
     *
     * @return void
     */
    public function boot()
    {
        //

        parent::boot();
        $request = request();
        Route::bind('user', function ($value) {
            return User::where('id', $this->resolveRealUserID($value))->first() ?? abort(404, "User not found!");
        });

        Route::bind('puzzle', function ($value) use ($request) {
            $user_id = $request->route()->parameter("user")["id"];
            return Puzzle::whereId($value)->where("user_id",$user_id)->first() ?? abort(404, "Puzzle not found!");
        });
    }


    private function resolveRealUserID($id){
        if(is_null(auth()->user()) && auth("api")->user()){
            auth()->login(auth("api")->user());
        }

        $finalID = $id;
        if($id == "me"){
            if(request()->user() == null){
                abort(403, "You are not logged in!");
            }
            $finalID = request()->user()->id;
        }
        //request()->add("test");
        return $finalID;
    }

    /**
     * Define the routes for the application.
     *
     * @return void
     */
    public function map()
    {
        $this->mapApiRoutes();

        $this->mapWebRoutes();

        //
    }

    /**
     * Define the "web" routes for the application.
     *
     * These routes all receive session state, CSRF protection, etc.
     *
     * @return void
     */
    protected function mapWebRoutes()
    {
        Route::middleware('web')
             ->namespace($this->namespace)
             ->group(base_path('routes/web.php'));
    }

    /**
     * Define the "api" routes for the application.
     *
     * These routes are typically stateless.
     *
     * @return void
     */
    protected function mapApiRoutes()
    {
        Route::prefix('api')
             ->middleware('api')
             ->namespace($this->namespace)
             ->group(base_path('routes/api.php'));
    }
}
