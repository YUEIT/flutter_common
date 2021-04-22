import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/provider/dialog_provider.dart';
import 'package:flutter_common/common/utils/hexc_olor.dart';

class LoadingDialog extends StatelessWidget {

  final BuildContext dialogContext;
  final String message;
  LoadingDialog(this.dialogContext, {
    this.message = "loading...",
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
    return Center(
        child: SizedBox(
          child: Container(
            decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.all(Radius.circular(10))),
            padding: EdgeInsets.all(15),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                CircularProgressIndicator(
                  backgroundColor: Colors.grey[200],
                  valueColor: AlwaysStoppedAnimation(HexColor('FF5F00')),
                ),
                Container(
                  margin: EdgeInsets.only(top: 20),
                  child: Text(
                    message,
                    style: TextStyle(
                        fontSize: 12.0,
                        color: Colors.black26,
                        decoration: TextDecoration.none),
                  ),
                ),
              ],
            ),
          ),
        ));
  }

  void dismiss() {
    try {
      Navigator.pop(dialogContext);
    } catch (e) {
      print("dialog already close");
    }
  }

}
