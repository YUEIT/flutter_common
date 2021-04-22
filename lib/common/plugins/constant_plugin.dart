import 'package:flutter/services.dart';

class ConstantPlugin {

  static const  _channel = const MethodChannel('plugins.flutter.ypc.com/constant');

  /// 获取编译环境 1：test; 2：uat; 3：release;
  static Future<int?> getBuildEnvironment() {
    return _channel.invokeMethod<int>('getBuildEnvironment');
  }

}