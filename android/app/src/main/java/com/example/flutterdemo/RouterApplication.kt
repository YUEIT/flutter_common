package com.example.flutterdemo

import android.app.Application
import cn.yue.base.flutter.FlutterModuleService
import cn.yue.base.flutter.module.IFlutterModule
import cn.yue.base.flutter.module.ModuleType
import cn.yue.base.flutter.module.manager.ModuleManager
import cn.yue.base.flutter.utils.Utils


/**
 * Description :
 * Created by yue on 2020/4/14
 */
class RouterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        ModuleManager.register(
            ModuleType.MODULE_FLUTTER,
            IFlutterModule::class.java, FlutterModuleService()
        )
        ModuleManager.doInit(this)
    }
}