package cn.yue.base.common.activity;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import cn.yue.base.common.utils.Utils;
import cn.yue.base.common.utils.code.ProcessUtils;
import cn.yue.base.common.utils.debug.LogUtils;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class CommonApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (null != getPackageName() && getPackageName().equals(
                ProcessUtils.getProcessName(this, android.os.Process.myPid()))) {
            //只有进程名和包名一样 才执行初始化操作
            initUtils();
        } else {
            LogUtils.e("其他进程启动,不做初始化操作:" + android.os.Process.myPid());
        }
    }

    private void initUtils() {
        Utils.init(this);
        initPhotoError();
        init();
    }

    protected abstract void init();

    /**
     *  android 7.0系统解决拍照的问题
     */
    private void initPhotoError() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }
}
