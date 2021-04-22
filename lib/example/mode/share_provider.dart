import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class ShareModel with ChangeNotifier {
  Color _color = Colors.black26;
  Color get color => _color;

  void setColor(Color value) {
    _color = value;
    notifyListeners();
  }

}