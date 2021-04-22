import 'package:flutter/foundation.dart';

class Debug {

  static bool _isDebug = kDebugMode;

  static bool isDebug() {
    return _isDebug;
  }

  /// 切勿轻易改变
  static void setDebug(bool debug) {
    _isDebug = debug;
  }

  /// 开启所有继承LifecycleState页面的生命周期
  static bool _isShowDetailLog = false;

  static bool debugDetail() {
    if (!isDebug()) {
      return false;
    }
    return _isShowDetailLog;
  }

  static void setDebugDetail(bool isOpen) {
    _isShowDetailLog = isOpen;
  }

  /// 开启网络接口日志
  static bool _isShowNetLog = false;

  static bool debugNet() {
    if (!isDebug()) {
      return false;
    }
    return _isShowNetLog;
  }

  static void setDebugNet(bool isOpen) {
    _isShowNetLog = isOpen;
  }

  /// 开启日志
  static bool _isShowLog = false;

  static bool debugLog() {
    if (!isDebug()) {
      return false;
    }
    return _isShowLog;
  }

  static void setDebugLog(bool isOpen) {
    _isShowLog = isOpen;
  }

  /// 开启代理
  static bool _openProxy = false;

  static bool debugProxy() {
    if (!isDebug()) {
      return false;
    }
    return _openProxy;
  }

  static void setProxy(bool isOpen) {
    _openProxy = isOpen;
  }

  /// 配置代理，IP为主机地址
  static String _proxyClient = "";

  static String proxyClient() {
    return _proxyClient;
  }

  static void setProxyClient(String str) {
    _proxyClient = str;
  }

  static String _proxyClientCode = "";

  static String proxyClientCode() {
    return _proxyClientCode;
  }

  static void setProxyClientCode(String str) {
    _proxyClientCode = str;
  }
}