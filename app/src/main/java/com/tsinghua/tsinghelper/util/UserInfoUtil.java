package com.tsinghua.tsinghelper.util;

import android.content.SharedPreferences;

import java.util.HashMap;

public class UserInfoUtil {

    public static int USERNAME_MAX_LEN = 20;
    public static int SIGNATURE_MAX_LEN = 40;

    private static UserInfoUtil instance = new UserInfoUtil();
    private SharedPreferences mSharedPreferences;

    private UserInfoUtil() {
    }

    public static UserInfoUtil getInstance() {
        return instance;
    }

    public static void setSharedPreferences(SharedPreferences sp) {
        getInstance().mSharedPreferences = sp;
    }

    public static SharedPreferences getUserInfoSharedPreferences() {
        return getInstance().mSharedPreferences;
    }

    public static void putPref(String key, String value) {
        SharedPreferences sharedPreferences = getInstance().mSharedPreferences;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putPrefs(HashMap<String, String> prefs) {
        SharedPreferences sharedPreferences = getInstance().mSharedPreferences;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : prefs.keySet()) {
            editor.putString(key, prefs.get(key));
        }
        editor.apply();
    }
}
