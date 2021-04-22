import 'dart:ui';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/normal_widget.dart';
import 'package:flutter_common/common/widget/simple_network_image.dart';
import 'package:flutter_common/example/mode/home_mode.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/example/mode/share_provider.dart';
import 'package:flutter_common/example/vm/home_view_model.dart';
import 'package:provider/provider.dart';

class HomePage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }

}

class _HomePageState extends LifecycleState<HomePage> {

  HomeViewModel homeViewModel = HomeViewModel();

  @override
  void initState() {
    super.initState();
    _refresh();
  }

  void _refresh() {
    homeViewModel.refresh();
  }

  @override
  Widget build(BuildContext context) {
    final _color = Provider.of<ShareModel>(context).color;
    return SimplePullWidget(
      pullViewModel: homeViewModel,
      buildWidget: (context, data) {
        HomeMode homeMode = data as HomeMode;
        return ListView.builder(
            itemCount: homeMode.homeData?.length,
            itemBuilder: (BuildContext context, int position) {
              return _listWidget(context, position, homeMode.homeData![position]);
            }
        );
      },
    );
  }

  Widget _listWidget(BuildContext context, int position, HomeData homeData) {
    switch(homeData.type) {
      case 0: return _bannerWidget(context, homeData);
      case 1: return _menuWidget(context, homeData);
      case 2: return _goodsWidget(context, homeData);
      default: return Container();
    }
  }

  Widget _bannerWidget(BuildContext context, HomeData homeData) {
    return Container(
      height: 200,
      child: PageView.builder(
        itemBuilder: (context, index) {
          return Stack(
            children: <Widget>[
              SimpleNetworkImage(
                imageUrl: "https://images.ypcang.com/1590727824662.png",
                height: double.infinity,
                width: double.infinity,
              ),
              Container(
                alignment: AlignmentDirectional.bottomEnd,
                 margin: EdgeInsets.only(right: 10, bottom: 10),
                 child: Text(
                      homeData.banner?[index].title ?? ""
                  )
              )
            ],
          );
        },
        itemCount: homeData.banner!.length,
        scrollDirection: Axis.horizontal,
        reverse: false,
        physics: PageScrollPhysics(parent: BouncingScrollPhysics()),
      )
    );
  }

  Widget _menuWidget(BuildContext context, HomeData homeData) {
    return Container(
      margin: EdgeInsets.only(top: 10, bottom: 10),
      child: GridView.builder(
          itemCount: homeData.menu!.length,
          shrinkWrap: true,
          gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 4,
              mainAxisSpacing: 10,
              crossAxisSpacing: 5,
              childAspectRatio: 1
          ),
          physics: NeverScrollableScrollPhysics(),
          itemBuilder: (context, position) {
            return _menuItemWidget(context, homeData.menu![position]);
          }
      ),
    );
  }

  Widget _menuItemWidget(BuildContext context, Menu menu) {
      return Container(
        child: Column(
          children: <Widget>[
            Expanded(
              child: Container(
                width: double.infinity,
                decoration: BoxDecoration(
                    color: Colors.black26
                ),
                child: Image.network(
                  menu.image ?? "",
                ),
              ),
            ),
            Container(
              alignment: Alignment.center,
              child: Text(menu.title ?? ""),
            )
          ],
        ),
      );
  }

  Widget _goodsWidget(BuildContext context, HomeData homeData) {
    return GridView.builder(
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
          mainAxisSpacing: 10,
          crossAxisSpacing: 10,
          childAspectRatio: 1
        ),
        shrinkWrap: true,
        physics: NeverScrollableScrollPhysics(),
        itemCount: homeData.goods?.length ?? 0,
        itemBuilder: (context, position) {
          Good goods = homeData.goods![position];
          return Container(
            child: Column(
              children: <Widget>[
                Expanded(
                  child: Container(
                    width: double.infinity,
                    decoration: BoxDecoration(
                        color: Colors.black26
                    ),
                    child: Image.network(
                      goods.image ?? "",
                    ),
                  ),
                ),
                Container(
                  alignment: Alignment.center,
                  child: Text(goods.name ?? ""),
                )
              ],
            )
          );
        }
    );
  }

}