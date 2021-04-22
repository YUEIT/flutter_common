import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';

typedef RefreshCallback = Future<void> Function();
typedef LoadCallback = Future<void> Function();

class RefreshWidget extends StatelessWidget {

  RefreshWidget({
    Key? key,
    required this.child,
    required this.onRefresh,
    this.onLoad,
    this.noMore = false,
  });

  final Widget child;

  final RefreshCallback onRefresh;

  final LoadCallback? onLoad;
  final bool noMore;

  /// controller 失效，原因不明，这里使用easyRefresh内机制控制。
  @override
  Widget build(BuildContext context) {
    return EasyRefresh(
      child: child,
      onRefresh: () async {
        await onRefresh();
      },
      onLoad: onLoad == null ? null : () async {
        await Future.delayed(Duration(seconds: 1));
        await onLoad!();
      },
      footer: CustomFooter(
          footerBuilder: _customFooter
      ),
    );
  }

  Widget _customFooter(BuildContext context,
    LoadMode loadState,
    double pulledExtent,
    double loadTriggerPullDistance,
    double loadIndicatorExtent,
    AxisDirection axisDirection,
    bool float,
    Duration completeDuration,
    bool enableInfiniteLoad,
    bool success,
    bool end) {
      String showText = "";
        if (loadState == LoadMode.drag) {
          showText = "上拉加载~";
        } else if (loadState == LoadMode.armed) {
          showText = "加载中...";
        } else if (loadState == LoadMode.load) {
          showText = "加载中...";
        } else if (loadState == LoadMode.done) {
          showText = "加载完成~";
        }
        if (noMore) {
          showText = "没有更多了~";
        }
        return Container(
          child: Text(showText),
        );
    }

//    return RefreshIndicator(
//      child: child,
//      onRefresh: () async{
//        await onRefresh();
//      }
//    );


}

const Duration _kIndicatorScaleDuration = Duration(milliseconds: 200);

class MaterialHeader extends Header {
  final Key? key;
  final double displacement;
  final Animation<Color>? valueColor;
  final Color? backgroundColor;

  final LinkHeaderNotifier linkNotifier = LinkHeaderNotifier();

  MaterialHeader({
    this.key,
    this.displacement = 40.0,
    this.valueColor,
    this.backgroundColor,
    completeDuration = const Duration(seconds: 1),
    bool enableHapticFeedback = false,
  }): super(
    float: true,
    extent: 70.0,
    triggerDistance: 70.0,
    completeDuration: completeDuration == null
        ? Duration(milliseconds: 300,)
        : completeDuration + Duration(milliseconds: 300,),
    enableInfiniteRefresh: false,
    enableHapticFeedback: enableHapticFeedback,
  );

  @override
  Widget contentBuilder(BuildContext context, RefreshMode refreshState,
      double pulledExtent, double refreshTriggerPullDistance,
      double refreshIndicatorExtent, AxisDirection axisDirection,
      bool float, Duration completeDuration, bool enableInfiniteRefresh,
      bool success, bool noMore) {
    linkNotifier.contentBuilder(context, refreshState, pulledExtent,
        refreshTriggerPullDistance, refreshIndicatorExtent, axisDirection,
        float, completeDuration, enableInfiniteRefresh, success, noMore);
    return MaterialHeaderWidget(
      key: key,
      displacement: displacement,
      valueColor: valueColor,
      backgroundColor: backgroundColor,
      linkNotifier: linkNotifier,
    );
  }
}
class MaterialHeaderWidget extends StatefulWidget {
  final double displacement;
  final Animation<Color>? valueColor;
  final Color? backgroundColor;
  final LinkHeaderNotifier? linkNotifier;

  const MaterialHeaderWidget({
    Key? key,
    this.displacement = 0,
    this.valueColor,
    this.backgroundColor,
    this.linkNotifier,
  }) : super(key: key);

