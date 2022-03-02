package cn.yue.base.flutter;

import android.app.Application;
import android.content.Context;
import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.FlutterBoostDelegate;
import com.idlefish.flutterboost.FlutterBoostRouteOptions;
import com.tekartik.sqflite.SqflitePlugin;
import java.util.HashMap;
import java.util.Map;
import cn.yue.base.flutter.plugin.CustomPlugin;
import cn.yue.base.flutter.plugin.LogPlugin;
import cn.yue.base.flutter.plugin.NetConnectPlugin;
import cn.yue.base.flutter.plugin.SharedPreferencesPlugin;
import cn.yue.base.flutter.plugin.ToastPlugin;
import cn.yue.base.middle.router.INavigation;
import cn.yue.base.middle.router.PlatformRouter;
import cn.yue.base.middle.module.IFlutterModule;
import io.flutter.embedding.engine.plugins.PluginRegistry;
import io.flutter.plugins.pathprovider.PathProviderPlugin;

public class FlutterModuleService implements IFlutterModule {

    @Override
    public void init(Context context) {
        FlutterBoost.instance().setup((Application) context, new FlutterBoostDelegate() {

            @Override
            public void pushNativeRoute(FlutterBoostRouteOptions options) {
                PlatformRouter.getInstance().build(options.pageName())
                        .withMap(options.arguments())
                        .navigation(FlutterBoost.instance().currentActivity());
            }

            @Override
            public void pushFlutterRoute(FlutterBoostRouteOptions options) {
                Map<String, Object> arguments = options.arguments();
                if (arguments == null) {
                    arguments = new HashMap<>();
                }
                arguments.put("uniqueId", options.uniqueId());
                PlatformRouter.getInstance().build(options.pageName())
                        .withMap(arguments)
                        .navigation(FlutterBoost.instance().currentActivity());
            }

            @Override
            public boolean popRoute(FlutterBoostRouteOptions options) {
                return false;
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