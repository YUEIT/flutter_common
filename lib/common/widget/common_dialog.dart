import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/provider/dialog_provider.dart';
import 'package:flutter_common/common/utils/object_extension.dart';

class CommonDialog extends StatelessWidget {

  CommonDialog (this.dialogContext,
      { this.title,
        this.content,
        this.leftStr,
        this.leftClick,
        this.rightStr,
        this.rightClick,
        int? globalKey
      }) {
    if (globalKey != null) {
      final provider = DialogProvider.requestProvider(dialogContext);
      provider.addCloseListener(CloseListener(globalKey, () {
        dismiss();
      }));
    }
  }

  final BuildContext dialogContext;
  final String? title;
  final Widget? content;
  final String? leftStr;
  final Function? leftClick;
  final String? rightStr;
  final Function? rightClick;

  @override
  Widget build(BuildContext context) {
    Widget optionWidget;
    if (leftStr.hasValue() && rightStr.hasValue()) {
      optionWidget = Container(
        child: Row(
          children: <Widget>[
            Expanded(
              child: Container(
                child: GestureDetector(
                  child: Text(leftStr ?? "",
                      style: TextStyle(color: Colors.blue, fontSize: 18)),
                  onTap: () {
                    if (leftClick != null) {
                      leftClick!();
                    }
                  },
                ),
                alignment: Alignment.center,
                padding: EdgeInsets.only(top: 10, bottom: 10),
              ),
              flex: 1,
            ),
            Container(
              height: 30,
              child: VerticalDivider(
                color: Colors.black12,
                width: 1,
              ),
            ),
            Expanded(
              child: Container(
                child: GestureDetector(
                    child: Text(rightStr ?? "",
                        style: TextStyle(color: Colors.black26, fontSize: 18)),
                    onTap: () {
                      if (rightClick != null) {
                        rightClick!();
                      }
                    }
                ),
                alignment: Alignment.center,
                padding: EdgeInsets.only(top: 10, bottom: 10),
              ),
              flex: 1,
            )
          ],
        ),
      );
    } else if (leftStr.notValue() && rightStr.notValue()) {
      optionWidget = Container();
    } else {
      optionWidget = Center(
        child: Container(
          child: GestureDetector(
            child: Text(
                leftStr?? rightStr ?? "", style: TextStyle(color: Colors.blue, fontSize: 18)),
            onTap: () {
              if (leftClick != null) {
                leftClick!();
              } else if (rightClick != null) {
                rightClick!();
              }
            },
          ),
          alignment: Alignment.center,
          padding: EdgeInsets.only(top: 10, bottom: 10),
        ),
      );
    }

    Widget dialogChild = IntrinsicWidth(
      stepWidth: 56.0,
      child: ConstrainedBox(
        constraints: const BoxConstraints(minWidth: 280.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Container(
              alignment: Alignment.center,
              margin: EdgeInsets.only(top: 10, bottom: 10),
              child: Text(
                title??"", style: TextStyle(color: Colors.black, fontSize: 20),),
            ),
            Container(
              alignment: Alignment.center,
              margin: EdgeInsets.all(10),
              child: content,
            ),
            Divider(
              color: Colors.black12,
              height: 1,
            ),
            optionWidget
          ],
        ),
      ),
    );
    return Dialog(
      backgroundColor: Colors.transparent,
      child: ClipRRect(
          borderRadius: BorderRadius.circular(10),
          child: Container(
            child: dialogChild,
            color: Colors.white,
          )
      ),
    );
  }

  void dismiss() {
    try {
      Navigator.pop(dialogContext);
    } catch (e) {
      print("$e");
    }
  }
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