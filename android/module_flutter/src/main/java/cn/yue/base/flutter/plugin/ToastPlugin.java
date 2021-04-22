package cn.yue.base.flutter.plugin;

import androidx.annotation.NonNull;

import cn.yue.base.common.utils.debug.ToastUtils;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class ToastPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private MethodChannel channel;
    private static final String NAME = "plugins.flutter.base.yue.cn/toast";
    private static ToastPlugin constantPlugin;

    public static ToastPlugin instance() {
        if (constantPlugin == null) {
            constantPlugin = new ToastPlugin();
        }
        return constantPlugin;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        ToastPlugin instance = ToastPlugin.instance();
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
        String msg = call.argument("msg");
        switch (call.method) {
            case "showShort":
                ToastUtils.showShortToast(msg);
                result.success(true);
                break;
            case "showShortSafe" :
                ToastUtils.showShortToastSafe(msg);
                result.success(true);
                break;
            case "showLong" :
                ToastUtils.showLongToast(msg);
                result.success(true);
                break;
            case "showLongSafe" :
                ToastUtils.showLongToastSafe(msg);
                result.success(true);
                break;
            default:
                result.notImplemented();
        }
    }


}