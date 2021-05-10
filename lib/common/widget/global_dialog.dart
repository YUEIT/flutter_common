import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/provider/dialog_provider.dart';

/// Description : 全局弹框
/// Created by yue on 4/26/21

class GlobalDialog extends StatelessWidget {

  final BuildContext dialogContext;
  final WidgetBuilder builder;
  GlobalDialog(this.dialogContext, {
    required this.builder,
    int? globalKey
  }) {
    if (globalKey != null) {
      final provider = DialogProvider.requestProvider(dialogContext);
      provider.addCloseListener(CloseListener(globalKey, () {
        dismiss();
      }));
    }
  }

  @override
  Widget build(BuildContext context) {
    return builder(dialogContext);
  }

  void dismiss() {
    try {
      Navigator.pop(dialogContext);
    } catch (e) {
      print("dialog already close");
    }
  }

}
