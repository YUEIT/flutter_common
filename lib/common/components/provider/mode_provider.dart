import 'package:flutter/cupertino.dart';
import 'package:flutter_common/common/components/vm/base_view_model.dart';

/// viewModel的状态保存
class ModeProvider extends InheritedWidget {

  final BaseViewModel viewModel;

  ModeProvider({Key? key, required this.viewModel, required Widget child}) : super(key: key, child: child);

  @override
  bool updateShouldNotify(ModeProvider oldWidget) {
    return viewModel != oldWidget.viewModel;
  }

  static T of<T>(BuildContext context) {
    var mode = context.dependOnInheritedWidgetOfExactType<ModeProvider>()?.viewModel;
    if (mode is T) {
      return mode as T;
    } else {
      throw Exception("no find!");
    }
  }
}