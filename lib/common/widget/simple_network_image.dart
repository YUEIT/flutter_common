import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/utils/dimens_extension.dart';

/// 网络图加载widget
class SimpleNetworkImage extends StatelessWidget {

  final String imageUrl;
  final double? width;
  final double? height;
  final Widget? placeholder;
  final Widget? errorWidget;
  final BoxFit? fit;
  SimpleNetworkImage({required this.imageUrl, this.width, this.height, this.fit,
    this.placeholder, this.errorWidget});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: height,
      width: width,
      child: CachedNetworkImage(
        imageUrl: imageUrl,
        fit: fit ?? BoxFit.cover,
        placeholder: (context, url) => placeholder ?? CupertinoActivityIndicator(
          animating: true,
          radius: 10.rdp,
        ),
        errorWidget: (context, url, error) => errorWidget ?? Container(),
      ),
    );
  }

}