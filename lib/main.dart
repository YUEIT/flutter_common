import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost_app.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_common/common/components/provider/dialog_provider.dart';
import 'package:flutter_common/common/constant/debug.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/router/router.route.dart';
import 'package:flutter_common/common/router/router.route.internal.dart';
import 'package:flutter_common/example/mode/share_provider.dart';
import 'package:flutter_common/common/utils/dimens_extension.dart';
import 'package:provider/provider.dart';

void main() {
  // 错误拦截
  FlutterError.onError = (FlutterErrorDetails details) {
    if (Debug.isDebug()) {
      print("${details.exception} \n ${details.stack}");
    } else {
      if (details.stack != null) {
        Zone.current.handleUncaughtError(details.exception, details.stack!);
      }
    }
  };
  // 错误页面
  ErrorWidget.builder = (FlutterErrorDetails flutterErrorDetails) {
    return Scaffold(
      body: Center(
        child: Text("抱歉，出错了！"),
      ),
    );
  };

  runZonedGuarded(() async {
    runApp(MultiProvider(
      providers: [
        ChangeNotifierProvider.value(
          value: ShareModel(),
        ),
        ChangeNotifierProvider.value(
          value: DialogProvider(),
        ),
      ],
      child: MyApp(),
    ));
  }, (error, stackTrace) async {
    await _reportError(error, stackTrace);
  });

}

Future _reportError(dynamic error, dynamic stackTrace) async {
  if (Debug.isDebug()) {
    print("$error \n $stackTrace");
    return;
  }
  //错误上报
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Map<String, FlutterBoostRouteFactory> builders = Map();
  @override
  void initState() {
    super.initState();
    print("-----------------------------------init flutter--------------------------------------");
    //别删！坑点
    builders['/'] = (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings, pageBuilder: (_, __, ___) => Container());
    };
    ARouterMap.innerRouterMap
        .forEach((String key, List<Map<String, dynamic>> page) {
      builders[key] = (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) => AppRoute.getPage(key, settings.arguments, uniqueId));
      };
    });

    NetUtils.init();
  }

  Route<dynamic>? routeFactory(RouteSettings settings, String uniqueId) {
    FlutterBoostRouteFactory? func = builders[settings.name];
    if (func == null) {
      return null;
    }
    return func(settings, uniqueId);
  }

  @override
  Widget build(BuildContext context) {
    return FlutterBoostApp(routeFactory,
        appBuilder: (home) {
          return _appBuilder(home);
        }
    );

  }

  Widget _appBuilder(Widget home) {
    return MaterialApp(
          debugShowCheckedModeBanner: false,
          home: HomeWidget(home),
          builder: (context, widget) {
            return MediaQuery(
              ///设置文字大小不随系统设置改变
              data: MediaQuery.of(context).copyWith(textScaleFactor: 1.0),
              child: widget ?? Container(),
            );
          },
    );
  }
}

class HomeWidget extends StatelessWidget {

  final Widget child;
  HomeWidget(this.child);

  @override
  Widget build(BuildContext context) {
    ///设计稿尺寸，单位应是pt或dp
    ScreenUtil.init(BoxConstraints(
      maxWidth: MediaQuery.of(context).size.width,
      maxHeight: MediaQuery.of(context).size.height,),
      designSize: Size(DimensExtension.screenWidth, DimensExtension.screenHeight),
      allowFontScaling: false,
    );
    return Container(
      child: child,
    );
  }

}
