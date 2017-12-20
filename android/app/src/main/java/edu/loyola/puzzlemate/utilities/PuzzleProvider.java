package edu.loyola.puzzlemate.utilities;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import edu.loyola.puzzlemate.PuzzleRestClient;
import edu.loyola.puzzlemate.models.Puzzle;

public class PuzzleProvider {

    public static List<Puzzle> queryPuzzles(Context c, String query, ProviderResponse<List<Puzzle>> reponseHandler) {
        final ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
        final ProviderResponse<List<Puzzle>> resp = reponseHandler;
        PuzzleRestClient.get("puzzles?q=" + query, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject body = null;
                try {
                    body = new JSONObject(new String(responseBody));
                    Gson gson = new Gson();
                    Puzzle[] p = gson.fromJson(body.getJSONArray("data").toString(), Puzzle[].class);

                    resp.success(Arrays.asList(p));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return puzzles;
    }

    public static List<Puzzle> fetchPuzzles(Context c, ProviderResponse<List<Puzzle>> reponseHandler) {
        return queryPuzzles(c, "", reponseHandler);
    }

    public static void fetchPuzzle(Context c, int authorId, int puzzleId, ProviderResponse<Puzzle> reponseHandler) {
        final ProviderResponse<Puzzle> resp = reponseHandler;
        String url = "users/" + authorId + "/puzzles/" + puzzleId;
        PuzzleRestClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject body = null;
                try {
                    body = new JSONObject(new String(responseBody));
                    Gson gson = new Gson();
                    Puzzle p = gson.fromJson(body.getJSONObject("data").toString(), Puzzle.class);
                    resp.success(p);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("FETCH FAIL", error.getMessage());
            }
        });
    }
}
