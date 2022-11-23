package cn.yue.base.flutter.module;

import cn.yue.base.flutter.module.manager.IModuleService;
import cn.yue.base.flutter.router.INavigation;


public interface IFlutterModule extends IModuleService {

    INavigation getFlutterRouter();
}
