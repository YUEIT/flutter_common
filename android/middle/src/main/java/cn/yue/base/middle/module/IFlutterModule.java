package cn.yue.base.middle.module;

import cn.yue.base.middle.module.manager.IModuleService;
import cn.yue.base.middle.router.INavigation;

public interface IFlutterModule extends IModuleService {

    INavigation getFlutterRouter();
}
