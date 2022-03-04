import 'package:analyzer/dart/element/element.dart';
import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';

import 'router_collector.dart';
import 'router.dart';
import 'router_writer.dart';

class RouterWriterGenerator extends GeneratorForAnnotation<ARouteRoot> {
  Collector collector() {
    return RouterGenerator.collector;
  }

  @override
  dynamic generateForAnnotatedElement(
      Element element, ConstantReader annotation, BuildStep buildStep) {
    return Writer(collector()).write();
  }
}

class RouterGenerator extends GeneratorForAnnotation<ARoute> {
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
