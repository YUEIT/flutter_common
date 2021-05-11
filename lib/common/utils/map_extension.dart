import 'package:flutter_common/common/utils/object_extension.dart';

typedef Convert = dynamic Function(dynamic obj);

extension MapExtendsion<K, V> on Map<K, V> {

  static Type typeOf<T>() => T;

  T? get<T>(dynamic key, {Convert? convert}) {
    Object? value = this[key];
    Type type = typeOf<T>();
    if (type == int) {
      return value.intValue() as T;
    } else if(type == double) {
      return value.doubleValue() as T;
    } else if (type == String) {
      return value.stringValue() as T;
    } else if (type == bool) {
      return value.boolValue() as T;
    } else {
      if (value != null) {
        if (convert != null) {
          return convert(value);
        } else {
          return value as T;
        }
      }
      return null;
    }
  }
}