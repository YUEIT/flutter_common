import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/base_scaffold.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/components/scaffold_widget.dart';
import 'package:flutter_common/common/components/top_banner.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/router/route.dart';
import 'package:flutter_common/example/base/example_router_path.dart';
import 'package:flutter_common/common/utils/dimens_extension.dart';
import 'package:flutter_common/example/vm/debug_view_mode.dart';

@ARoute(url: ExampleRouterPath.DEBUG_PAGE)
class DebugPage extends StatefulWidget {
  DebugPage(ARouteOption option);

  @override
  State<StatefulWidget> createState() {
    return DebugState();
  }
}

class DebugState extends ViewModelState<DebugPage, DebugViewModel> {
  @override
  DebugViewModel initViewModel() {
    return DebugViewModel();
  }

  @override
  void initState() {
    super.initState();
    viewModel.ipController.text = viewModel.proxyClient;
    viewModel.codeController.text = viewModel.proxyClientCode;
  }

  @override
  Widget build(BuildContext context) {
    return BaseScaffold(
      appBar: TopBanner(
        title: "调试设置",
        actions: [
          Container(
            alignment: Alignment.center,
            margin: EdgeInsets.only(right: 15.rdp),
            child: GestureDetector(
              onTap: () {
                viewModel.saveState();
              },
              child: Text(
                "保存",
                style: TextStyle(fontSize: 18.rsp, color: Color(0xff333333)),
              ),
            ),
          )
        ],
      ),
      body: SingleChildScrollView(
        child: Container(
          margin: EdgeInsets.only(
              left: 15.rdp, right: 15.rdp, top: 15.rdp, bottom: 15.rdp),
          child: Column(
            children: [
              buildDebugWidget(),
              buildProxyWidget(),
              buildLogWidget(),
              buildNetLogWidget(),
              buildDetailLogWidget()
            ],
          ),
        ),
      ),
    );
  }

  Widget buildDebugWidget() {
    return Container(
      margin: EdgeInsets.only(top: 20.rdp),
      child: LiveStreamBuilder<bool>(
          liveData: viewModel.debug,
          builder: (context, open) {
            return Row(
              children: [
                Container(
                  width: 80.rdp,
                  child: Text(
                    "调试模式:",
                    style:
                        TextStyle(fontSize: 18.rsp, color: Color(0xff333333)),
                  ),
                ),
                CupertinoSwitch(
                    value: open,
                    onChanged: (data) {
                      viewModel.openDebug(data);
                    }),
              ],
            );
          }),
    );
  }

  Widget buildProxyWidget() {
    return Container(
      margin: EdgeInsets.only(top: 50.rdp),
      child: LiveStreamBuilder<bool>(
          liveData: viewModel.openProxy,
          builder: (context, open) {
            return Column(
              children: [
                Row(
                  children: [
                    Container(
                      child: Text(
                        "代理:",
                        style: TextStyle(
                            fontSize: 18.rsp, color: Color(0xff333333)),
                      ),
                      width: 80.rdp,
                    ),
                    CupertinoSwitch(
                        value: open,
                        onChanged: (data) {
                          viewModel.openProxySetting(data);
                        }),
                  ],
                ),
                Container(
                  margin: EdgeInsets.only(
                    top: 10.rdp,
                    left: 10.rdp
                  ),
                  child: Row(
                    children: [
                      Container(
                        child: Text(
                          "主机IP：",
                          style: TextStyle(
                              fontSize: 18.rsp,
                              color: Color(0xff333333)
                          ),
                        ),
                      ),
                      Expanded(
                          child: Container(
                            height: 30.rdp,
                            alignment: Alignment.centerLeft,
                            child: TextField(
                              decoration: null,
                              controller: viewModel.ipController,
                              keyboardType: TextInputType.number,
                            ),
                            decoration: BoxDecoration(
                              border: Border.all(color: Color(0xffff5f00), width: 1),
                              borderRadius: BorderRadius.all(Radius.circular(5)),
                            ),
                          ))
                    ],
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(
                      top: 10.rdp,
                      left: 10.rdp
                  ),
                  child: Row(
                    children: [
                      Container(
                        child: Text(
                          "端口号：",
                          style: TextStyle(
                              fontSize: 18.rsp,
                              color: Color(0xff333333)
                          ),
                        ),
                      ),
                      Expanded(
                          child: Container(
                            height: 30.rdp,
                            alignment: Alignment.centerLeft,
                            child: TextField(
                              decoration: null,
                              controller: viewModel.codeController,
                              keyboardType: TextInputType.number,
                            ),
                            decoration: BoxDecoration(
                              border: Border.all(color: Color(0xffff5f00), width: 1),
                              borderRadius: BorderRadius.all(Radius.circular(5)),
                            ),
                          ))
                    ],
                  ),
                )
              ],
            );
          }),
    );
  }

  Widget buildLogWidget() {
    return Container(
      margin: EdgeInsets.only(top: 50.rdp),
      child: LiveStreamBuilder<bool>(
          liveData: viewModel.showLog,
          builder: (context, open) {
            return Row(
              children: [
                Container(
                  width: 80.rdp,
                  child: Text(
                    "日志:",
                    style:
                        TextStyle(fontSize: 18.rsp, color: Color(0xff333333)),
                  ),
                ),
                CupertinoSwitch(
                    value: open,
                    onChanged: (data) {
                      viewModel.openLog(data);
                    }),
              ],
            );
          }),
    );
  }

  Widget buildNetLogWidget() {
    return Container(
      margin: EdgeInsets.only(
          top: 10.rdp,
          left: 10.rdp
      ),
      child: LiveStreamBuilder<bool>(
          liveData: viewModel.showNetLog,
          builder: (context, open) {
            return Row(
              children: [
                Container(
                  width: 80.rdp,
                  child: Text(
                    "网络日志:",
                    style:
                        TextStyle(fontSize: 18.rsp, color: Color(0xff333333)),
                  ),
                ),
                CupertinoSwitch(
                    value: open,
                    onChanged: (data) {
                      viewModel.openNetLog(data);
                    }),
              ],
            );
          }),
    );
  }

  Widget buildDetailLogWidget() {
    return Container(
      margin: EdgeInsets.only(
          top: 10.rdp,
          left: 10.rdp
      ),
      child: LiveStreamBuilder<bool>(
          liveData: viewModel.showDetailLog,
          builder: (context, open) {
            return Row(
              children: [
                Container(
                  width: 80.rdp,
                  child: Text(
                    "页面日志:",
                    style:
                        TextStyle(fontSize: 18.rsp, color: Color(0xff333333)),
                  ),
                ),
                CupertinoSwitch(
                    value: open,
                    onChanged: (data) {
                      viewModel.openDetailLog(data);
                    }),
              ],
            );
          }),
    );
  }
}
