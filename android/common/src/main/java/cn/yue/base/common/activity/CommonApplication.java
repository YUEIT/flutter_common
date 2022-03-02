package cn.yue.base.common.activity;

import android.app.Application;
import android.content.Context;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.common.utils.device.ProcessUtils;
import cn.yue.base.common.utils.Utils;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (null != getPackageName() && getPackageName().equals(
                ProcessUtils.getProcessName(this, android.os.Process.myPid()))) {
            //只有进程名和包名一样 才执行初始化操作
            Utils.init(this);
            init();
        } else {
            LogUtils.e("其他进程启动,不做初始化操作:" + android.os.Process.myPid());
        }
    }

    protected abstract void init();

}
