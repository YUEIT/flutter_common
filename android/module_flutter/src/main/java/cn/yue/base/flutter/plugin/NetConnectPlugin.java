package cn.yue.base.flutter.plugin;

import androidx.annotation.NonNull;

import cn.yue.base.common.utils.Utils;
import cn.yue.base.common.utils.device.NetworkUtils;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;


public class NetConnectPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private MethodChannel channel;
    private static final String NAME = "plugins.flutter.base.yue.cn/net_connect";
    private static NetConnectPlugin constantPlugin;

    public static NetConnectPlugin instance() {
        if (constantPlugin == null) {
            constantPlugin = new NetConnectPlugin();
        }
        return constantPlugin;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        NetConnectPlugin instance = NetConnectPlugin.instance();
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
        switch (call.method) {
            case "isNetConnect":
                result.success(NetworkUtils.isConnected());
                break;
            case "isWifi":
                result.success(NetworkUtils.isWifiConnected());
                break;
            case "isMobile" :
                result.success(NetworkUtils.isMobileData());
                break;
            default:
                result.notImplemented();
        }
    }

}