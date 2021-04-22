import 'package:flutter_common/common/components/vm/base_view_model.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/net/http_call_back.dart';
import 'package:flutter_common/example/base/example_api.dart';

class NetViewModel extends BaseViewModel {

  LiveData<String> responseStr = LiveData(initialData: "访问数据");

  void login() {
    NetUtils.request(Method.POST, ExampleApi.LOGIN,
        data: {'tel': "15901793643", 'code': '333333'},
        httpCallBack: LifecycleHttpCallBack(
          lifecycleObserver: this,
          startCallback: (){
            print("start: ");
          },
          successCallback: (data){
            print("success: $data");
            responseStr.postValue("$data");
          },
          failureCallback: (e){
            print("error ${e.error}");
          },
        )
    );
  }

  @override
  void dispose() {
    super.dispose();
    responseStr.dispose();
  }

}