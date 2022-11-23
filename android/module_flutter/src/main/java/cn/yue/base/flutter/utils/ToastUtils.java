package cn.yue.base.flutter.utils;

import android.widget.Toast;

public final class ToastUtils {

    public static void showShort(final CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void showLong(final CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    private static void show(CharSequence s, int time) {
        Toast.makeText(Utils.getApp(), s, time).show();
    }
}