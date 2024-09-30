package gr.galeos.seniortracker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences = null;

    public static void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    private static void writeToSharedPreferences(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    private static String readFromSharedPreferences(String key) {
        return sharedPreferences.getString(key, null);
    }

    private static void writeToSharedPreferencesBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    private static boolean readFromSharedPreferencesBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static void removeFromSharedPreferences(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public static void clearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    public static void saveSessionId(String token) {
        writeToSharedPreferences(Constants.USER_TOKEN, token);
    }

    public static String retrieveSessionId() {
        return readFromSharedPreferences(Constants.USER_TOKEN);
    }

    public static boolean isSessionIdValid() {
        return retrieveSessionId() != null;
    }

    public static void invalidateSessionId() {
        removeFromSharedPreferences(Constants.USER_TOKEN);
    }

    public static void saveSkipLogin(boolean isSkipped) {
        writeToSharedPreferencesBoolean(Constants.USER_SKIP_LOGIN, isSkipped);
    }

    public static boolean isLoginSkipped() {
        return readFromSharedPreferencesBoolean(Constants.USER_SKIP_LOGIN);
    }
}

