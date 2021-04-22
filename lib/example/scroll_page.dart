import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/base_scaffold.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/components/normal_widget.dart';
import 'package:flutter_common/common/components/refresh.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'package:flutter_common/common/router/route.dart';
import 'package:flutter_common/example/base/example_router_path.dart';
import 'package:flutter_common/example/vm/scroll_view_model.dart';

@ARoute(url: ExampleRouterPath.SCROLL_PAGE)
class ScrollPage extends StatefulWidget {
  final ARouteOption option;

  ScrollPage(this.option);

  @override
  State<StatefulWidget> createState() {
    return _ScrollPageState();
  }
}

class _ScrollPageState extends ViewModelState<ScrollPage, ScrollViewModel>
    with SingleTickerProviderStateMixin {

  TabController? tabController;

  @override
  ScrollViewModel initViewModel() {
    return ScrollViewModel();
  }

  @override
  void initState() {
    super.initState();
    tabController = TabController(length: 3, vsync: this);
    viewModel.refresh();
  }

  @override
  Widget build(BuildContext context) {
    return BaseScaffold(
        body: NormalPullWidget(
            pullViewModel: viewModel,
            buildWidget: (BuildContext context) {
              print("buildWidget");
              return LiveStreamBuilder<dynamic>(
                liveData: viewModel.data,
                builder: (context, data) {
                  // return RefreshIndicator(
                  //     notificationPredicate: (ScrollNotification notification) {
                  //       return true;
                  //     },
                  //     child: NestedScrollViewWidget(tabController!),
                  //     onRefresh: () async {}
                  // );
                  return NestedScrollViewWidget(tabController!);
                  // return RefreshWidget(
                  //     onRefresh: () async {
                  //       viewModel.refresh();
                  //     },
                  //     child: Container (
                  //       height: 600,
                  //       child:
                  //     );
                },
              );
            }
        )
    );
  }

}

class CustomScrollViewWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return CustomScrollView(
      scrollDirection: Axis.vertical,
      slivers: <Widget>[
        SliverAppBar(
          floating: false,
          pinned: true,
          expandedHeight: 250.0,
          backgroundColor: Colors.blue,
          flexibleSpace: FlexibleSpaceBar(
            background: Image.network(
              "http://b-ssl.duitang.com/uploads/blog/201312/04/20131204184148_hhXUT.jpeg",
              fit: BoxFit.cover,
            ),
          ),
        ),
        SliverPadding(
          padding: EdgeInsets.all(20),
          sliver: SliverGrid(
            delegate: SliverChildBuilderDelegate(
                  (BuildContext context, int index) {
                return Container(
                  color: index % 2 == 0 ? Colors.greenAccent : Colors.yellow,
                  alignment: Alignment.center,
                  child: Text("$index"),
                );
              },
              childCount: 8,
            ),
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                crossAxisSpacing: 20,
                mainAxisSpacing: 20,
                childAspectRatio: 1),
          ),
        ),
        SliverFixedExtentList(
            delegate:
            SliverChildBuilderDelegate((BuildContext context, int index) {
              return Container(
                color: Colors.red,
                alignment: Alignment.center,
                child: Text("$index"),
              );
            }, childCount: 20),
            itemExtent: 50),
      ],
    );
  }
}

class NestedScrollViewWidget extends StatelessWidget {
  NestedScrollViewWidget(this.tabController);
  GlobalKey? ley;
  final TabController tabController;

  @override
  Widget build(BuildContext context) {
    return NestedScrollView(
        headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
          return [
            SliverAppBar(
              expandedHeight: 250,
              pinned: false,
              backgroundColor: Colors.blue,
              flexibleSpace: FlexibleSpaceBar(
                background: Image.network(
                  "http://b-ssl.duitang.com/uploads/blog/201312/04/20131204184148_hhXUT.jpeg",
                  fit: BoxFit.cover,
                ),
              ),
              bottom: TabBar(
                controller: tabController,
                tabs: [
                  Tab(
                    text: "标签一",
                  ),
                  Tab(
                    text: "标签二",
                  ),
                  Tab(
                    text: "标签三",
                  )
                ],
              ),
            )
          ];
        },
        body: TabBarView(
          controller: tabController,
          children: [
            MediaQuery.removePadding(
              context: context,
              removeTop: true,
              child: RefreshWidget(
                onRefresh: () async {

                },
                child: ListView.builder(
                  itemBuilder: (BuildContext context, int index) {
                    return Container(
                      child: Text("$index"),
                      height: 100,
                      color: index % 2 == 0 ? Colors.red : Colors.blue,
                    );
                  },
                  itemCount: 20,
                ),
              ),
            ),
            Text("页面二"),
            Text("页面三")
          ],
        ));
  }
}
