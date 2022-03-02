package cn.yue.base.flutter.plugin;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import cn.yue.base.common.utils.code.SPUtils;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class SharedPreferencesPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private MethodChannel channel;
    private static final String NAME = "plugins.flutter.base.yue.cn/shared_preference";
    private static SharedPreferencesPlugin constantPlugin;

    public static SharedPreferencesPlugin instance() {
        if (constantPlugin == null) {
            constantPlugin = new SharedPreferencesPlugin();
        }
        return constantPlugin;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        SharedPreferencesPlugin instance = SharedPreferencesPlugin.instance();
        instance.channel = new MethodChannel(registrar.messenger(), NAME);
        instance.channel.setMethodCallHandler(instance);
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), NAME);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull MethodChannel.Result result) {
        String name = call.argument("name");
        String key = call.argument("key");
        Object value = call.argument("value");
        if (TextUtils.isEmpty(key) || value == null) return;
        if (name == null) name = "";
        switch (call.method) {
            case "putInt":
                SPUtils.getInstance(name).put(key, (int)value);
                result.success(true);
                break;
            case "putString" :
                SPUtils.getInstance(name).put(key, (String)value);
                result.success(true);
                break;
            case "putBoolean" :
                SPUtils.getInstance(name).put(key, (boolean)value);
                result.success(true);
                break;
            case "getString" :
                result.success(SPUtils.getInstance(name).getString(key));
                break;
            case "getInt" :
                result.success(SPUtils.getInstance(name).getInt(key));
                break;
            case "getBoolean" :
                result.success(SPUtils.getInstance(name).getBoolean(key));
                break;
            default:
                result.notImplemented();
        }

    }

}