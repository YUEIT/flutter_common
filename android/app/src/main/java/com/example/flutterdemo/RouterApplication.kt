package com.example.flutterdemo

import cn.yue.base.flutter.FlutterModuleService
import cn.yue.base.middle.init.MiddleApplication
import cn.yue.base.middle.module.IFlutterModule
import cn.yue.base.middle.module.ModuleType
import cn.yue.base.middle.module.manager.ModuleManager


/**
 * Description :
 * Created by yue on 2020/4/14
 */
class RouterApplication : MiddleApplication() {
    
    override fun registerModule() {
        ModuleManager.register(ModuleType.MODULE_FLUTTER, IFlutterModule::class.java , FlutterModuleService())
    }
}