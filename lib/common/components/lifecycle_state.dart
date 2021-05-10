
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_common/common/components/system_ui_chrome.dart';
import 'package:flutter_common/common/components/vm/base_view_model.dart';
import 'package:flutter_common/common/router/navigation/platform_navigator.dart';
import 'package:flutter_common/common/widget/common_dialog.dart';
import 'package:flutter_common/common/widget/loading_dialog.dart';
import '../constant/debug.dart';
import '../plugins/log_plugin.dart';

/// 生命周期注册
abstract class LifecycleState<T extends StatefulWidget> extends State<T> {

  static const TAG = "LifecycleState";

  LifecycleProvider lifecycleProvider = LifecycleProvider();

  @override
  void initState() {
    super.initState();
    initStatusBar();
    initSubscribe();
    lifecycleProvider.onPageCreate();
    if (Debug.debugDetail()) {
      LogPlugin.logInfo(tag: TAG,
          msg: "------------------initState(" + this.toString() +
              ")-----------------------");
    }
  }

  @protected
  void initStatusBar() {
    SystemUiChrome().setSystemUIOverlayStyle(SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarIconBrightness: Brightness.dark
    ));
  }

  @protected
  void initSubscribe() {}

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    //active
    lifecycleProvider.onPageShow();
    if (Debug.debugDetail()) {
      LogPlugin.logInfo(tag: TAG,
          msg: "------------------didChangeDependencies(" + this.toString() +
              ")-----------------");
    }
  }

  @override
  void didUpdateWidget(T oldWidget) {
    super.didUpdateWidget(oldWidget);
    //active
    lifecycleProvider.onPageShow();
    if (Debug.debugDetail()) {
      LogPlugin.logInfo(tag: TAG,
          msg: "------------------didUpdateWidget(" + this.toString() +
              ")-----------------");
    }
  }

  @override
  void deactivate() {
    super.deactivate();
    //inactive
    lifecycleProvider.onPageHide();
    if (Debug.debugDetail()) {
      LogPlugin.logInfo(tag: TAG,
          msg: "------------------deactivate(" + this.toString() +
              ")----------------------------");
    }
  }

  @override
  void dispose() {
    super.dispose();
    lifecycleProvider.onPageDestroy();
    if (Debug.debugDetail()) {
      LogPlugin.logInfo(tag: TAG,
          msg: "------------------dispose(" + this.toString() +
              ")-------------------------------");
    }
  }

}

/// 通用绑定viewModel
abstract class ViewModelState<T extends StatefulWidget, VM extends BaseViewModel> extends LifecycleState<T> {

  @override
  void initSubscribe() {
    super.initSubscribe();
    _viewModel = initViewModel();
    lifecycleProvider = _viewModel!.lifecycleProvider;
    _viewModel?.bindLifecycle();
    _viewModel?.waitEvent.observe((value) {
        if (value.isShow) {
          _showLoadingDialog(value.message);
        } else {
          _dismissLoadingDialog();
        }
    });
    _viewModel?.finishEvent.observe((value) {
      PlatformNavigator.instance.close(value.result);
    });
  }

  VM initViewModel();

  VM? _viewModel;

  VM get viewModel => _viewModel!;

  LoadingDialog? loadingDialog;

  Future<void> _showLoadingDialog(String message) async {
    await showDialog(
        context: context,
        barrierDismissible: true,
        useRootNavigator: false,
        builder: (BuildContext context) {
          return loadingDialog = LoadingDialog(context, message: message);
        });
  }

  Future<void> _dismissLoadingDialog() async {
    loadingDialog?.dismiss();
  }
}

class LifecycleProvider<T extends LifecycleEvent> {

  LifecycleSubject<T> _lifecycleSubject = LifecycleSubject();

  T? get lifecycleEvent => _lifecycleSubject.lifecycleEvent;

  void onSubscribe(LifecycleObserver<T> owner) {
    _lifecycleSubject.onSubscribe(owner);
  }

  void unSubscribe(LifecycleObserver<T> owner) {
    _lifecycleSubject.unSubscribe(owner);
  }

  void onPageCreate() {
    _lifecycleSubject.onNext(LifecycleEvent.initial as T);
  }

  void onPageDestroy() {
    _lifecycleSubject.onNext(LifecycleEvent.defunct as T);
    _lifecycleSubject.unSubscribeAll();
  }

  void onPageHide() {
    _lifecycleSubject.onNext(LifecycleEvent.inactive as T);
  }

  void onPageShow() {
    _lifecycleSubject.onNext(LifecycleEvent.active as T);
  }
}

class LifecycleSubject<T extends LifecycleEvent> {

  T? _event;

  T? get lifecycleEvent => _event;

  void onNext(T event) {
    _event = event;
    List.of(lifecycleOwners).forEach((owner){
      owner.onNext(event);
    });
  }

  void onSubscribe(LifecycleObserver owner) {
    if (!lifecycleOwners.contains(owner)) {
      lifecycleOwners.add(owner);
    }
  }

  void unSubscribe(LifecycleObserver owner) {
    if (lifecycleOwners.contains(owner)) {
      lifecycleOwners.remove(owner);
    }
  }

  void unSubscribeAll() {
    lifecycleOwners.clear();
  }

  List<LifecycleObserver> lifecycleOwners = [];
}

abstract class LifecycleObserver<T extends LifecycleEvent> {
  void onNext(T event);
}

abstract class BindLifecycleObserver<T extends LifecycleEvent> extends LifecycleObserver<T> {
  void bindLifecycle();
  void unBindLifecycle();
  bool unBound();
}

enum LifecycleEvent {
  initial,
  active,
  inactive,
  defunct,
}