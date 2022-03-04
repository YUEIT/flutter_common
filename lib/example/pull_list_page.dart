import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_common/common/components/base_scaffold.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/components/normal_widget.dart';
import 'package:flutter_common/common/components/top_banner.dart';
import 'package:flutter_common/common/router/navigation/platform_navigator.dart';
import 'package:flutter_common/common/router/router.dart';
import 'package:flutter_common/example/base/example_router_path.dart';
import 'package:flutter_common/example/vm/pull_list_view_model.dart';
import 'mode/pull_list_mode.dart';
import '../common/components/refresh.dart';
import '../common/components/vm/live_data.dart';
import '../common/components/provider/mode_provider.dart';

@ARoute(url: ExampleRouterPath.PULL_LIST_PAGE)
class PullListPage extends StatefulWidget {
  final ARouteOption option;

  PullListPage(this.option) {
    print("get" + this.option.params["id"].toString());
  }

  @override
  State<StatefulWidget> createState() {
    return _PullListPageState();
  }
}

class _PullListPageState extends LifecycleState<PullListPage> {
  PullListViewModel userViewModel = PullListViewModel();

  @override
  void initState() {
    super.initState();
    _request();
  }

  @override
  Widget build(BuildContext context) {
    return BaseScaffold(
      appBar: TopBanner(
        title: "title",
        onBackPressed: () {
          PlatformNavigator.instance.close({"param": "param"});
        },
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.add),
            color: Colors.black,
            onPressed: () {
              userViewModel.addUser();
            },
          ),
        ],
      ),
      body: NormalPullWidget(
        pullViewModel: userViewModel,
        buildWidget: _content,
      ),
    );
  }

  Widget _content(BuildContext context) {
    return LiveStreamBuilder<List<PullListMode>>(
      liveData: userViewModel.data,
      builder: (context, userModel) {
        return RefreshWidget(
            onRefresh: () async {
              userViewModel.refresh();
            },
            child: ListView.builder(
                itemCount: userModel.length,
                itemBuilder: (BuildContext context, int position) {
                  return _listWidget(context, position, userModel[position]);
                }));
      },
    );
  }

  Widget _listWidget(BuildContext context, int position, PullListMode mData) {
    // return ItemWidget(position, mData, (UserModel data) {
    //   setState(() {
    //     data.isSelected = !data.isSelected;
    //   });
    //   // _showDialog();
    // });
    return new ItemWidget2(mData);
  }

  void _request() {
    userViewModel.refresh();
  }
}

class ItemWidget extends StatefulWidget {
  final int position;
  final PullListMode userModel;
  final ValueChanged<PullListMode> _changed;

  ItemWidget(this.position, this.userModel, this._changed);

  @override
  _ItemWidgetState createState() => _ItemWidgetState();
}

class _ItemWidgetState extends State<ItemWidget> {
  @override
  Widget build(BuildContext context) {
    print(ModeProvider.of<PullListViewModel>(context).data.value.toString());
    return Container(
      child: Row(
        children: <Widget>[
          Container(
            child: Image.asset(
              "images/ic_coupons_left.png",
              width: 50,
              height: 50,
            ),
            margin: EdgeInsets.all(10),
          ),
          Expanded(
            flex: 1,
            child: Container(
              child: Text(
                widget.userModel.name ?? "",
                style: TextStyle(
                    fontStyle: FontStyle.normal,
                    fontSize: 10,
                    color: Colors.black),
              ),
              alignment: Alignment.centerLeft,
            ),
          ),
          Container(
            alignment: Alignment.centerRight,
            margin: EdgeInsets.only(right: 10),
            child: IconButton(
              icon: Icon(widget.userModel.isSelected
                  ? Icons.favorite
                  : Icons.favorite_border),
              onPressed: () {
                widget._changed(widget.userModel);
              },
            ),
          )
        ],
      ),
    );
  }
}

class ItemWidget2 extends StatelessWidget {
  final PullListMode userModel;
  final LiveData<PullListMode> _liveData = LiveData();

  ItemWidget2(this.userModel) {
    _liveData.setValue(userModel);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        children: <Widget>[
          Container(
            child: Icon(
              Icons.title,
              size: 50,
            ),
            margin: EdgeInsets.all(10),
          ),
          Expanded(
            flex: 1,
            child: Container(
              child: Text(
                userModel.name ?? "",
                style: TextStyle(
                    fontStyle: FontStyle.normal,
                    fontSize: 10,
                    color: Colors.black),
              ),
              alignment: Alignment.centerLeft,
            ),
          ),
          Container(
              alignment: Alignment.centerRight,
              margin: EdgeInsets.only(right: 10),
              child: LiveStreamBuilder<PullListMode>(
                liveData: _liveData,
                builder: (context, data) {
                  return IconButton(
                    icon: Icon(data.isSelected
                        ? Icons.favorite
                        : Icons.favorite_border),
                    onPressed: () {
                      data.isSelected = !data.isSelected;
                      _liveData.setValue(data);
                    },
                  );
                },
              ))
        ],
      ),
    );
  }
}
