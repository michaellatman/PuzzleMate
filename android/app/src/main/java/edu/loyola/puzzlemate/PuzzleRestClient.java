package edu.loyola.puzzlemate;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import edu.loyola.puzzlemate.utilities.PuzzleDataUtil;

/**
 * Puzzle Rest Client this handles all http activity throughout our app
 *
 * @author Michael Latman, Billy Quintano, Doug Robie
 */

public class PuzzleRestClient {
    private static final String BASE_URL = "http://cs482f17grp1.cs.loyola.edu/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * Get Request with headers
     *
     * @param context
     * @param url
     * @param headers
     * @param params
     * @param responseHandler
     */
    public static void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(url), headers, params, responseHandler);
    }

    /**
     * Get Request
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Post with headers
     * @param context
     * @param url
     * @param headers
     * @param params
     * @param responseHandler
     */
    public static void post(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(url), headers, params, null, responseHandler);
    }

    /**
     * Post json
     * @param context
     * @param url
     * @param jsonParams
     * @param responseHandler
     */
    public static void post(Context context, String url, JSONObject jsonParams, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(context, getAbsoluteUrl(url), entity, "application/json",
                    responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do login
     * @param context
     * @param username
     * @param password
     * @param responseHandler
     */

    public static void doLogin(Context context, String username, String password, AsyncHttpResponseHandler responseHandler) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", username);
            params.put("password", password);
            client.addHeader("Content-Type", "application/json");
            client.addHeader("Accept", "application/json");
            PuzzleRestClient.post(context, "login", params, responseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do logout
     * @param context
     * @param responseHandler
     */
    public static void doLogout(Context context, AsyncHttpResponseHandler responseHandler) {
        JSONObject params = new JSONObject();
        try {
            client.addHeader("Content-Type", "application/json");
            client.addHeader("Accept", "application/json");
            String token = PuzzleDataUtil.getAuthToken(context);
            params.put("api_token", token);
            PuzzleRestClient.post(context, "logout", params, responseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mark Puzzle Started
     * @param context
     * @param puzzleID
     */
    public static void startPuzzle(Context context, String puzzleID) {
        JSONObject params = new JSONObject();
        try {

            String token = PuzzleDataUtil.getAuthToken(context);
            if (token != null) {
                params.put("api_token", token);
                PuzzleRestClient.post(context, "users/me/statistics/start/" + puzzleID, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("StatCollection-Stat", "Done");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("StatCollection-Start", "Failed");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mark Puzzle Complete
     * @param context
     * @param puzzleID
     */
    public static void completePuzzle(Context context, String puzzleID) {
        JSONObject params = new JSONObject();
        try {
            String token = PuzzleDataUtil.getAuthToken(context);
            if (token != null) {
                params.put("api_token", token);
                PuzzleRestClient.post(context, "users/me/statistics/complete/" + puzzleID, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("StatCollection-Complete", "Done");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("StatCollection-Complete", "Fail");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do register
     * @param context
     * @param name
     * @param username
     * @param password
     * @param confirmation
     * @param responseHandler
     */
    public static void doRegister(Context context, String name, String username, String password,
                                  String confirmation, AsyncHttpResponseHandler responseHandler) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("email", username);
            params.put("password", password);
            params.put("password_confirmation", confirmation);
            PuzzleRestClient.post(context, "register", params, responseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param context
     * @param title
     * @param image
     * @param tags
     * @param desc
     * @param responseHandler
     */
    public static void doCreatePuzzle(Context context, String title, File image, String tags,
                                      String desc, AsyncHttpResponseHandler responseHandler) {
        //url params responshandlr
        RequestParams params = new RequestParams();
        params.put("name", title);
        try {
            params.put("image", image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("description", desc);
        params.put("tags", tags);
        client.removeAllHeaders();
        Header[] headers = {
                new BasicHeader("Authorization", "Bearer " + PuzzleDataUtil.getAuthToken(context)),
                new BasicHeader("Accept", "application/json"),
        };


        PuzzleRestClient.post(context, "users/me/puzzles", headers, params, responseHandler);

    }

    /**
     * GetAbsolute url using relative url.
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
