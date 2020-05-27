package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginUtil {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = UserInfoUtil.getUserInfoSharedPreferences();
        String username = sharedPreferences.getString("username", "");
        if (username.isEmpty()) {
            // not logged in yet
            return false;
        }

        // TODO: check validity of the user id
        return true;
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences sharedPreferences = UserInfoUtil.getUserInfoSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
