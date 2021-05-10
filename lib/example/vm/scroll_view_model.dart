import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/components/vm/pull_view_model.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/net/net_client.dart';

class ScrollViewModel extends PullViewModel {

  LiveData<String> data = LiveData();

  @override
  Future<void> loadData() async {
    NetClient.instance().request(Method.LOCAL, "",
        httpCallBack: getHttpCallBack(
          onSuccess: (response){
            print("onSuccess");
            data.postValue(response.toString());
          },
        ),
      returnResponse: Response(
          requestOptions: RequestOptions(path: ""),
          data: jsonDecode("{}"),
      )
    );
  }
}