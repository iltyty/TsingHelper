package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginUtil {
    private static final String USER_INFO_FILENAME = "userinfo";

    public static SharedPreferences getUserInfoSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_INFO_FILENAME, Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = getUserInfoSharedPreferences(context);
        String username = sharedPreferences.getString("username", "");
        if (username.isEmpty()) {
            // not logged in yet
            return false;
        }

        // TODO: check validity of the user id
        return true;
    }

    public static void recordUserInfo(Context context, HashMap<String, String> params) {
        SharedPreferences sharedPreferences = getUserInfoSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : params.keySet()) {
            editor.putString(key, params.get(key));
        }
        editor.apply();
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER_INFO_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
