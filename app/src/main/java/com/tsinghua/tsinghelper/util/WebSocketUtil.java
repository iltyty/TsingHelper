package com.tsinghua.tsinghelper.util;

import okhttp3.Request;
import okhttp3.WebSocketListener;

public class WebSocketUtil extends NetworkUtil {

    public static void setWSConnection(WebSocketListener wsl) {
        Request request = new Request.Builder()
                .url(SERVER_WS_URL)
                .build();
        mClient.newWebSocket(request, wsl);
    }
}
