package com.tsinghua.tsinghelper.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    
    private static final String SERVER_URL = "http://192.168.1.105:3000/";
//    private static final String SERVER_URL = "http://123.56.51.235:3000/";
    public static final String USER_PREFIX = SERVER_URL + "users/";
    private static final String TASK_PREFIX = SERVER_URL + "tasks/";
    private static final String PROFILE_PREFIX = SERVER_URL + "profile/";

    public static final String USER_LOGIN = USER_PREFIX + "login/";
    public static final String USER_REGISTER = USER_PREFIX + "register/";
    public static final String USER_MODIFY = USER_PREFIX + "modify/";
    public static final String TASK_ADD = TASK_PREFIX + "add/";
    public static final String TASK_GET = TASK_PREFIX + "get/";
    public static final String TASK_GET_ALL = TASK_PREFIX + "all/";
    public static final String AVATAR_UPLOAD = PROFILE_PREFIX + "avatar/";
    public static final String BACKGROUND_UPLOAD = PROFILE_PREFIX + "background/";

    private static final OkHttpClient mClient = new OkHttpClient();

    // Initiate an asynchronous get request
    public static void get(String url, HashMap<String, String> params, Callback callback) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                httpBuilder.addQueryParameter(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("auth", UserInfoUtil.getPref("auth", ""))
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    // Initiate an asynchronous post request
    public static void post(String url, HashMap<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("auth", UserInfoUtil.getPref("auth", ""))
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    public static void uploadImage(String url, String imagePath, Callback callback) {
        File image = new File(imagePath);
        RequestBody body = RequestBody.create(MediaType.parse("image/png"), image);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, body)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("auth", UserInfoUtil
                        .getUserInfoSharedPreferences()
                        .getString("auth", ""))
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    public static void downloadImage(String url, Callback callback)
            throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("auth", UserInfoUtil
                        .getUserInfoSharedPreferences()
                        .getString("auth", ""))
                .build();
        mClient.newCall(request).enqueue(callback);
    }
}
