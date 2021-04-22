import 'package:flutter/rendering.dart';
import 'dart:math' as math;

import 'package:flutter/widgets.dart';

class RenderCloudParentData extends ContainerBoxParentData<RenderBox> {
  double width = 0;
  double height = 0;
  Rect get content => Rect.fromLTWH(offset.dx, offset.dy, width, height);
}

class RenderCloudWidget extends RenderBox with ContainerRenderObjectMixin<RenderBox, RenderCloudParentData>,
    RenderBoxContainerDefaultsMixin<RenderBox, RenderCloudParentData> {

  RenderCloudWidget({
    List<RenderBox>? children,
    Overflow overflow = Overflow.visible,
    double ratio = 0
  }) : _ratio = ratio,
    _overflow = overflow {
    addAll(children);
  }

  double _mathPi = math.pi * 2;

  bool _needClip = false;

  Overflow get overflow => _overflow;
  Overflow _overflow;

  set overflow(Overflow value) {
    if (_overflow != value) {
      _overflow = value;
      markNeedsPaint();
    }
  }

  double _ratio;
  double get ratio => _ratio;

  set ratio(double value) {
    if(_ratio != value) {
      _ratio = value;
      markNeedsPaint();
    }
  }

  bool overlaps(RenderCloudParentData data) {
    Rect rect = data.content;
    RenderBox? child = data.previousSibling;
    if (child == null) {
      return false;
    }
    do {
      RenderCloudParentData childParentData = child!.parentData as RenderCloudParentData;
      if (rect.overlaps(childParentData.content)) {
        return true;
      }
      child = childParentData.previousSibling;
    } while(child != null);
    return false;
  }

  @override
  void setupParentData(RenderObject child) {
    if (child.parentData is! RenderCloudParentData) {
      child.parentData = RenderCloudParentData();
    }
  }

  @override
  void performLayout() {
    _needClip = false;
    if(childCount == 0) {
      size = constraints.smallest;
      return;
    }
    var recordRect = Rect.zero;
    var previousChildRect = Rect.zero;
    RenderBox? child = firstChild;
    while(child != null) {
      var curIndex = -1;
      final RenderCloudParentData childParentData = child.parentData as RenderCloudParentData;
      child.layout(constraints, parentUsesSize: true);
      var childSize = child.size;
      childParentData.width = childSize.width;
      childParentData.height = childSize.height;
      do {
        var rx = ratio >= 1 ? ratio : 1.0;
        var ry = ratio >= 1 ? ratio : 1.0;
        var step = 0.02 * _mathPi;
        var rotation = 0.0;
        var angle = curIndex * step;
        var angleRadius = 5 + 5 * angle;
        var x = rx * angleRadius * math.cos(angle + rotation);
        var y = ry * angleRadius * math.sin(angle + rotation);
        var position = Offset(x, y);
        var childOffset = position - Alignment.center.alongSize(childSize);
        ++curIndex;
        childParentData.offset = childOffset;
      } while (overlaps(childParentData));
      previousChildRect = childParentData.content;
      recordRect = recordRect.expandToInclude(previousChildRect);
      child = childParentData.nextSibling;
    }
    size = constraints.tighten(height: recordRect.height, width: recordRect.width).smallest;
    var contentCenter = size.center(Offset.zero);
    var recordRectCenter = recordRect.center;
    var transCenter = contentCenter - recordRectCenter;
    child = firstChild;
    while(child != null) {
      final RenderCloudParentData childParentData = child.parentData as RenderCloudParentData;
      childParentData.offset += transCenter;
      child = childParentData.nextSibling;
    }
    _needClip = size.width < recordRect.width || size.height < recordRect.height;
  }

  @override
  void paint(PaintingContext context, Offset offset) {
    if (!_needClip || _overflow != Overflow.clip) {
      defaultPaint(context, offset);
    } else {
      context.pushClipRect(needsCompositing, offset, Offset.zero & size, defaultPaint);
    }
  }

  @override
  double computeDistanceToActualBaseline(TextBaseline baseline) {
    return defaultComputeDistanceToHighestActualBaseline(baseline) ?? 0;
  }

  @override
  bool hitTestChildren(BoxHitTestResult result, {required Offset position}) {
    return defaultHitTestChildren(result, position: position);
  }
}

class CloudWidget extends MultiChildRenderObjectWidget {
  final Overflow overflow;
  final double ratio;

  CloudWidget({
    Key? key,
    this.ratio = 1,
    this.overflow = Overflow.clip,
    List<Widget> children = const <Widget>[]
  }) : super(key: key, children: children);

  @override
  RenderObject createRenderObject(BuildContext context) {
    return RenderCloudWidget(
      ratio: ratio,
      overflow: overflow,
    );
  }

  @override
  void updateRenderObject(BuildContext context, RenderObject renderObject) {
    renderObject as RenderCloudWidget
      ..ratio = ratio
      ..overflow = overflow;
  }
}