  @override
  MaterialHeaderWidgetState createState() {
    return MaterialHeaderWidgetState();
  }
}
class MaterialHeaderWidgetState extends State<MaterialHeaderWidget>
    with TickerProviderStateMixin<MaterialHeaderWidget>{
  static final Animatable<double> _oneToZeroTween =
  Tween<double>(begin: 1.0, end: 0.0);

  RefreshMode? get _refreshState => widget.linkNotifier?.refreshState;
  double get _pulledExtent => widget.linkNotifier?.pulledExtent ?? 0;
  double get _riggerPullDistance =>
      widget.linkNotifier?.refreshTriggerPullDistance ?? 0;
  Duration get _completeDuration =>
      widget.linkNotifier?.completeDuration ?? Duration(milliseconds: 0);
  AxisDirection? get _axisDirection => widget.linkNotifier?.axisDirection;

  AnimationController? _scaleController;
  Animation<double>? _scaleFactor;

  @override
  void initState() {
    super.initState();
    _scaleController = AnimationController(vsync: this);
    _scaleFactor = _scaleController?.drive(_oneToZeroTween);
  }

  @override
  void dispose() {
    super.dispose();
    _scaleController?.dispose();
  }

  bool _refreshFinish = false;
  set refreshFinish(bool finish) {
    if (_refreshFinish != finish) {
      if (finish) {
        Future.delayed(_completeDuration
            - Duration(milliseconds: 300), () {
          if (mounted) {
            _scaleController?.animateTo(1.0, duration: _kIndicatorScaleDuration);
          }
        });
        Future.delayed(_completeDuration, () {
          _refreshFinish = false;
          _scaleController?.animateTo(0.0, duration: Duration(milliseconds: 10));
        });
      }
      _refreshFinish = finish;
    }
  }

  @override
  Widget build(BuildContext context) {
    bool isVertical = _axisDirection == AxisDirection.down
        || _axisDirection == AxisDirection.up;
    bool isReverse = _axisDirection == AxisDirection.up
        || _axisDirection == AxisDirection.left;
    double? indicatorValue = _pulledExtent / (_riggerPullDistance / 0.75);
    indicatorValue = indicatorValue < 0.75 ? indicatorValue : 0.75;
    if (_refreshState == RefreshMode.refreshed) {
      refreshFinish = true;
    }
    return Container(
      height: isVertical ? _refreshState == RefreshMode.inactive
          ? 0.0 : _pulledExtent : double.infinity,
      width: !isVertical ? _refreshState == RefreshMode.inactive
          ? 0.0 : _pulledExtent : double.infinity,
      child: Stack(
        children: <Widget>[
          Positioned(
            top: isVertical ? isReverse ? 0.0 : null : 0.0,
            bottom: isVertical ? !isReverse ? 0.0 : null : 0.0,
            left: !isVertical ? isReverse ? 0.0 : null : 0.0,
            right: !isVertical ? !isReverse ? 0.0 : null : 0.0,
            child: Container(
              padding: EdgeInsets.only(
                top: isVertical ? isReverse ? 0.0 : widget.displacement : 0.0,
                bottom: isVertical ? !isReverse ? 0.0
                    : widget.displacement : 0.0,
                left: !isVertical ? isReverse ? 0.0 : widget.displacement : 0.0,
                right: !isVertical ? !isReverse ? 0.0
                    : widget.displacement : 0.0,
              ),
              alignment: isVertical ? isReverse ? Alignment.topCenter
                  : Alignment.bottomCenter : isReverse
                  ? Alignment.centerLeft : Alignment.centerRight,
              child: ScaleTransition(
                scale: _scaleFactor!,
                child: RefreshProgressIndicator(
                  value: _refreshState == RefreshMode.armed
                      || _refreshState == RefreshMode.refresh
                      || _refreshState == RefreshMode.refreshed
                      || _refreshState == RefreshMode.done
                      ? null
                      : indicatorValue,
                  valueColor: widget.valueColor,
                  backgroundColor: widget.backgroundColor,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}