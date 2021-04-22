import 'package:flutter/material.dart';

class ColorUtil {

  static Color? parse(String? colorString) {
    if (colorString == null || colorString.isEmpty) {
      return null;
    }
    int color;
    if (colorString[0] == '#') {
      color = int.parse(colorString.substring(1), radix: 16);
    } else {
      color = int.parse(colorString, radix: 16);
    }
    if (colorString.length == 7) {
      // Set the alpha value
      color |= 0x00000000ff000000;
    } else if (colorString.length != 9) {
      return null;
    }
    return Color(color);
  }
}
