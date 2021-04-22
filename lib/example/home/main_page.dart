import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/top_banner.dart';
import 'package:flutter_common/common/router/route.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/example/base/example_router_path.dart';

import 'home_page.dart';
import 'list_page.dart';
import 'user_page.dart';


@ARoute(url: ExampleRouterPath.MAIN_PAGE)
class MainPage extends StatefulWidget {

  MainPage(ARouteOption option);

  @override
  State<StatefulWidget> createState() {
    return _MainPageState();
  }

}

class _MainPageState extends LifecycleState<MainPage> {

  @override
  Widget build(BuildContext context) {
    return Scaffold (
      backgroundColor: Colors.white,
      appBar: TopBanner(noActionBar: true,),
      body: IndexedStack(
        index: _currentIndex,
        children: [
          HomePage(),
          ListPage(),
          UserPage(),
        ],
      ),
      bottomNavigationBar: BottomNavigationBar (
        items: <BottomNavigationBarItem>[
          _buildItem(Icons.account_balance, "HOME"),
          _buildItem(Icons.apps, "LIST"),
          _buildItem(Icons.account_box, "USER"),
        ],
        currentIndex: _currentIndex,
        selectedIconTheme: IconThemeData(
          color: Colors.red,
          size: 25
        ),
        selectedLabelStyle: TextStyle(
          color: Colors.red,
          fontSize: 12
        ),
        onTap: _changeTab,
      ),
    );
  }

  int _currentIndex = 0;

  void _changeTab(int index) {
    setState(() {
      _currentIndex = index;
    });
  }

  BottomNavigationBarItem _buildItem(IconData iconData, String title) {
    return BottomNavigationBarItem(
        icon: Icon(
          iconData,
          size: 25,
          color: Colors.black12,
        ),
        activeIcon: Icon(
          iconData,
          size: 25,
          color: Colors.red,
        ),
        title: Text(
          title,
          style: TextStyle(
              color: Colors.black12,
              fontSize: 12
          ),
        )
    );
  }

  Widget _contentWidget(int index) {
    if (index == 0) {
      return HomePage();
    } else if (index == 1) {
      return ListPage();
    } else if (index == 2) {
      return UserPage();
    }
    return Container();
  }
}