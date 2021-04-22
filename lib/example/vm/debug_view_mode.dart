import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/vm/base_view_model.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/constant/debug.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/plugins/toast_plugin.dart';

class DebugViewModel extends BaseViewModel {

  LiveData<bool> debug = LiveData(initialData: Debug.isDebug());
  LiveData<bool> openProxy = LiveData(initialData: Debug.debugProxy());

  LiveData<bool> showLog = LiveData(initialData: Debug.debugLog());
  LiveData<bool> showNetLog = LiveData(initialData: Debug.debugNet());
  LiveData<bool> showDetailLog = LiveData(initialData: Debug.debugDetail());
  String proxyClient = Debug.proxyClient();
  TextEditingController ipController = TextEditingController();
  String proxyClientCode = Debug.proxyClientCode();
  TextEditingController codeController = TextEditingController();

  void openDebug(bool isOpen) {
    debug.setValue(isOpen);
  }

  void openProxySetting(bool isOpen) {
    if (debug.value!) {
      openProxy.setValue(isOpen);
    } else {
      openProxy.setValue(false);
    }
  }

  void openLog(bool isOpen) {
    if (debug.value!) {
      showLog.postValue(isOpen);
    } else {
      showLog.setValue(false);
    }
  }

  void openNetLog(bool isOpen) {
    if (debug.value! && showLog.value!) {
      showNetLog.postValue(isOpen);
    } else {
      showNetLog.setValue(false);
    }
  }

  void openDetailLog(bool isOpen) {
    if (debug.value! && showLog.value!) {
      showDetailLog.postValue(isOpen);
    } else {
      showDetailLog.setValue(false);
    }
  }

  void saveState() {
    Debug.setDebug(debug.value!);
    Debug.setDebugLog(showLog.value!);
    Debug.setDebugNet(showNetLog.value!);
    Debug.setDebugDetail(showDetailLog.value!);
    Debug.setProxy(openProxy.value!);
    Debug.setProxyClient(ipController.text);
    Debug.setProxyClientCode(codeController.text);
    NetUtils.openProxy();
    ToastPlugin.showShort("保存成功~");
  }
}