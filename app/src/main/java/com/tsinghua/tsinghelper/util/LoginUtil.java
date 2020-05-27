package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginUtil {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = UserInfoUtil.getUserInfoSharedPreferences(context);
        String username = sharedPreferences.getString("username", "");
        if (username.isEmpty()) {
            // not logged in yet
            return false;
        }

        // TODO: check validity of the user id
        return true;
    }

    public static void recordUserInfo(Context context, HashMap<String, String> params) {
        SharedPreferences sharedPreferences = UserInfoUtil.getUserInfoSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : params.keySet()) {
            editor.putString(key, params.get(key));
        }
        editor.apply();
    }

    public static void clearUserInfo(Context context) {
//        SharedPreferences sharedPreferences =
//                context.getSharedPreferences(UserInfoUtil.USER_INFO_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = UserInfoUtil.getUserInfoSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
