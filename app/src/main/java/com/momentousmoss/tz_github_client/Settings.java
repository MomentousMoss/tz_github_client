package com.momentousmoss.tz_github_client;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String PREFERENCES_FILE_NAME = "SharedPreferences";
    private static final String USER_ACCESS_TOKEN = "userAccessToken";

    private SharedPreferences preferences;

    public Settings(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        return preferences.getString(USER_ACCESS_TOKEN, "");
    }
}
