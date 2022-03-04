import 'dart:io';

import 'package:liquid_engine/liquid_engine.dart';

import 'router_collector.dart';

class Writer {

  Collector collector;

  Writer(this.collector);

  Future<String> write() async {
    List<String> routerList = <String>[];
    collector.routerMap.forEach((String url, List<Map<String, dynamic>> configList) {
      configList.forEach((element) {
        routerList.add(element["'clazz'"]);
      });
    });

    final context = Context.create();
    context.variables['imports'] = collector.importList;
    context.variables['routerStr'] = collector.routerMap.toString();
    context.variables['routerList'] = routerList;
    File file = new File(Directory.current.path + '/lib/common/router/template.tl');
    String content = await file.readAsString();
    final template = Template.parse(context, Source.fromString(content));
    String tem = await template.render(context);
    print(tem);
    return tem;
  }

}

