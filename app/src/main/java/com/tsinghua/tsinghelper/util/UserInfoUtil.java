package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoUtil {

    private static final String USER_INFO_FILENAME = "userinfo";

    public static SharedPreferences getUserInfoSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_INFO_FILENAME, Context.MODE_PRIVATE);
    }
}
