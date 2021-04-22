/// 返回参数基础类型
class BaseData {
    dynamic data;
    int? code;
    String? message;

    BaseData({this.data, this.code, this.message});

    factory BaseData.fromJson(Map<String, dynamic> json) {
        int code;
        if (json['code'] is String) {
            code = int.parse(json['code']);
        } else {
            code = json['code'];
        }
        return BaseData(
            data: json['data'],
            code: code,
            message: json['msg'],
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        data['code'] = this.code;
        data['message'] = this.message;
        data['data'] = this.data;
        return data;
    }
}