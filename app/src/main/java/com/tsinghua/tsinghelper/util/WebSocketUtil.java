package com.tsinghua.tsinghelper.util;

import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketUtil extends NetworkUtil {

    private static WebSocket mWebSocket;

    public static void setWSConnection(WebSocketListener wsl) {
        Request request = new Request.Builder()
                .url(SERVER_WS_URL)
                .addHeader("authorization", UserInfoUtil.getPref("auth", ""))
                .build();
        mWebSocket = mClient.newWebSocket(request, wsl);
    }

    public static WebSocket getWebSocket() {
        return mWebSocket;
    }
}
