import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/base_scaffold.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/components/provider/dialog_provider.dart';
import 'package:flutter_common/common/components/top_banner.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/router/route.dart';
import 'package:flutter_common/common/widget/common_dialog.dart';
import 'package:flutter_common/common/widget/global_dialog.dart';
import 'package:flutter_common/example/base/example_router_path.dart';
import 'package:flutter_common/example/vm/option_view_model.dart';

@ARoute(url: ExampleRouterPath.OPTION_PAGE)
class OptionPage extends StatefulWidget {
  OptionPage(ARouteOption option);

  @override
  State<StatefulWidget> createState() {
    return _OptionPage();
  }
}

class _OptionPage extends ViewModelState<OptionPage, OptionViewModel> {

  @override
  OptionViewModel initViewModel() {
    return OptionViewModel();
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return BaseScaffold(
      appBar: TopBanner(
        title: "option",
      ),
      body: Center(
        child: Column(
          children: <Widget>[
            IconButton(
              icon: Icon(Icons.ac_unit),

              color: Color(0xFF00FF00),
              onPressed: viewModel.toggle,
            ),
            LiveStreamBuilder<bool>(
                liveData: viewModel.iconStatus,
                builder: (BuildContext context, bool status) {
                  return IconButton(
                    icon: Icon(status
                        ? Icons.accessibility
                        : Icons.access_alarm),
                    color: Color(0xFF00FF00),
                    onPressed: viewModel.toggle,
                  );
                }
            ),
          ],
        ),
      ),
      onWillPop: () {
        return _showGlobalDialog();
      },
    );
  }

  Future<bool> _showGlobalDialog() async {
    return await showGlobalDialog(
        context: context,
        builder: (dialogContext, globalKey) {
          return GlobalDialog(
              dialogContext,
              globalKey: globalKey,
              builder: (dialogContext) {
                return CommonDialog(
                  dialogContext,
                  title: "确认退出当前页面",
                  leftStr: "取消",
                  leftClick: () {
                    Navigator.of(dialogContext).pop(false);
                  },
                  rightStr: "退出",
                  rightClick: () {
                    Navigator.of(dialogContext).pop(true);
                  },
                );
              }
          );
        }
    );
  }
}






