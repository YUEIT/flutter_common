
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/widget/common_dialog.dart';
import 'package:flutter_common/common/widget/loading_dialog.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';

/// 带loading框的回调
class LoadingHttpCallBack extends LifecycleHttpCallBack {
  LoadingHttpCallBack({
    required this.context,
    BindLifecycleObserver? lifecycleObserver,
    RequestStartCallback? startCallback,
    RequestSuccessCallback? successCallback,
    RequestFailureCallback? failureCallback,
    RequestEndCallback? endCallback})
      : super(
      lifecycleObserver: lifecycleObserver,
      startCallback: startCallback,
      successCallback: successCallback,
      failureCallback: failureCallback,
      endCallback: endCallback);

  BuildContext context;

  @override
  Future onStart() async {
    _showLoadingDialog();
    super.onStart();
  }

  @override
  Future onEnd() async {
    _dismissLoadingDialog();
    super.onEnd();
  }

  LoadingDialog? loadingDialog;

  Future<void> _showLoadingDialog() async {
    await showGlobalDialog(
        context: context,
        barrierDismissible: true,
        builder: (BuildContext context, int globalKey) {
          return loadingDialog = LoadingDialog(context, globalKey: globalKey);
        });
  }

  Future<void> _dismissLoadingDialog() async {
    loadingDialog?.dismiss();
  }
}

typedef Dispose = bool Function();

///生命周期响应的回调，解决内存溢出
class LifecycleHttpCallBack extends HttpCallBack {
  LifecycleHttpCallBack({
    this.lifecycleObserver,
    RequestStartCallback? startCallback,
    RequestSuccessCallback? successCallback,
    RequestFailureCallback? failureCallback,
    RequestEndCallback? endCallback})
      : super(
      startCallback: startCallback,
      successCallback: successCallback,
      failureCallback: failureCallback,
      endCallback: endCallback);

  BindLifecycleObserver? lifecycleObserver;

  bool unBound() {
    return lifecycleObserver != null && lifecycleObserver!.unBound();
  }

  @override
  Future onStart() async {
    if (!unBound()) {
      super.onStart();
    }
  }


  @override
  Future onSuccess(dynamic response) async {
    if (!unBound()) {
      super.onSuccess(response);
    }
  }

  @override
  Future onFailure(ResultError e) async {
    if (!unBound()) {
      super.onFailure(e);
    }
  }

  @override
  Future onEnd() async {
    if (!unBound()) {
      super.onEnd();
    }
  }

}
