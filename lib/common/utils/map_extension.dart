extension MapExtendsion<K, V> on Map<K, V> {

  String getString(K key) {
    V? value = this[key];
    return value?.toString() ?? "";
  }

  String? getNullString(K key) {
    V? value = this[key];
    return value?.toString();
  }

  int getInt(K key) {
    V? value = this[key];
    if (value == null) {
      return 0;
    } else {
      if (value is int) {
        return value;
      } if (value is double) {
        return value.toInt();
      } else if (value is String) {
        return int.parse(value);
      } else {
        return 0;
      }
    }
  }

  int? getNullInt(K key) {
    V? value = this[key];
    if (value == null) {
      return null;
    } else {
      if (value is int) {
        return value;
      } if (value is double) {
        return value.toInt();
      } else if (value is String) {
        return int.parse(value);
      } else {
        return 0;
      }
    }
  }

  double getDouble(K key) {
    V? value = this[key];
    if (value == null) {
      return 0;
    } else {
      if (value is int) {
        return value.toDouble();
      } if (value is double) {
        return value;
      } else if (value is String) {
        return double.parse(value);
      } else {
        return 0;
      }
    }
  }

  double? getNullDouble(K key) {
    V? value = this[key];
    if (value == null) {
      return null;
    } else {
      if (value is int) {
        return value.toDouble();
      } if (value is double) {
        return value;
      } else if (value is String) {
        return double.parse(value);
      } else {
        return 0;
      }
    }
  }

  bool getBool(K key) {
    V? value = this[key];
    if (value == null) {
      return false;
    }
    if (value is bool) {
      return value;
    }
    return false;
  }

  bool? getNullBool(K key) {
    V? value = this[key];
    if (value is bool) {
      return value;
    }
    return null;
  }
}