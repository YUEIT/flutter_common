package cn.yue.base.flutter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.FlutterBoostDelegate;
import com.idlefish.flutterboost.containers.FlutterBoostActivity;
import com.tekartik.sqflite.SqflitePlugin;

import java.util.HashMap;
import java.util.Map;

import cn.yue.base.common.utils.Utils;
import cn.yue.base.flutter.plugin.CustomPlugin;
import cn.yue.base.flutter.plugin.LogPlugin;
import cn.yue.base.flutter.plugin.NetConnectPlugin;
import cn.yue.base.flutter.plugin.SharedPreferencesPlugin;
import cn.yue.base.flutter.plugin.ToastPlugin;
import cn.yue.base.middle.router.INavigation;
import cn.yue.base.middle.router.PlatformRouter;
import cn.yue.base.middle.module.IFlutterModule;
import io.flutter.FlutterInjector;
import io.flutter.embedding.android.FlutterActivityLaunchConfigs;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.PluginRegistry;
import io.flutter.plugins.pathprovider.PathProviderPlugin;

public class FlutterModuleService implements IFlutterModule {

    @Override
    public void init(Context context) {
        FlutterBoost.instance().setup((Application) context, new FlutterBoostDelegate() {

            @Override
            public void pushNativeRoute(String pageName, Map<String, Object> arguments) {
                PlatformRouter.getInstance().build(pageName)
                        .withMap(arguments)
                        .navigation(FlutterBoost.instance().currentActivity());
            }

            @Override
            public void pushFlutterRoute(String pageName, String uniqueId, Map<String, Object> arguments) {
                if (arguments == null) {
                    arguments = new HashMap<>();
                }
                arguments.put("uniqueId", uniqueId);
                PlatformRouter.getInstance().build(pageName)
                        .withMap(arguments)
                        .navigation(FlutterBoost.instance().currentActivity());
            }

        },engine->{
            PluginRegistry pluginRegistry =  engine.getPlugins();
            pluginRegistry.add(new CustomPlugin());
            pluginRegistry.add(new LogPlugin());
            pluginRegistry.add(new NetConnectPlugin());
            pluginRegistry.add(new SharedPreferencesPlugin());
            pluginRegistry.add(new ToastPlugin());
            pluginRegistry.add(new PathProviderPlugin());
            pluginRegistry.add(new SqflitePlugin());
        } );
    }

    @Override
    public INavigation getFlutterRouter() {
        return FlutterRouter.getInstance();
    }
}