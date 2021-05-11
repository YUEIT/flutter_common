import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/components/vm/pull_view_model.dart';
import 'package:flutter_common/common/constant/api.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/net/net_client.dart';
import 'package:flutter_common/example/base/example_api.dart';
import 'package:flutter_common/example/mode/pull_list_mode.dart';

class PullListViewModel extends PullViewModel {

  ListLiveData<PullListMode> data = ListLiveData();

  @override
  Future<void> loadData() async {
    NetClient.instance().request(Method.GET, ExampleApi.USER,
      httpCallBack: getHttpCallBack(
        onSuccess: (response){
          List<PullListMode> list = parseResponse(response);
          data.postValue(list);
        },
      )
    );
  }

  void addUser() {
    // data.add(PullListMode(
    //   id: 3,
    //   name: "xixixi",
    //   phone: "10000",
    //   address: "jjjjjj"
    // ));
  }

  List<PullListMode> parseResponse(response) {
    List<PullListMode> list = (response as List).map((i) => PullListMode.fromJson(i)).toList();
    return list;
  }

  @override
  void dispose() {
    super.dispose();
    data.dispose();
  }
}