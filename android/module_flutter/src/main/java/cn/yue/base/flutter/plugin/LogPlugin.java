package cn.yue.base.flutter.plugin;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.middle.init.InitConstant;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class LogPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private Context context;
    private MethodChannel channel;
    private static final String NAME = "plugins.flutter.base.yue.cn/log";

    public LogPlugin() {}

    public static void registerWith(PluginRegistry.Registrar registrar) {
        LogPlugin instance = new LogPlugin();
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
        String tag = call.argument("tag");
        String msg = call.argument("msg") == null? "" : call.argument("msg");
        switch (call.method) {
            case "logInfo" :
                logInfo(tag, msg);
                break;
            case "logDebug" :
                logDebug(tag, msg);
                break;
            case "logWarn" :
                logWarn(tag, msg);
                break;
            case "logError" :
                logError(tag, msg);
                break;
            default:
                result.notImplemented();
        }
        result.success(true);
    }

    private void logInfo(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            LogUtils.i(msg);
        } else {
            LogUtils.i(tag, msg);
        }
    }

    private void logDebug(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            LogUtils.d(msg);
        } else {
            LogUtils.d(tag, msg);
        }
    }

    private void logWarn(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            LogUtils.w(msg);
        } else {
            LogUtils.w(tag, msg);
        }
    }

    private void logError(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            LogUtils.e(msg);
        } else {
            LogUtils.e(tag, msg);
        }
    }
}