package com.tsinghua.tsinghelper.util;

import android.app.Activity;
import android.util.Log;

public class ErrorHandlingUtil {

    private static final String NETWORK_ERROR_LOG_TAG = "NetworkError";

    public static void handleNetworkError(Activity activity, String prompt, Exception e) {
        logToConsole(e);
        ToastUtil.showToastOnUIThread(activity, prompt);
    }

    public static void logToConsole(Exception e) {
        Log.e(NETWORK_ERROR_LOG_TAG, e.toString());
        e.printStackTrace();
    }
}
