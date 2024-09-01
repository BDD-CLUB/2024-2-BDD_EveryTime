package com.example.toyproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String REFRESH_TOKEN_KEY = "refreshToken";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveAccessToken(Context context, String token) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(ACCESS_TOKEN_KEY, token);
        editor.apply();
    }

    public static void saveRefreshToken(Context context, String token) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(REFRESH_TOKEN_KEY, token);
        editor.apply();
    }

    public static String getAccessTokenKey(Context context) {
        return getPreferences(context).getString(ACCESS_TOKEN_KEY, null);
    }

    public static String getRefreshTokenKey(Context context) {
        return getPreferences(context).getString(REFRESH_TOKEN_KEY, null);
    }

    public static void clearTokens(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.apply();
    }
}
