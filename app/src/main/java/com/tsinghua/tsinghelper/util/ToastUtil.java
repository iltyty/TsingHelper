package com.tsinghua.tsinghelper.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastOnUIThread(Activity activity, String text) {
        activity.runOnUiThread(() -> showToast(activity, text));
    }
}
