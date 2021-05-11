typedef Block = Function(dynamic obj);

extension ObjetExtension on dynamic? {

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

  int intValue() {
    if (this == null) {
      return 0;
    } else {
      if (this is int) {
        return (this as int);
      } if (this is double) {
        return (this as double).toInt();
      } else if (this is String) {
        return int.parse((this as String));
      } else {
        return 0;
      }
    }
  }

  double doubleValue() {
    if (this == null) {
      return 0;
    } else {
      if (this is int) {
        return (this as int).toDouble();
      } if (this is double) {
        return (this as double);
      } else if (this is String) {
        return double.parse((this as String));
      } else {
        return 0;
      }
    }
  }

  String stringValue() {
    if (this == null) {
      return "";
    } else {
      return this.toString();
    }
  }
}