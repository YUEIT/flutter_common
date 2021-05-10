import 'package:flutter_common/common/components/vm/data/page_state.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/net/http_call_back.dart';
import 'package:flutter_common/common/plugins/toast_plugin.dart';
import 'package:flutter_common/common/components/vm/base_view_model.dart';

abstract class PullViewModel extends BaseViewModel {

  LiveData<PageStateEvent> pageStateLiveData = LiveData(initialData: PageStateEvent.loading());

  bool _isLoading = false;
  bool _isFirstLoad = true;

  void postPageState(PageStateEvent pageState) {
    pageStateLiveData.postValue(pageState);
  }

  HttpCallBack getHttpCallBack({
    RequestStartCallback? onStart,
    RequestSuccessCallback? onSuccess,
    RequestFailureCallback? onFailure,
    RequestEndCallback? onEnd}) {
    return LifecycleHttpCallBack(
      lifecycleObserver: this,
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
    postPageState(PageStateEvent.normal());
    _isFirstLoad = false;
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
    await loadData();
  }

  //获取网络数据
  Future<void> loadData();

  //释放
  @override
  void dispose(){
    super.dispose();
    pageStateLiveData.dispose();
  }
}