package cn.yue.base.middle.init;

import android.app.Application;
import android.content.Context;

import cn.yue.base.middle.module.IBaseModule;
import cn.yue.base.middle.router.FRouter;

public class BaseModuleService implements IBaseModule {

    @Override
    public void init(Context context) {
        FRouter.init((Application) context);
        BaseUrlAddress.setDebug(InitConstant.isDebug());
        NotificationConfig.initChannel();
    }
}
