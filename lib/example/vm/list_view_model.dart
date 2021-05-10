import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_common/common/components/vm/page_view_model.dart';
import 'package:flutter_common/common/net/http.dart';
import 'package:flutter_common/common/net/net_client.dart';

import '../mode/list_mode.dart';

class ListViewModel extends PageViewModel<ListMode, Order> {

  //获取网络数据
  @override
  Future<void> loadData() async{
    NetClient.instance().request(Method.LOCAL, "path " + currentPage.toString(),
        httpCallBack: getHttpCallBack(),
        returnResponse: Response(
            requestOptions: RequestOptions(path: "path " + currentPage.toString()),
            data: jsonDecode(json)
        )
    );
  }

  @override
  ListMode parseResponse(response) {
    return ListMode.fromJson(response);
  }

  String json = "{\n" +
      "\t\"orderList\":[\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t\"id\": 1,\n" +
      "\t\t\t\"image\": \"xxxx\",\n" +
      "\t\t\t\"title\" : \"sssss\",\n" +
      "\t\t\t\"des\": \"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\"\n" +
      "\t\t}\n" +
      "\t]\n" +
      "}";
}