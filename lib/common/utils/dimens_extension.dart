import 'package:flutter_screenutil/flutter_screenutil.dart';

extension DimensExtension on num {

  static double screenWidth = 375;
  static double screenHeight = 750;

  /// 尺寸
  double get rdp => ScreenUtil().setWidth(this);

  /// 字体
  double get rsp => ScreenUtil().setSp(this);

  ///屏幕宽度的倍数
  ///Multiple of screen width
  double get rsw => ScreenUtil().screenWidth * this;

  ///屏幕高度的倍数
  ///Multiple of screen height
  double get rsh => ScreenUtil().screenHeight * this;
}
