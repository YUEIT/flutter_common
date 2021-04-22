import 'package:flutter_common/common/components/vm/data/page_mode.dart';

class ListMode extends PageMode<Order>{
    List<Order>? orderList;

    ListMode({this.orderList});

    factory ListMode.fromJson(Map<String, dynamic> json) {
        return ListMode(
            orderList: json['orderList'] != null ? (json['orderList'] as List).map((i) => Order.fromJson(i)).toList() : null, 
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        if (this.orderList != null) {
            data['orderList'] = this.orderList!.map((v) => v.toJson()).toList();
        }
        return data;
    }

  @override
  List<Order> getDataList() {
    return orderList ?? [];
  }

  @override
  int getPageNo() {
    return 0;
  }

  @override
  int getPageSize() {
    return 7;
  }

  @override
  bool isEmpty() {
    return orderList == null || orderList!.isEmpty;
  }

  @override
  bool isEnd() {
      return false;
  }

}

class Order {
    String? des;
    int? id;
    String? image;
    String? title;
    bool? isCollect;

    Order({this.des, this.id, this.image, this.title});

    factory Order.fromJson(Map<String, dynamic> json) {
        return Order(
            des: json['des'],
            id: json['id'],
            image: json['image'],
            title: json['title'],
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        data['des'] = this.des;
        data['id'] = this.id;
        data['image'] = this.image;
        data['title'] = this.title;
        return data;
    }
}