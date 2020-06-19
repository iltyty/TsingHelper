package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginUtil {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = UserInfoUtil.getUserInfoSharedPreferences();
        String username = sharedPreferences.getString("username", "");
        // not logged in yet
        return !username.isEmpty();

        // TODO: check validity of the user id
    }

}
