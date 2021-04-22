import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

/// SystemChrome setSystemUIOverlayStyle 是静态方法，且其中通过_latestStyle缓存了最后设置的状态栏样式
/// FlutterActivity create中会默认使用 View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN，statusBar 颜色为0x40000000；
/// onPostResume 中通过PlatformPlugin注册的交互通道获取flutter设置的状态栏样式并设置，所以必须在该方法调用前flutter设置才有效
/// 当通过Flutter-Boost新开一个页面时，如果不设置systemUI就会为默认的；（仅在run(app)中设置是无效的）
class SystemUiChrome {

  void setSystemUIOverlayStyle(SystemUiOverlayStyle style) {
    if (_pendingStyle != null) {
      // The microtask has already been queued; just update the pending value.
      _pendingStyle = style;
      return;
    }
    if (style == _latestStyle) {
      // Trivial success: no microtask has been queued and the given style is
      // already in effect, so no need to queue a microtask.
      return;
    }
    _pendingStyle = style;
    scheduleMicrotask(() {
      assert(_pendingStyle != null);
      if (_pendingStyle != _latestStyle) {
        SystemChannels.platform.invokeMethod<void>(
          'SystemChrome.setSystemUIOverlayStyle',
          _toMap(_pendingStyle!),
        );
        _latestStyle = _pendingStyle;
      }
      _pendingStyle = null;
    });
  }

  SystemUiOverlayStyle? _pendingStyle;

  /// The last style that was set using [SystemChrome.setSystemUIOverlayStyle].
  @visibleForTesting
  SystemUiOverlayStyle? get latestStyle => _latestStyle;
  SystemUiOverlayStyle? _latestStyle;

  Map<String, dynamic> _toMap(SystemUiOverlayStyle style) {
    return <String, dynamic>{
      'systemNavigationBarColor': style.systemNavigationBarColor?.value,
      'systemNavigationBarDividerColor': style.systemNavigationBarDividerColor?.value,
      'statusBarColor': style.statusBarColor?.value,
      'statusBarBrightness': style.statusBarBrightness?.toString(),
      'statusBarIconBrightness': style.statusBarIconBrightness?.toString(),
      'systemNavigationBarIconBrightness': style.systemNavigationBarIconBrightness?.toString(),
    };
  }
}