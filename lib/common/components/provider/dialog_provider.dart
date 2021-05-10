import 'dart:collection';
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

/// 全局弹框状态保存
class DialogProvider with ChangeNotifier {

  List _dialogStack = [];
  List get dialogStack => _dialogStack;
  int _dismissKey = 0;

  static DialogProvider? _instance;
  static DialogProvider requestProvider(BuildContext context) {
    if (_instance == null) {
        _instance = Provider.of<DialogProvider>(context, listen: false);
        return _instance!;
    }
    return _instance!;
  }

  static DialogProvider? provider() {
    return _instance;
  }

  int nextGlobalKey() {
    var currentKey = Random().nextInt(1000000);
    while (_dialogStack.contains(currentKey)) {
      currentKey = Random().nextInt(1000000);
    }
    return currentKey;
  }

  bool isDialogShowing() {
    return _dialogStack.isNotEmpty;
  }

  void addShowKey(dynamic globalKey) {
    _dialogStack.add(globalKey);
    notifyListeners();
  }

  void removeShowKey(dynamic globalKey) {
    _dialogStack.remove(globalKey);
    notifyListeners();
  }

  void willRemoveLast() {
    _dismissKey = _dialogStack.removeLast();
    notifyListeners();
  }

  void willRemoveAll() {
    while (isDialogShowing()) {
      willRemoveLast();
    }
  }

  LinkedList<CloseListener> closeListeners = LinkedList<CloseListener>();
  bool hasContainer = false;

  void addCloseListener(CloseListener listener) {
    closeListeners.add(listener);
    if (!hasContainer) {
      notifyCloseListener();
      hasContainer = true;
    }
  }

  void notifyCloseListener() {
    addListener(() {
      List<CloseListener> removeAll = [];
      for (CloseListener current in closeListeners) {
        if (current.globalKey == _dismissKey) {
          current.listener();
        }
        if (!_dialogStack.contains(current.globalKey)) {
          removeAll.add(current);
        }
      }
      removeAll.forEach((element) {
        closeListeners.remove(element);
      });
    });
  }

  @override
  void dispose() {
    super.dispose();
    hasContainer = false;
  }
}

class CloseListener extends LinkedListEntry<CloseListener> {
  CloseListener(this.globalKey, this.listener);
  final int globalKey;
  final VoidCallback listener;
}

typedef GlobalWidgetBuilder = Widget Function(BuildContext context, int globalKey);

Future<T?> showGlobalDialog<T>({
  required BuildContext context,
  required GlobalWidgetBuilder builder,
  bool barrierDismissible = true,
  Color? barrierColor = Colors.black54,
  String? barrierLabel,
  bool useSafeArea = true,
  bool useRootNavigator = true,
  RouteSettings? routeSettings,
}) async {
  final provider = DialogProvider.requestProvider(context);
  var currentKey = provider.nextGlobalKey();
  provider.addShowKey(currentKey);
  var value = await showDialog<T>(
      context: context,
      builder: (dialogContext) {
        return builder(dialogContext, currentKey);
      },
      barrierDismissible: barrierDismissible,
      barrierLabel: barrierLabel,
      useSafeArea: useSafeArea,
      useRootNavigator: useRootNavigator,
      routeSettings: routeSettings
  );
  provider.removeShowKey(currentKey);
  return value;
}