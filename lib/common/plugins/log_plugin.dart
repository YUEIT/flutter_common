import 'package:flutter/services.dart';
import 'package:flutter_common/common/constant/debug.dart';

class LogPlugin {

  static const TAG = "flutter";

  static const _channel =
  MethodChannel('plugins.flutter.base.yue.cn/log');

  static Future<void> log(String msg) async {
    logInfo(tag: TAG, msg: msg);
  }

  static Future<void> logInfo({String? tag, String? msg}) async {
    if (!Debug.debugLog()) return;
    Map<dynamic, dynamic> properties = new Map<dynamic, dynamic>();
    properties["tag"] = tag;
    properties["msg"] = msg;
    _channel.invokeMethod('logInfo', properties);
  }

  static Future<void> logError({String? tag, String? msg}) async {
    if (!Debug.debugLog()) return;
    Map<dynamic, dynamic> properties = new Map<dynamic, dynamic>();
    properties["tag"] = tag;
    properties["msg"] = msg;
    _channel.invokeMethod('logError', properties);
  }
}
