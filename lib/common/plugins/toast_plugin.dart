import 'package:flutter/services.dart';

class ToastPlugin {

  static const _channel = const MethodChannel('plugins.flutter.base.yue.cn/toast');

  /// 提示框短时
  static Future<void> showShort(String msg) async {
    Map<dynamic, dynamic> properties = new Map<dynamic, dynamic>();
    properties["msg"] = msg;
    _channel.invokeMethod<void>('showShort', properties);
  }

  /// 提示框长时
  static Future<void> showLong(String msg) async {
    Map<dynamic, dynamic> properties = new Map<dynamic, dynamic>();
    properties["msg"] = msg;
    return _channel.invokeMethod<void>('showLong', properties);
  }

}