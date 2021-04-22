import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/vm/data/page_state.dart';
import 'package:flutter_common/common/utils/dimens_extension.dart';

typedef ReloadCallback = Future<void> Function();

/// 各加载状态页面
class ScaffoldContainer extends StatelessWidget{

  ScaffoldContainer({
    required this.content,
    this.currentStateEvent,
    required this.reload,
    this.stateWidget,
  });

  final Widget content;
  final ReloadCallback reload;
  final PageStateEvent? currentStateEvent;
  final PageStateWidget? stateWidget;
  PageState? currentState;

  void _reload() async {
    await reload();
  }

  @override
  Widget build(BuildContext context) {
    currentState = currentStateEvent?.state ?? PageState.loading;
    return Center(
      child: _contentWidget(),
    );
  }

  Widget _contentWidget() {
    switch (currentState) {
      case PageState.loading:
        return stateWidget?.loadingWidget ?? buildLoading();
      case PageState.normal:
        return content;
      case PageState.noData:
        return stateWidget?.noDataWidget ?? buildNoData();
      case PageState.error:
        return stateWidget?.errorWidget ?? buildError();
      case PageState.noNet:
        return stateWidget?.noNetWidget ?? buildNoNet();
      case PageState.unKnown:
      default:
        return buildUnKnown();
    }
  }

  Widget buildLoading() {
    return Container(
      child: CupertinoActivityIndicator(
        animating: true,
        radius: 20,
      ),
    );
  }

  Widget buildNoData() {
    return GestureDetector(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Container(
            margin: EdgeInsets.only(top: 15),
            child: Text(
              "暂无数据",
              style: TextStyle(
                  fontSize: 15.rsp,
                  color: Color(0xff888888)
              ),
            ),
          )
        ],
      ),
      onTap: _reload,
    );
  }

  Widget buildError() {
    return GestureDetector(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Container(
            margin: EdgeInsets.only(top: 15),
            child: Text(
              currentStateEvent?.showInfo ?? "服务器出错了~",
              style: TextStyle(
                  fontSize: 15.rsp,
                  color: Color(0xff888888)
              ),
            ),
          )
        ],
      ),
      onTap: _reload,
    );
  }

  Widget buildNoNet() {
    return GestureDetector(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Container(
            margin: EdgeInsets.only(top: 15),
            child: Text(
              "无网络，请先检查网络连接",
              style: TextStyle(
                  fontSize: 15.rsp,
                  color: Color(0xff888888)
              ),
            ),
          )
        ],
      ),
      onTap: _reload,
    );
  }

  Widget buildUnKnown() {
    return Center();
  }

}

class PageStateWidget{
  final Widget? loadingWidget;
  final Widget? noDataWidget;
  final Widget? errorWidget;
  final Widget? noNetWidget;
  PageStateWidget({this.loadingWidget, this.noDataWidget, this.errorWidget, this.noNetWidget});
}