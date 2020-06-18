package com.tsinghua.tsinghelper.util;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserInfoUtil {

    public static int USERNAME_MAX_LEN = 20;
    public static int SIGNATURE_MAX_LEN = 40;

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

    public static String[] getPublisherTS(int userId) {
        return getPublisherTS(String.valueOf(userId));
    }

    public static String[] getPublisherTS(String userId) {
        String url = HttpUtil.getUserProfileUrlById(userId);
        HashMap<String, String> params = new HashMap<>();
        params.put("avatar_ts", "");
        params.put("bg_ts", "");
        String[] avatarTs = new String[2];
        avatarTs[0] = "";
        avatarTs[1] = "";
        String resStr = HttpUtil.getSync(url, params);
        try {
            JSONObject resJson = new JSONObject(resStr);
            System.out.println(resJson);
            avatarTs[0] = resJson.getString("avatar_ts");
            avatarTs[1] = resJson.getString("bg_ts");
        } catch (JSONException e) {
            Log.e("error", e.toString());
            e.printStackTrace();
        }
        return avatarTs;
    }

}
