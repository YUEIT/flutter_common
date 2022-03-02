package cn.yue.base.common;

import android.os.Build;
import android.os.Environment;

import java.io.File;

import cn.yue.base.common.utils.Utils;

/**
 * Description :
 * Created by yue on 2018/11/12
 */
public class Constant {

    private Constant() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final String COMMON_NAME = "MyShop";

    public static String getExternalStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return Environment.getExternalStorageDirectory() + File.separator + COMMON_NAME;
        } else {
            File filesDir = Utils.getApp().getExternalFilesDir("");
            if (filesDir == null) {
                return Utils.getApp().getFilesDir() + File.separator + COMMON_NAME;
            }
            return filesDir.getPath() + File.separator + COMMON_NAME;
        }
    }

    public static String getImagePath() {
        return getExternalStorage() + File.separator + "image" + File.separator;
    }

    public static String getAudioPath() {
        return getExternalStorage() + File.separator + "audio" + File.separator;
    }

    public static String getCachePath() {
        return getExternalStorage() + File.separator + "cache" + File.separator;
    }
}
