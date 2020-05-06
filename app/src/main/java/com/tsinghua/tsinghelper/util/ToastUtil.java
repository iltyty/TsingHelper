package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ToastUtil {
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
