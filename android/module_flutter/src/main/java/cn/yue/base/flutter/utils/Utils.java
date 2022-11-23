package cn.yue.base.flutter.utils;

import android.content.Context;

/**
 * Description :
 * Created by yue on 2022/11/23
 */

public class Utils {

    private static Context context;

    public static void init(Context appContext) {
        context = appContext;
    }

    public static Context getApp() {
        return context;
    }
}
