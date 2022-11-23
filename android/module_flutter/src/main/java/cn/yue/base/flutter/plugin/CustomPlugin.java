package cn.yue.base.flutter.plugin;

import android.content.Context;

import androidx.annotation.NonNull;

import cn.yue.base.flutter.utils.LogUtils;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class CustomPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private Context context;
    private MethodChannel channel;
    private static final String NAME = "plugins.flutter.base.yue.cn/custom";

    public CustomPlugin() {}

    public static void registerWith(PluginRegistry.Registrar registrar) {
        CustomPlugin instance = new CustomPlugin();
        instance.channel = new MethodChannel(registrar.messenger(), NAME);
        instance.context = registrar.context();
        instance.channel.setMethodCallHandler(instance);
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), NAME);
        context = binding.getApplicationContext();
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "getDevicesId" :
                result.success(getDevicesId());
                break;
            case "log" :
                String msg = call.argument("msg");
                log(msg);
                result.success(true);
                break;
            default:
                result.notImplemented();
        }
    }

    private String getDevicesId() {
        return "";
    }

    private void log(String msg) {
        LogUtils.i(msg);
    }
}