/// Description : 
/// Created by yue on 4/21/21

import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_common/common/utils/dimens_extension.dart';

/// 经典Header
class CustomHeader extends Header {
  /// Key
  final Key? key;

  /// 提示刷新文字
  final String? refreshText;

  /// 背景颜色
  final Color bgColor;

  /// 字体颜色
  final Color textColor;

  CustomHeader({
    bool float = false,
    Duration completeDuration = const Duration(seconds: 1),
    bool enableInfiniteRefresh = false,
    bool enableHapticFeedback = true,
    this.key,
    this.refreshText,
    this.bgColor: Colors.transparent,
    this.textColor: Colors.black,
  }) : super(
    extent: 80.rdp,
    triggerDistance: 90.rdp,
    float: float,
    completeDuration: completeDuration,
    enableInfiniteRefresh: enableInfiniteRefresh,
    enableHapticFeedback: enableHapticFeedback,
  );

  @override
  Widget contentBuilder(
      BuildContext context,
      RefreshMode refreshState,
      double pulledExtent,
      double refreshTriggerPullDistance,
      double refreshIndicatorExtent,
      AxisDirection axisDirection,
      bool float,
      Duration? completeDuration,
      bool enableInfiniteRefresh,
      bool success,
      bool noMore) {
    return CustomHeaderWidget(
      key: key,
      classicalHeader: this,
      refreshState: refreshState,
      pulledExtent: pulledExtent,
      refreshTriggerPullDistance: refreshTriggerPullDistance,
      refreshIndicatorExtent: refreshIndicatorExtent,
      axisDirection: axisDirection,
      float: float,
      completeDuration: completeDuration,
      enableInfiniteRefresh: enableInfiniteRefresh,
      success: success,
      noMore: noMore,
    );
  }
}

/// 经典Header组件
class CustomHeaderWidget extends StatefulWidget {
  final CustomHeader classicalHeader;
  final RefreshMode refreshState;
  final double pulledExtent;
  final double refreshTriggerPullDistance;
  final double refreshIndicatorExtent;
  final AxisDirection axisDirection;
  final bool float;
  final Duration? completeDuration;
  final bool enableInfiniteRefresh;
  final bool success;
  final bool noMore;

  CustomHeaderWidget(
      {Key? key,
        required this.refreshState,
        required this.classicalHeader,
        required this.pulledExtent,
        required this.refreshTriggerPullDistance,
        required this.refreshIndicatorExtent,
        required this.axisDirection,
        required this.float,
        required this.completeDuration,
        required this.enableInfiniteRefresh,
        required this.success,
        required this.noMore})
      : super(key: key);

  @override
  State createState() => CustomHeaderWidgetState();
}

class CustomHeaderWidgetState extends State<CustomHeaderWidget>
    with TickerProviderStateMixin<CustomHeaderWidget> {
  // 是否到达触发刷新距离
  bool _overTriggerDistance = false;

  bool get overTriggerDistance => _overTriggerDistance;

  set overTriggerDistance(bool over) {
    if (_overTriggerDistance != over) {
      _overTriggerDistance = over;
    }
  }

  /// 文本
  String get _refreshText {
    return widget.classicalHeader.refreshText ?? "100%正品·限时抢购·退换无忧";
  }

  // 是否刷新完成
  bool _refreshFinish = false;

  set refreshFinish(bool finish) {
    if (_refreshFinish != finish) {
      if (finish && widget.float) {
        Future.delayed(widget.completeDuration! - Duration(milliseconds: 400),
                () {
              if (mounted) {
                _floatBackController?.forward();
              }
            });
        Future.delayed(widget.completeDuration!, () {
          _floatBackDistance = null;
          _refreshFinish = false;
        });
      }
      _refreshFinish = finish;
    }
  }

  // 动画
  AnimationController? _floatBackController;
  Animation<double>? _floatBackAnimation;

  // 浮动时,收起距离
  double? _floatBackDistance;

  // 显示文字
  String get _showText {
    return _refreshText;
  }

  // 刷新结束图标
  IconData get _finishedIcon {
    if (!widget.success) return Icons.error_outline;
    if (widget.noMore) return Icons.hourglass_empty;
    return Icons.done;
  }

  @override
  void initState() {
    super.initState();
    // float收起动画
    _floatBackController = new AnimationController(
        duration: const Duration(milliseconds: 300), vsync: this);
    _floatBackAnimation =
    new Tween(begin: widget.refreshIndicatorExtent, end: 0.0)
        .animate(_floatBackController!)
      ..addListener(() {
        setState(() {
          if (_floatBackAnimation!.status != AnimationStatus.dismissed) {
            _floatBackDistance = _floatBackAnimation!.value;
          }
        });
      });
    _floatBackAnimation!.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        _floatBackController!.reset();
      }
    });
  }

  @override
  void dispose() {
    _floatBackController!.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // 是否到达触发刷新距离
    overTriggerDistance = widget.refreshState != RefreshMode.inactive &&
        widget.pulledExtent >= widget.refreshTriggerPullDistance;
    if (widget.refreshState == RefreshMode.refreshed) {
      refreshFinish = true;
    }
    var percent = widget.pulledExtent / widget.refreshTriggerPullDistance;
    if (percent > 1) {
      percent = 1;
    }
    return Stack(
      children: <Widget>[
        Positioned(
          top: null,
          bottom:_floatBackDistance == null ? 0.0 : (widget.refreshIndicatorExtent - _floatBackDistance!),
          left: 0.0,
          right: 0.0,
          child: Container(
            alignment: Alignment.bottomCenter,
            width: double.infinity,
            height: _floatBackDistance == null
                ? (widget.refreshIndicatorExtent > widget.pulledExtent
                ? widget.refreshIndicatorExtent
                : widget.pulledExtent)
                : widget.refreshIndicatorExtent,
            color: widget.classicalHeader.bgColor,
            child: SizedBox(
              height: widget.refreshIndicatorExtent,
              width: double.infinity,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  buildAnimWidget(percent),
                  buildInfoWidget()
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget buildAnimWidget(double percent) {
    return Expanded(
        flex: 2,
        child: Container(
          width: 80.rdp * percent,
          height: 30.rdp * percent + 20.rdp,
          margin: EdgeInsets.only(top: 10.rdp, bottom: 10.rdp),
          child: widget.refreshState == RefreshMode.refreshed
              || widget.refreshState == RefreshMode.refresh
              ? Icon(Icons.stream)
              : Icon(Icons.stream),
        )
    );
  }

  Widget buildInfoWidget() {
    return Expanded(
      flex: 1,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text(
            _showText,
            style: TextStyle(
              fontSize: 12.rsp,
              color: widget.classicalHeader.textColor,
            ),
          ),
        ],
      ),
    );
  }
}