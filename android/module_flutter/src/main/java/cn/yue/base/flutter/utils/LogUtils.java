package cn.yue.base.flutter.utils;

import android.util.Log;

public final class LogUtils {

    private static final String TAG = "FLUTTER";
    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void v(final Object... contents) {
        log(V, TAG, contents);
    }

    public static void d(final Object... contents) {
        log(D, TAG, contents);
    }

    public static void i(final Object... contents) {
        log(I, TAG, contents);
    }

    public static void w(final Object... contents) {
        log(W, TAG, contents);
    }

    public static void e(final Object... contents) {
        log(E, TAG, contents);
    }

    public static void a(final Object... contents) {
        log(A, TAG, contents);
    }


    private static void log(int level, String tag, Object... contents) {
        try {
            StringBuilder builder = new StringBuilder();
            for (Object content : contents) {
                builder.append(content.toString());
            }
            String msg = builder.toString();
            switch (level) {
                case V:
                    Log.v(tag, msg);
                    break;
                case W:
                    Log.w(tag, msg);
                    break;
                case I:
                    Log.i(tag, msg);
                    break;
                case E:
                    Log.e(tag, msg);
                    break;
                case D:
                    Log.d(tag, msg);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
