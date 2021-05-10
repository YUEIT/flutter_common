import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/components/vm/data/event_mode.dart';

abstract class BaseViewModel implements BindLifecycleObserver<LifecycleEvent> {

  LifecycleProvider lifecycleProvider = LifecycleProvider();
  LiveData<WaitEvent> waitEvent = LiveData();
  LiveData<FinishEvent> finishEvent = LiveData();

  @override
  void bindLifecycle() {
    lifecycleProvider.onSubscribe(this);
  }

  @override
  void unBindLifecycle() {
    lifecycleProvider.unSubscribe(this);
  }

  @mustCallSuper
  @override
  void onNext(LifecycleEvent event) {
    if (unBound()) {
      unBindLifecycle();
      dispose();
    }
  }

  @override
  bool unBound() {
    return lifecycleProvider.lifecycleEvent == LifecycleEvent.defunct;
  }

  @mustCallSuper
  void dispose() {
    waitEvent.dispose();
    finishEvent.dispose();
  }
}