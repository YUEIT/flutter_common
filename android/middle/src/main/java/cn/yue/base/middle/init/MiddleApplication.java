package cn.yue.base.middle.init;

import cn.yue.base.common.activity.CommonApplication;
import cn.yue.base.middle.module.IBaseModule;
import cn.yue.base.middle.module.ModuleType;
import cn.yue.base.middle.module.manager.ModuleManager;
import cn.yue.base.middle.router.FRouter;

public abstract class MiddleApplication extends CommonApplication {

    @Override
    protected void init() {
        ModuleManager.register(ModuleType.MODULE_BASE, IBaseModule.class, new BaseModuleService());
        registerModule();
        ModuleManager.doInit(this);
    }

    public abstract void registerModule();
}
