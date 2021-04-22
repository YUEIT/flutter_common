import 'package:analyzer/dart/element/element.dart';
import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';

import 'collector.dart';
import 'route.dart';
import 'writer.dart';

class RouteWriterGenerator extends GeneratorForAnnotation<ARouteRoot> {
  Collector collector() {
    return RouteGenerator.collector;
  }

  @override
  dynamic generateForAnnotatedElement(
      Element element, ConstantReader annotation, BuildStep buildStep) {
    return Writer(collector()).write();
  }
}

class RouteGenerator extends GeneratorForAnnotation<ARoute> {
  static Collector collector = Collector();

  @override
  dynamic generateForAnnotatedElement(
      Element element, ConstantReader annotation, BuildStep buildStep) {
    collector.collect(element as ClassElement, annotation, buildStep);
//    String className = element.displayName;
//    String url =annotation.peek('url').stringValue;
//    Map param =annotation.peek('params').mapValue;
    return null;
  }
}
