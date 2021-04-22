// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// RouteWriterGenerator
// **************************************************************************

import 'package:flutter_common/example/debug_page.dart';
import 'package:flutter_common/example/flutter_page.dart';
import 'package:flutter_common/example/scroll_page.dart';
import 'package:flutter_common/example/net_page.dart';
import 'package:flutter_common/example/home/main_page.dart';
import 'package:flutter_common/example/option_page.dart';

class ARouterMap {
  static final Map<String, List<Map<String, dynamic>>> innerRouterMap =
      <String, List<Map<String, dynamic>>>{
    'flutter://example/debugPage': [
      {'clazz': DebugPage}
    ],
    'flutter://example/flutterPage': [
      {'clazz': FlutterPage}
    ],
    'flutter://example/scrollPage': [
      {'clazz': ScrollPage}
    ],
    'flutter://example/netPage': [
      {'clazz': NetPage}
    ],
    'flutter://example/mainPage': [
      {'clazz': MainPage}
    ],
    'flutter://example/optionPage': [
      {'clazz': OptionPage}
    ]
  };
  static dynamic instanceFromClazz(Type clazz, dynamic option) {
    switch (clazz) {
      case DebugPage:
        return new DebugPage(option);
      case FlutterPage:
        return new FlutterPage(option);
      case ScrollPage:
        return new ScrollPage(option);
      case NetPage:
        return new NetPage(option);
      case MainPage:
        return new MainPage(option);
      case OptionPage:
        return new OptionPage(option);
      default:
        return null;
    }
  }
}
