package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showToast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }
}
