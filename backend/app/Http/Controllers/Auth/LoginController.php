<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use App\User;
use Illuminate\Foundation\Auth\AuthenticatesUsers;
use Illuminate\Http\Request;

class LoginController extends Controller
{
    /*
    |--------------------------------------------------------------------------
    | Login Controller
    |--------------------------------------------------------------------------
    |
    | This controller handles authenticating users for the application and
    | redirecting them to your home screen. The controller uses a trait
    | to conveniently provide its functionality to your applications.
    |
    */

    use AuthenticatesUsers {
        login as protected superLogin;
        logout as protected superLogout;
    }

    /**
     * Where to redirect users after login.
     *
     * @var string
     */
    protected $redirectTo = '/home';

    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('guest')->except('logout');
    }

    public function login(Request $request)
    {
        if($request->wantsJson()) {
            $this->validateLogin($request);

            if ($this->attemptLogin($request)) {
                $guardedUser = $this->guard()->user();
                $finalUser = User::where("id", $guardedUser->getAuthIdentifier())->first();
                $finalUser->generateAPIToken();
                return response()->json([
                    'data' => $finalUser->toArray(),
                ]);
            }
            return response()->json([
                "message" => "Your username & password do not match our records. Please try again",
                "errors" => [
                    "email" => "Does not match records",
                    "password" => "Does not match records"
                ]
            ], 401);
        }
        else{
            $this->superLogin($request);
        }
    }

    public function logout(Request $request)
    {
        if($request->wantsJson()) {
            $guardedUser = $this->guard()->user();
            $finalUser = User::find($guardedUser)->first();
            $finalUser->api_token = null;
            $finalUser->save();
            return response()->json([
                'message'=>"Logged Out!",
                'data' => ["is_logged_in"=>false],
            ]);
        }
        else{
            $this->superLogout($request);
        }

    }

}
