package com.tsinghua.tsinghelper.util;

import android.content.SharedPreferences;

import java.util.HashMap;

public class UserInfoUtil {

    public static final int USERNAME_MAX_LEN = 20;
    public static final int SIGNATURE_MAX_LEN = 40;
    public static final int PHONE_LEN = 11;
    public static final int REALNAME_LEN = 6;
    public static final int DEPARTMENT_MAX_LEN = 20;
    public static final int GRADE_MAX_LEN = 4;
    public static final int DORMITORY_MAX_LEN = 20;
    public static final int WECHAT_MAX_LEN = 50;
    public static final int EMAIL_MAX_LEN = 50;

    public static String BG_SIGN = "bg_cache_signature";
    public static String AVATAR_SIGN = "avatar_cache_signature";

    public static String ID = "id";
    public static String EMAIL = "email";
    public static String GRADE = "grade";
    public static String PHONE = "phone";
    public static String AVATAR = "avatar";
    public static String WECHAT = "wechat";
    public static String REALNAME = "realname";
    public static String USERNAME = "username";
    public static String DORMITORY = "dormitory";
    public static String SIGNATURE = "signature";
    public static String DEPARTMENT = "department";
    public static String DOING_TASKS = "doing_tasks";
    public static String FAILED_TASKS = "failed_tasks";
    public static String REWARDED_TASKS = "rewarded_tasks";
    public static String MODERATING_TASKS = "moderating_tasks";

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

    public static String getPref(String key, String defValue) {
        SharedPreferences sharedPreferences = getInstance().mSharedPreferences;
        return sharedPreferences.getString(key, defValue);
    }

    public static void clearUserInfo() {
        SharedPreferences sharedPreferences = getInstance().mSharedPreferences;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
