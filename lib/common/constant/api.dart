import 'package:flutter_common/common/plugins/constant_plugin.dart';

class Api {

  static const String BASE_URL = "http://101.133.164.46:8080";

  static Future<String> initBaseUrl() async {
    int? type = await ConstantPlugin.getBuildEnvironment();
    return BASE_URL;
  }

}