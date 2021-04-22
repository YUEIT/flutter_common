import 'package:dio/adapter.dart';
import 'package:dio/dio.dart';
import 'package:flutter_common/common/constant/api.dart';
import 'package:flutter_common/common/constant/debug.dart';
import 'package:flutter_common/common/net/base_data.dart';
import 'package:flutter_common/common/plugins/net_connet_plugin.dart';
import 'package:flutter_common/common/plugins/log_plugin.dart';

var defaultDio = Dio();

typedef RequestStartCallback = dynamic Function();
typedef RequestFailureCallback = dynamic Function(ResultError e);
typedef RequestSuccessCallback = dynamic Function(dynamic response);
typedef RequestEndCallback = dynamic Function();

class NetUtils {
  static void init() {
    Api.initBaseUrl().timeout(Duration(seconds: 10)).then((value) {
      defaultDio.options.baseUrl = value;
    }, onError: (Object error, StackTrace stackTrace) {
      defaultDio.options.baseUrl = Api.BASE_URL;
    });
    defaultDio.interceptors.add(NoNetInterceptor());
    defaultDio.interceptors.add(ParamsInterceptor());
    defaultDio.transformer = ParamsTransformer();
    //代理设置
    openProxy();
  }

  static void openProxy() {
    if (Debug.debugProxy()) {
      (defaultDio.httpClientAdapter as DefaultHttpClientAdapter)
          .onHttpClientCreate = (client) {
        // config the http client
        client.findProxy = (uri) {
          //proxy all request to localhost:8888
          // return 'PROXY 192.168.10.104:8888';
          return "PROXY " + Debug.proxyClient() + ":" +  Debug.proxyClientCode();
        };
        // you can also create a HttpClient to dio
        // return HttpClient();
      };
    }
  }

