/// Description : 
/// Created by yue on 4/21/21

class NullUtil {

  static bool hasValue(dynamic obj) {
    if (obj == null) {
      return false;
    }
    if (obj is String) {
      return obj.isNotEmpty;
    }
    if (obj is List) {
      return obj.isNotEmpty;
    }
    if (obj is Map) {
      return obj.isNotEmpty;
    }
    return true;
  }

  static bool notValue(dynamic obj) {
    return !hasValue(obj);
  }
}
