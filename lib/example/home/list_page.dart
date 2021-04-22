import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/normal_widget.dart';
import 'package:flutter_common/example/mode/list_mode.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/example/mode/share_provider.dart';
import 'package:provider/provider.dart';
import 'package:flutter_common/example/vm/list_view_model.dart';

class ListPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _ListPageState();
  }
}

class _ListPageState extends LifecycleState<ListPage> {
  ListViewModel orderViewModel = ListViewModel();

  @override
  void initSubscribe() {
    lifecycleProvider.onSubscribe(orderViewModel);
  }

  @override
  void initState() {
    super.initState();
    _refresh();
  }

  void _refresh() {
    orderViewModel.refresh();
  }

  @override
  Widget build(BuildContext context) {
    return _listWidget(context);
  }

  Widget _listWidget(BuildContext context) {
    return SimplePageWidget(
      pageViewModel: orderViewModel,
      buildItemWidget: (context, data, position) {
        Order order = data as Order;
        return _itemWidget(context, order, position);
      },
    );
  }

  Widget _itemWidget(BuildContext context, Order order, int position) {
    return Container(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Consumer<ShareModel>(
            builder: (context, ShareModel shareModel, child) => Container(
              height: 100,
              width: 100,
              margin: EdgeInsets.all(10),
              decoration: BoxDecoration(color: shareModel.color),
              alignment: Alignment.center,
              child: Text(
                position.toString(),
                style: TextStyle(color: Colors.white, fontSize: 18),
              ),
            ),
          ),
          Expanded(
              child: Container(
                margin: EdgeInsets.only(top: 10),
                child: Column(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Container(
                      child: Text(order.title ?? ""),
                      alignment: Alignment.topLeft,
                    ),
                    Container(
                      margin: EdgeInsets.only(top: 10),
                      child: Text(order.des ?? ""),
                      alignment: Alignment.topLeft,
                    )
                  ],
                ),
          ))
        ],
      ),
    );
  }
}
