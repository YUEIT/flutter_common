import 'package:flutter/material.dart';
import 'package:flutter_boost/boost_navigator.dart';
import 'package:flutter_common/common/utils/dimens_extension.dart';
import 'package:flutter_common/common/constant/colors.dart';

/// 标题栏
class TopBanner extends StatelessWidget implements PreferredSizeWidget {

  const TopBanner({
    Key? key,
    this.title = "",
    this.titleBold = true,
    this.hasBottomLine = false,
    this.backgroundColor = AppColors.themeLightWhite,
    this.actions,
    this.onBackPressed,
    this.noActionBar = false,
    this.titleBar
  }): super(key: key);

  final String title;
  final bool titleBold;
  final bool hasBottomLine;
  final Color backgroundColor;
  final List<Widget>? actions;
  final VoidCallback? onBackPressed;
  final bool noActionBar;
  final Widget? titleBar;

  bool _isLightTheme() {
    return backgroundColor.value == AppColors.themeLightWhite.value;
  }

  void _close(BuildContext context) {
    BoostNavigator.of().pop();
  }

  @override
  Widget build(BuildContext context) {
    return AppBar(
      elevation: 0,
      backgroundColor: backgroundColor,
      brightness: _isLightTheme()? Brightness.light : Brightness.dark,
      title: titleBar ?? Text(
        title,
        style: TextStyle(
            fontSize: 18.rsp,
            fontStyle: FontStyle.normal,
            color: _isLightTheme()? AppColors.themeDarkBlack : AppColors.themeLightWhite,
            fontWeight: titleBold? FontWeight.bold : FontWeight.normal
        ),
        textAlign: TextAlign.center,
      ),
      centerTitle: true,
      leading: IconButton(
        iconSize: 18,
        icon: Icon(
          Icons.arrow_back_ios,
          color:  _isLightTheme()? AppColors.themeDarkBlack : AppColors.themeLightWhite,
        ),
        onPressed: onBackPressed?? (){_close(context);},
      ),
      actions: actions,
      bottom: hasBottomLine? PreferredSize (
        preferredSize: Size.fromHeight(1),
        child: Theme (
          data: Theme.of(context).copyWith(accentColor: AppColors.themeLightWhite),
          child: new Container(
            height: 1,
            color: AppColors.themeDarkBlack,
          ),
        ),
      ): null,
    );
  }

  @override
  Size get preferredSize => Size.fromHeight((noActionBar? 0.0 : kToolbarHeight) + (hasBottomLine? 1 : 0));

}