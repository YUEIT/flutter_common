import 'package:flutter_common/common/components/vm/data/page_mode.dart';
import 'package:flutter_common/common/utils/map_extension.dart';

class CouponMode extends PageMode<CouponBean>{
  PageBean? page;
  List<CouponBean>? coupons;

  CouponMode({this.page, this.coupons});

  factory CouponMode.fromJson(Map<String, dynamic> json) {
    var originPage = json['page'];
    PageBean? finalPage;
    if (originPage != null) {
      finalPage =  PageBean.fromJson(originPage);
    }
    var originCoupons = json['coupons'];
    List<CouponBean>? finalCoupons;
    if (originCoupons != null) {
      finalCoupons = (originCoupons as List).map((i) => CouponBean.fromJson(i)).toList();
    }
    return CouponMode(
      page: finalPage,
      coupons: finalCoupons,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['page'] = this.page;
    if (this.coupons != null) {
      data['coupons'] = this.coupons!.map((e) => e.toJson()).toList();
    }
    return data;
  }

  @override
  List<CouponBean> getDataList() {
    return coupons ?? [];
  }

  @override
  int? getPageNo() {
    return null;
  }

  @override
  int getPageSize() {
    return 20;
  }

  @override
  bool isEmpty() {
    return coupons == null || coupons!.isEmpty;
  }

  @override
  bool isEnd() {
    return coupons == null || coupons!.isEmpty;
  }
}

class PageBean {
  int? total;
  int? count;
  int? more;

  PageBean({this.total, this.count, this.more});

  factory PageBean.fromJson(Map<String, dynamic> json) {
    return PageBean(
      total: json.getNullInt('total'),
      count: json.getNullInt('count'),
      more: json.getNullInt('more')
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['total'] = total;
    data['count'] = count;
    data['more'] = more;
    return data;
  }
}

class CouponBean {

  String? couponId;
  String? couponName;
  String? couponNo;
  String? userId;
  String? satisfyMoney;
  String? couponMoney;
  String? invalidTimeExplain;
  int? status;
  int? useGoods;
  String? satisfyExplain;
  String? useExplain;
  String? couponTypeName;
  int? couponType;
  int? grantStatus;
  String? useRules;
  int? useRange;
  String? userEnabled;
  String? useCouponToast;
  String? grantType;

  bool openRule = false;

  CouponBean({this.couponId, this.couponName, this.couponNo, this.userId,
    this.satisfyMoney, this.couponMoney, this.invalidTimeExplain, this.status,
    this.useGoods, this.satisfyExplain, this.useExplain, this.couponTypeName, this.couponType,
    this.grantStatus, this.useRules, this.useRange, this.userEnabled, this.useCouponToast,
    this.grantType
  });

  factory CouponBean.fromJson(Map<String, dynamic> json) {
    return CouponBean(
      couponId: json.getNullString('coupon_id'),
      couponName: json.getNullString('coupon_name'),
      couponNo: json.getNullString('coupon_no'),
      userId: json.getNullString('user_id'),
      satisfyMoney: json.getNullString('satisfy_money'),
      couponMoney: json.getNullString('coupon_money'),
      invalidTimeExplain: json.getNullString('invalid_time_explain'),
      status: json.getNullInt('status'),
      useGoods: json.getNullInt('use_goods'),
      satisfyExplain: json.getNullString('satisfy_explain'),
      useExplain: json.getNullString('use_explain'),
      couponTypeName: json.getNullString('coupon_type_name'),
      couponType: json.getNullInt('coupon_type'),
      useRules: json.getNullString('use_rules'),
      useRange: json.getNullInt('use_range'),
      userEnabled: json.getNullString('use_enabled'),
      useCouponToast: json.getNullString('use_coupon_toast'),
      grantType: json.getNullString('grant_type')
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['coupon_id'] = couponId;
    data['coupon_name'] = couponName;
    data['coupon_no'] = couponNo;
    data['user_id'] = userId;
    data['satisfy_money'] = satisfyMoney;
    data['coupon_money'] = couponMoney;
    data['invalid_time_explain'] = invalidTimeExplain;
    data['status'] = status;
    data['use_goods'] = useGoods;
    data['satisfy_explain'] = satisfyExplain;
    data['use_explain'] = useExplain;
    data['coupon_type_name'] = couponTypeName;
    data['coupon_type'] = couponType;
    data['grant_status'] = grantStatus;
    data['use_rules'] = useRules;
    data['use_range'] = useRange;
    data['use_enabled'] = userEnabled;
    data['use_coupon_toast'] = useCouponToast;
    data['grant_type'] = grantType;
    return data;
  }

}