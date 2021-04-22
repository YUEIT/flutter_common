typedef Block = Function(dynamic obj);

extension ObjetExtension on Object? {

  dynamic let(Block block) {
    if (this != null) {
      block(this);
    }
    return this;
  }

  bool hasValue() {
    if (this == null) {
      return false;
    }
    if (this is String) {
      return (this as String).isNotEmpty;
    }
    if (this is List) {
      return (this as List).isNotEmpty;
    }
    if (this is Map) {
      return (this as Map).isNotEmpty;
    }
    return true;
  }

  bool notValue() {
    return !this.hasValue();
  }

  bool boolValue() {
    if (this == null) {
      return false;
    }
    if (this is bool) {
      return (this as bool);
    }
    return false;
  }
}