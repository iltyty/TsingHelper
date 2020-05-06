package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginUtil {
    private static final String USER_INFO_FILENAME = "userinfo";

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER_INFO_FILENAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        if (userId.isEmpty()) {
            // not logged in yet
            return false;
        }

        // TODO: check validity of the user id
        return true;
    }
}
