import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/top_banner.dart';
import 'package:flutter_common/common/constant/router_path.dart';
import 'package:flutter_common/common/router/navigation/platform_navigator.dart';
import 'package:flutter_common/common/router/router.dart';
import 'package:flutter_common/example/base/example_router_path.dart';

@ARoute(url: ExampleRouterPath.FLUTTER_PAGE)
class FlutterPage extends StatelessWidget {
  ARouteOption option;
  FlutterPage(this.option);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: TopBanner(
        title: "flutter page",
      ),
      body: Center(
        child: Column(
          children: <Widget>[
            Container(
              child: Text("url：${option.urlPattern} \nparams：${option.params}",
                textAlign: TextAlign.left,
              ),
              margin: EdgeInsets.fromLTRB(0, 10, 0, 10),
            ),
            ElevatedButton(
              onPressed: () {
                //导航到新路由
                PlatformNavigator.instance
                    .open(ExampleRouterPath.DEBUG_PAGE, withContainer: true);
              },
              child: Text("调试页面"),
            ),
            ElevatedButton(
              onPressed: () {
                //导航到新路由
                PlatformNavigator.instance
                    .open(ExampleRouterPath.OPTION_PAGE, withContainer: true);
              },
              child: Text("option Page"),
            ),
            ElevatedButton(
              onPressed: () {
                //导航到新路由
                PlatformNavigator.instance.open(ExampleRouterPath.NET_PAGE, withContainer: true);
              },
              child: Text("net page"),
            ),
            ElevatedButton(
              onPressed: () {
                //导航到新路由
                PlatformNavigator.instance.open(ExampleRouterPath.PULL_LIST_PAGE, withContainer: true);
              },
              child: Text("pull list page"),
            ),
            ElevatedButton(
              onPressed: () {
                //导航到新路由
                PlatformNavigator.instance.open(ExampleRouterPath.MAIN_PAGE, withContainer: true);
              },
              child: Text("home page"),
            ),
            ElevatedButton(
              onPressed: () {
                PlatformNavigator.instance.open(ExampleRouterPath.SCROLL_PAGE, withContainer: true);
              },
              child: Text("scroll page"),
            ),
          ],
        ),
      ),
    );
  }

}