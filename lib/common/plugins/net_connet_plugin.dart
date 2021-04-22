import 'package:flutter/services.dart';

class NetConnectPlugin {

  static const  _channel = const MethodChannel('plugins.flutter.base.yue.cn/net_connect');

  /// 网络连接
  static Future<bool> isNetConnect() async {
    return await _channel.invokeMethod<bool>("isNetConnect") ?? false;
  }

  static Future<bool> isWifi() async {
    return await _channel.invokeMethod<bool>("isWifi") ?? false;
  }

  static Future<bool> isMobile() async {
    return await _channel.invokeMethod<bool>("isMobile") ?? false;
  }
}