import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/vm/base_view_model.dart';
import 'package:flutter_common/common/components/vm/data/page_mode.dart';
import 'package:flutter_common/common/components/vm/data/page_state.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/net/http_call_back.dart';
import 'package:flutter_common/common/plugins/toast_plugin.dart';

/// 分页provider 相当于VM层
/// 持有页面状态PageState与数据data
/// 通过stream的观察者，将数据映射到widget中
/// 已持有网络回调getHttpCallBack方法，可通过继承对应的onSuccess, onFailure处理对应逻辑
/// 其中parseResponse必须实现

abstract class PageViewModel<P extends PageMode<T>, T> extends BaseViewModel {

  LiveData<PageStateEvent> pageStateLiveData = LiveData(initialData: PageStateEvent.loading());
  ListLiveData dataListLiveData = ListLiveData();

  bool _isLoading = false;
  bool _isEnd = false;
  bool _isFirstLoad = true;
  int currentPage = initPage;
  int showPage = initPage;
  static const int initPage = 1;

  CancelToken token = CancelToken();

  bool noMore() => _isEnd;

  void postPageState(PageStateEvent pageStateEvent) {
    pageStateLiveData.postValue(pageStateEvent);
  }

  P parseResponse(dynamic response);

  HttpCallBack getHttpCallBack({
    RequestStartCallback? onStart,
    RequestSuccessCallback? onSuccess,
    RequestFailureCallback? onFailure,
    RequestEndCallback? onEnd}) {
    return LifecycleHttpCallBack(
        startCallback: (){
          _isLoading = true;
          if (onStart != null) {
            onStart();
          }
        },
        successCallback: (response) {
          _dealWithSuccess(response);
          if (onSuccess != null) {
            onSuccess(response);
          }
        },
        failureCallback: (e) {
          _dealWidthFailure(e);
          if (onFailure != null) {
            onFailure(e);
          }
        },
        endCallback: (){
          _isLoading = false;
          if (onEnd != null) {
            onEnd();
          }
        }
    );
  }

  /// 接口成功处理
  void _dealWithSuccess(dynamic response) {
    P p = parseResponse(response);
    if (currentPage == initPage) {
      dataListLiveData.setValue([]);
    }
    if (p.isEmpty() && _isFirstLoad) {
      postPageState(PageStateEvent.noData());
    } else {
      postPageState(PageStateEvent.normal());
      _isFirstLoad = false;
    }
    _isEnd = p.isEnd();
    optionShowPage(p.getPageNo() ?? currentPage);
    List<T> dataModel = p.getDataList();
    dataListLiveData.addAll(dataModel);
  }

  /// 接口失败处理
  void _dealWidthFailure(ResultError e) {
    if (_isFirstLoad) {
      if (e.resultType == ResultErrorType.NO_DATA) {
        postPageState(PageStateEvent.noData());
      } else if (e.resultType == ResultErrorType.NO_NET) {
        postPageState(PageStateEvent.noNet());
      } else if (e.resultType == ResultErrorType.ERROR_SERVER) {
        var pageStateEvent = PageStateEvent.error();
        pageStateEvent.showInfo = e.message;
        postPageState(pageStateEvent);
      } else if (e.resultType == ResultErrorType.TIME_OUT) {
        postPageState(PageStateEvent.noNet());
      }
    }
    ToastPlugin.showShort(e.message);
  }

  Future<void> refresh() async{
    currentPage = initPage;
    _isEnd = false;
    await loadData();
  }

  //获取网络数据
  Future<void> loadData();

  Future<void> loadMore({ScrollNotification? notification}) async{
    if (canLoadMore(notification: notification)) {
      currentPage = showPage + 1;
      await loadData();
    }
  }

  /// 通过NotificationListener观察到当前滑动位置来判断是否需要加载
  bool canLoadMore({ScrollNotification? notification}) {
    if (notification == null) {
      return !_isEnd && !_isLoading;
    }
    return notification.metrics.pixels > 20
        && notification.metrics.pixels >= notification.metrics.maxScrollExtent - 20
        && !_isEnd
        && !_isLoading;
  }

  void optionShowPage(int page) {
    currentPage = page;
    showPage = page;
  }

  //释放
  @override
  void dispose(){
    super.dispose();
    pageStateLiveData.dispose();
    dataListLiveData.dispose();
    token.cancel();
  }

}