  static void request(
    Method method,
    String path, {
    Map<String, dynamic>? queryParameters,
    dynamic data,
    ProgressCallback? onReceiveProgress,
    HttpCallBack? httpCallBack,
    Response? returnResponse,
    Dio? dio,
  }) async {
    try {
      if (dio == null) {
        dio = defaultDio;
      }
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

class HttpCallBack {
  HttpCallBack(
      {this.startCallback,
      this.successCallback,
      this.failureCallback,
      this.endCallback});

  RequestStartCallback? startCallback;

  RequestSuccessCallback? successCallback;
  RequestFailureCallback? failureCallback;
  RequestEndCallback? endCallback;

  Future onStart() async {
    if (startCallback != null) {
      startCallback!();
    }
  }

  Future onSuccess(dynamic response) async {
    if (successCallback != null) {
      successCallback!(response);
    }
  }

  Future onFailure(ResultError e) async {
    if (failureCallback != null) {
      failureCallback!(e);
    }
  }

  Future onEnd() async {
    if (endCallback != null) {
      endCallback!();
    }
  }
}

enum Method { LOCAL, GET, POST }

/// 参数拦截器，合并通用参数
class ParamsInterceptor extends Interceptor {
  static const TAG = "flutter_request";

  @override
  Future onRequest(
      RequestOptions options, RequestInterceptorHandler handler) async {
    options.connectTimeout = 10000;
    options.receiveTimeout = 10000;
    options.headers['accept-language'] = 'zh-cn';
    options.headers['content-type'] = 'application/json';
    if (Debug.debugNet()) {
      LogPlugin.logInfo(
          tag: TAG,
          msg: "REQUEST => \n"
              "METHOD: ${options.method} \n"
              "PATH: ${options.uri} \n"
              "HEADER: ${options.headers} \n"
              "QUERY: ${options.queryParameters} \n"
              "BODY: ${options.data}");
    }
    return super.onRequest(options, handler);
  }

  @override
  Future onResponse(
      Response response, ResponseInterceptorHandler handler) async {
    if (Debug.debugNet()) {
      LogPlugin.logInfo(
          tag: TAG,
          msg: "RESPONSE => \n"
              "PATH: ${response.realUri} \n"
              "CODE: ${response.statusCode} \n"
              "DATA: ${response.data}");
    }
    return super.onResponse(response, handler);
  }

  @override
  Future onError(DioError err, ErrorInterceptorHandler handler) async {
    ResultError resultError;
    if (err is ResultError) {
      resultError = err;
    } else {
      resultError = ResultError.copy(err);
    }
    if (Debug.debugNet()) {
      LogPlugin.logInfo(
          tag: TAG,
          msg: "RESPONSE => \n"
              "CODE: ${resultError.type} \n"
              "TYPE: ${resultError.resultType} \n"
              "MSG: ${resultError.message}");
    }
    return super.onError(resultError, handler);
  }
}

/// 网络连接拦截器
class NoNetInterceptor extends Interceptor {
  @override
  Future onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    bool connect = await isConnected();
    if (connect) {
      return super.onRequest(options, handler);
    } else {
      handler.reject(ResultError(
          requestOptions: options,
          error: "网络连接异常",
          resultType: ResultErrorType.NO_NET));
    }
  }

  @override
  Future onResponse(
      Response response, ResponseInterceptorHandler handler) async {
    return super.onResponse(response, handler);
  }

  @override
  Future onError(DioError err, ErrorInterceptorHandler handler) async {
    return super.onError(err, handler);
  }

  Future<bool> isConnected() async {
    return await NetConnectPlugin.isNetConnect();
  }
}

///返回参数剥离，获取data实际内容体
class ParamsTransformer extends DefaultTransformer {
  @override
  Future<String> transformRequest(RequestOptions options) {
    return super.transformRequest(options);
  }

  @override
  Future transformResponse(RequestOptions options, ResponseBody response) async {
    return super.transformResponse(options, response).then((onValue) {
      BaseData baseData = BaseData.fromJson(onValue);
      if (baseData.code != 0) {
        throw ResultError(requestOptions: options, errorCode: baseData.code, error: baseData.message);
      }
      return baseData.data;
    });
  }
}

/// 自定义异常
class ResultError extends DioError {

  static ResultError copy(DioError error) {
    return ResultError(
        requestOptions: error.requestOptions,
        response: error.response,
        type: error.type,
        error: error.error);
  }

  /// Response info, it may be `null` if the request can't reach to
  /// the http server, for example, occurring a dns error, network is not available.
  Response? response;

  /// The original error/exception object; It's usually not null when `type`
  /// is DioErrorType.DEFAULT
  dynamic error;

  String get message => (error?.toString() ?? '');

  ResultErrorType? resultType;

  int? errorCode;

  ResultError(
      {required RequestOptions requestOptions,
        this.response,
        DioErrorType type = DioErrorType.other,
        this.error,
        this.errorCode,
        this.resultType})
      : super(
      requestOptions: requestOptions,
      response: response,
      type: type,
      error: error) {
    if (resultType == null) {
      resultType = parseErrorType();
    }
    if (type == DioErrorType.connectTimeout ||
        type == DioErrorType.receiveTimeout ||
        type == DioErrorType.sendTimeout) {
      resultType = ResultErrorType.TIME_OUT;
    }
    if (errorCode == null) {
      errorCode = parseErrorCode();
    }
  }

  int? parseErrorCode() {
    switch (resultType) {
      case ResultErrorType.ERROR_SERVER:
        return 1000;
      case ResultErrorType.TOKEN_INVALID:
        return 2000;
      case ResultErrorType.NO_DATA:
        return 3000;
      case ResultErrorType.NO_NET:
        return 4000;
      case ResultErrorType.OPERATION:
        return 5000;
      case ResultErrorType.TIME_OUT:
        return 6000;
      case ResultErrorType.UN_KNOW:
        return -1;
      default:
        return null;
    }
  }

  ResultErrorType parseErrorType() {
    if (errorCode != null) {
      switch (errorCode) {
        case 1000:
          return ResultErrorType.ERROR_SERVER;
        case 2000:
          return ResultErrorType.TOKEN_INVALID;
        case 3000:
          return ResultErrorType.NO_DATA;
        case 4000:
          return ResultErrorType.NO_NET;
        case 5000:
          return ResultErrorType.OPERATION;
        case 6000:
          return ResultErrorType.TIME_OUT;
        default:
          return ResultErrorType.ERROR_SERVER;
      }
    }
    return ResultErrorType.UN_KNOW;
  }
}

enum ResultErrorType {
  ERROR_SERVER,
  TOKEN_INVALID,
  NO_DATA,
  NO_NET,
  OPERATION,
  UN_KNOW,
  TIME_OUT
}
