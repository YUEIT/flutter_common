import 'package:dio/dio.dart';
import 'package:flutter_common/common/constant/api.dart';

import 'http.dart';

/// Description :
/// Created by yue on 4/26/21

abstract class INetClient {

  Dio dio = Dio();

  Future<String> baseUrl();

  List<Interceptor> interceptors();

  Transformer transformer();

  Future<void> request(
      Method method,
      String path, {
        Map<String, dynamic>? queryParameters,
        dynamic data,
        ProgressCallback? onReceiveProgress,
        HttpCallBack? httpCallBack,
        Response? returnResponse,
      }) async {
    try {
      httpCallBack?.onStart();
      Response? response;
      if (method == Method.GET) {
        response = await dio.get(path,
            queryParameters: queryParameters,
            onReceiveProgress: onReceiveProgress);
      } else if (method == Method.POST) {
        response = await dio.post(path,
            data: data,
            queryParameters: queryParameters,
            onReceiveProgress: onReceiveProgress);
      } else if (method == Method.LOCAL) {
        response = await Future.sync(() {
          return returnResponse;
        });
      }
      if (response == null || response.data == null) {
        httpCallBack?.onFailure(ResultError(
            requestOptions:
            response?.requestOptions ?? RequestOptions(path: path),
            resultType: ResultErrorType.NO_DATA,
            error: "没有数据"));
      } else {
        httpCallBack?.onSuccess(response.data);
      }
    } on DioError catch (e) {
      ResultError resultError;
      if (e is ResultError) {
        resultError = e;
      } else {
        resultError = ResultError.copy(e);
      }
      httpCallBack?.onFailure(resultError);
    }
    httpCallBack?.onEnd();
  }
}

class NetClient extends INetClient{

  @override
  Future<String> baseUrl() async {
    return await Api.initBaseUrl();
  }

  @override
  List<Interceptor> interceptors() {
    return [NoNetInterceptor(), ParamsInterceptor()];
  }

  @override
  Transformer transformer() {
    return ParamsTransformer();
  }

  static NetClient? _instance;
  static NetClient instance() {
    if (_instance == null) {
      _instance = NetClient();
      return _instance!;
    }
    return _instance!;
  }

}