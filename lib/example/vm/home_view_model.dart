import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/example/mode/home_mode.dart';
import 'package:flutter_common/common/net/http.dart';
import '../../common/components/vm/simple_pull_view_model.dart';

class HomeViewModel extends SimplePullViewModel<HomeMode>{

  @override
  Future<void> loadData() async{
    NetUtils.request(Method.LOCAL, "",
        httpCallBack: getHttpCallBack(
          onSuccess: (response) {
            var result = parseResponse(response);
            if (result != null) {
              data.postValue(result);
            }
          }
        ),
        returnResponse: Response(
            requestOptions: RequestOptions(path: ""),
            data: jsonDecode(json),
        )
    );
  }

  HomeMode parseResponse(response) {
    return HomeMode.fromJson(response);
  }

  String json = "{\n" +
      "\t\"homeData\" : [ \n" +
      "\t\t{\n" +
      "\t\t\t\"id\" : 0,\n" +
      "\t\t\t\"type\": 0,\n" +
      "\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\"banner\" : [\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t}\n" +
      "\t\t\t]\t\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\" : 1,\n" +
      "\t\t\t\"type\": 1,\n" +
      "\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\"menu\" : [\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"image\" : \"ddddd\",\n" +
      "\t\t\t\t\t\"width\" : 1080,\n" +
      "\t\t\t\t\t\"height\" : 300\n" +
      "\t\t\t\t}\n" +
      "\t\t\t]\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\" : 2,\n" +
      "\t\t\t\"type\": 2,\n" +
      "\t\t\t\"title\" : \"xxxx\",\n" +
      "\t\t\t\"goods\" : [\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t},\n" +
      "\t\t\t\t{\n" +
      "\t\t\t\t\t\"id\": 1,\n" +
      "\t\t\t\t\t\"name\" : \"xxxx\",\n" +
      "\t\t\t\t\t\"des\" : \"sssss\",\n" +
      "\t\t\t\t\t\"price\" : 1111,\n" +
      "\t\t\t\t\t\"stock\" :2000,\n" +
      "\t\t\t\t\t\"image\" : \"ssss\"\n" +
      "\t\t\t\t}\n" +
      "\t\t\t]\n" +
      "\t\t}\n" +
      "\t]\n" +
      "}";

}