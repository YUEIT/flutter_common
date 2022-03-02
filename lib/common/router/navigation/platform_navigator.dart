import 'package:flutter_boost/flutter_boost.dart';

/// 路由
class PlatformNavigator {

  static PlatformNavigator? _instance;
  static PlatformNavigator get instance => _getInstance();

  static PlatformNavigator _getInstance() {
    if (_instance == null) {
      _instance = PlatformNavigator();
    }
    return _instance!;
  }

  /// 打开页面
  Future<T>? open<T extends Object>(String name,
      {Map<String, dynamic>? arguments, bool withContainer = false}) {
    return BoostNavigator.instance
        .push(name, arguments: arguments, withContainer: withContainer);
  }

  /// 关闭页面
  Future<bool> close<T extends Object>([T? result]) {
    return BoostNavigator.instance.pop(result);
  }
}