// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// RouterWriterGenerator
// **************************************************************************

import "package:flutter_common/example/flutter_page.dart";

import "package:flutter_common/example/option_page.dart";

import "package:flutter_common/example/net_page.dart";

import "package:flutter_common/example/pull_list_page.dart";

import "package:flutter_common/example/scroll_page.dart";

import "package:flutter_common/example/debug_page.dart";

import "package:flutter_common/example/home/main_page.dart";

class ARouterMap {
  static final Map<String, List<Map<String, dynamic>>> innerRouterMap =
      <String, List<Map<String, dynamic>>>{
    'flutter://example/flutterPage': [
      {'clazz': FlutterPage}
    ],
    'flutter://example/optionPage': [
      {'clazz': OptionPage}
    ],
    'flutter://example/netPage': [
      {'clazz': NetPage}
    ],
    'flutter://example/pullListPage': [
      {'clazz': PullListPage}
    ],
    'flutter://example/scrollPage': [
      {'clazz': ScrollPage}
    ],
    'flutter://example/debugPage': [
      {'clazz': DebugPage}
    ],
    'flutter://example/mainPage': [
      {'clazz': MainPage}
    ]
  };

  static dynamic instanceFromClazz(Type clazz, dynamic option) {
    switch (clazz) {
      case FlutterPage:
        return new FlutterPage(option);

      case OptionPage:
        return new OptionPage(option);

      case NetPage:
        return new NetPage(option);

      case PullListPage:
        return new PullListPage(option);

      case ScrollPage:
        return new ScrollPage(option);

      case DebugPage:
        return new DebugPage(option);

      case MainPage:
        return new MainPage(option);

      default:
        return null;
    }
  }
}
