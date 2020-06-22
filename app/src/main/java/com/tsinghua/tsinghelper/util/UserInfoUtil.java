package com.tsinghua.tsinghelper.util;

import android.content.SharedPreferences;

import com.tsinghua.tsinghelper.dtos.UserDTO;

import java.util.HashMap;

public class UserInfoUtil {

    public static UserDTO me;

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

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String GRADE = "grade";
    public static final String PHONE = "phone";
    public static final String STATE = "state";
    public static final String AVATAR = "avatar";
    public static final String WECHAT = "wechat";
    public static final String REALNAME = "realname";
    public static final String USERNAME = "username";
    public static final String DORMITORY = "dormitory";
    public static final String FOLLOWERS = "followers";
    public static final String FOLLOWINGS = "followings";
    public static final String SIGNATURE = "signature";
    public static final String DEPARTMENT = "department";
    public static final String DOING_TASKS = "doing_tasks";
    public static final String FAILED_TASKS = "failed_tasks";
    public static final String REWARDED_TASKS = "rewarded_tasks";
    public static final String MODERATING_TASKS = "moderating_tasks";

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

    public static boolean isLoggedIn() {
        return !UserInfoUtil.getPref("loggedIn", "").isEmpty();
    }
}
