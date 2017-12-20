package edu.loyola.puzzlemate.utilities;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Syncrobits LLC on 11/19/17.
 */

public class PuzzleDataUtil {
    public static void storeAuthToken(Context contex, String token) {
        PreferenceManager.getDefaultSharedPreferences(contex).edit()
                .putString("api_token", token).apply();
    }

    public static void clearAuthToken(Context contex) {
        PreferenceManager.getDefaultSharedPreferences(contex).edit()
                .remove("api_token").apply();
    }

    public static String getAuthToken(Context contex) {
        return PreferenceManager.getDefaultSharedPreferences(contex)
                .getString("api_token", null);
    }

    public static Boolean hasAuthToken(Context contex) {
        return PreferenceManager.getDefaultSharedPreferences(contex)
                .contains("api_token");
    }
}
