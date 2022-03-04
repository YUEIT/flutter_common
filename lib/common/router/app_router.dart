
import 'package:flutter/widgets.dart';

import 'router.dart';
import 'router_impl.dart';

@ARouteRoot()
class AppRouter {

  static Widget getPage(String urlString, dynamic params, String? uniqueId) {
    print("$urlString");
    Map<String, dynamic> query = Map();
    if (params != null && params is Map) {
      params.forEach((key, value) {
        query[key] = value;
      });
    }
    return _getPage(urlString, query, uniqueId);
  }

  static Widget _getPage(String urlString, Map<String, dynamic> query, String? uniqueId) {
    ARouterInternalImpl internal = ARouterInternalImpl();
    ARouterResult routeResult = internal.findPage(ARouteOption(urlString, query, uniqueId), ARouteOption(urlString, query, uniqueId));
    if(routeResult.state == ARouterResultState.FOUND) {
      return routeResult.widget;
    }
    return Text('NOT FOUND');
  }
}
