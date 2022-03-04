import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';

import 'common/router/router_generator.dart';


Builder routeBuilder(BuilderOptions options) => LibraryBuilder(RouterGenerator(),
    generatedExtension: '.internal_invalid.dart');

Builder routeWriteBuilder(BuilderOptions options) => LibraryBuilder(RouterWriterGenerator(),
        generatedExtension: '.internal.dart');