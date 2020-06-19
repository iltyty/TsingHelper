package com.tsinghua.tsinghelper.util;

import okhttp3.OkHttpClient;

class NetworkUtil {
    static final OkHttpClient mClient = new OkHttpClient();
    static final String SERVER_WS_URL = "ws://192.168.1.105:8080/";
    static final String SERVER_HTTP_URL = "http://192.168.1.105:3000/";
    // static final String SERVER_URL = "http://123.56.51.235:3000/";

    // static final String SERVER_URL = "http://47.94.16.255:3000/";
}
