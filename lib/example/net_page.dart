import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/components/top_banner.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/router/route.dart';
import 'package:flutter_common/example/base/example_router_path.dart';
import 'package:flutter_common/example/vm/net_view_model.dart';

@ARoute(url: ExampleRouterPath.NET_PAGE)
class NetPage extends StatefulWidget {

  NetPage(ARouteOption option);

  @override
  State<StatefulWidget> createState() {
    return _NetPageState();
  }
}

class _NetPageState extends ViewModelState<NetPage, NetViewModel>  {

  @override
  NetViewModel initViewModel() {
    return NetViewModel();
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: TopBanner(
          title: "net test",
        ),
        body: ListView(
          children: [
            Container(
              child: ElevatedButton(
                child: Text("login"),
                onPressed: viewModel.login,
              ),
            ),
            LiveStreamBuilder<String>(
                liveData: viewModel.responseStr,
                builder: (context, data) {
                  return Text(data,
                    style: TextStyle(fontSize: 18, color: Colors.black),
                  );
                })
          ],
        )
    );
  }

  @override
  void dispose() {
    super.dispose();
  }

}
