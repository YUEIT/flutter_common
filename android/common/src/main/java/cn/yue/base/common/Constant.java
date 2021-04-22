package cn.yue.base.common;

import android.os.Environment;

import java.io.File;

/**
 * Description :
 * Created by yue on 2018/11/12
 */
public class Constant {

    private Constant() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /*公用的名称*/
    public static final String COMMON_NAME = "MyShop";
    public static final String SDCARD_NAME = Environment.getExternalStorageDirectory() + File.separator + COMMON_NAME;
    public static final String IMAGE_PATH = SDCARD_NAME + File.separator + "image" + File.separator;
    public static final String AUDIO_PATH = SDCARD_NAME + File.separator + "audio" + File.separator;
    public static final String CACHE_PATH = SDCARD_NAME + File.separator + "cache" + File.separator;
}
