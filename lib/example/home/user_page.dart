import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/lifecycle_state.dart';
import 'package:flutter_common/common/constant/colors.dart';
import 'package:flutter_common/example/mode/share_provider.dart';
import 'package:provider/provider.dart';

class UserPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return _UserPageState();
  }
}

class _UserPageState extends LifecycleState<UserPage> {

  @override
  Widget build(BuildContext context) {
    final shareModel = Provider.of<ShareModel>(context);
    return Container(
      margin: EdgeInsets.only(top: 40),
      child: Column(
        children: <Widget>[
          _userWidget(),
          Card(
            margin: EdgeInsets.only(top: 20, left: 20, right: 20),
            child: Column(
              children: <Widget>[
                _optionWidget("change item color", (){
                  shareModel.setColor(Colors.red);
                }),
                Divider(height: 1, color: Color(0xffeeeeee),),
                _optionWidget("title", (){}),
                Divider(height: 1, color: Color(0xffeeeeee),),
                _optionWidget("title", (){}),
                Divider(height: 1, color: Color(0xffeeeeee),),
                _optionWidget("title", (){}),
              ],
            ),
          )
        ],
      ),
    );
  }

  Widget _userWidget() {
    return Card(
      margin: EdgeInsets.only(left: 20, right: 20),
      child: Container(
        height: 100,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Container(
              width: 50,
              height: 50,
              alignment: Alignment.centerLeft,
              margin: EdgeInsets.only(left: 20, right: 20),
              decoration: BoxDecoration(
                  color: Colors.black26,
                  borderRadius: BorderRadius.all(Radius.circular(5))
              ),
            ),
            Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Container(
                      child: Text("user"),
                      alignment: Alignment.centerLeft,
                    ),
                    Container(
                      child: Text("user descrption"),
                      margin: EdgeInsets.only(top: 10),
                      alignment: Alignment.centerLeft,
                    )
                  ],
                )
            )
          ],
        ),
      )
    );
  }

  Widget _optionWidget(String title, GestureTapCallback? function) {
    return GestureDetector(
      onTap: function,
      child: Container(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Expanded(
                  child: Container(
                    child: Text(
                        title,
                      style: TextStyle(
                        color: AppColors.themeDarkBlack,
                        fontSize: 15
                      ),
                    ),
                    margin: EdgeInsets.only(top: 10, bottom: 10, left: 20),
                  )
              ),
              Container(
                child: Icon(
                    Icons.arrow_right
                ),
                margin: EdgeInsets.only(right: 10),
              )
            ],
          ),
      )
    );
  }
}