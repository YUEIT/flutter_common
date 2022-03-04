import 'dart:convert';

import 'package:flutter_common/common/utils/map_extension.dart';

class HomeMode {
    List<HomeData>? homeData;

    HomeMode({this.homeData});

    factory HomeMode.fromJson(Map<String, dynamic> json) {
        return HomeMode(
            homeData: json.get('homeData', convert: (it) { return (it as List).map((i) => HomeData.fromJson(i)).toList();}),
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        if (this.homeData != null) {
            data['homeData'] = this.homeData!.map((v) => v.toJson()).toList();
        }
        return data;
    }
}

class HomeData {
    List<Banner>? banner;
    List<Good>? goods;
    int? id;
    List<Menu>? menu;
    String? title;
    int? type;

    HomeData({this.banner, this.goods, this.id, this.menu, this.title, this.type});

    factory HomeData.fromJson(Map<String, dynamic> json) {
        return HomeData(
            banner: json.get('banner', convert: (it) { return (it as List).map((i) => Banner.fromJson(i)).toList(); }),
            goods: json.get('goods', convert: (it) { return (it as List).map((i) => Good.fromJson(i)).toList(); }),
            id: json.get('id'),
            menu: json.get('menu', convert: (it) { return (it as List).map((i) => Menu.fromJson(i)).toList(); }),
            title: json.get('title'),
            type: json.get('type'),
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        data['id'] = this.id;
        data['title'] = this.title;
        data['type'] = this.type;
        if (this.banner != null) {
            data['banner'] = this.banner!.map((v) => v.toJson()).toList();
        }
        if (this.goods != null) {
            data['goods'] = this.goods!.map((v) => v.toJson()).toList();
        }
        if (this.menu != null) {
            data['menu'] = this.menu!.map((v) => v.toJson()).toList();
        }
        return data;
    }
}

class Banner {
    int? height;
    String? image;
    String? title;
    int? width;

    Banner({this.height, this.image, this.title, this.width});

    factory Banner.fromJson(Map<String, dynamic> json) {
        return Banner(
            height: json.get('height'),
            image: json.get('image'),
            title: json.get('title'),
            width: json.get('width'),
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        data['height'] = this.height;
        data['image'] = this.image;
        data['title'] = this.title;
        data['width'] = this.width;
        return data;
    }
}

class Good {
    String? des;
    int? id;
    String? image;
    String? name;
    int? price;
    int? stock;

    Good({this.des, this.id, this.image, this.name, this.price, this.stock});

    factory Good.fromJson(Map<String, dynamic> json) {
        return Good(
            des: json.get('des'),
            id: json.get('id'),
            image: json.get('image'),
            name: json.get('name'),
            price: json.get('price'),
            stock: json.get('stock'),
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        data['des'] = this.des;
        data['id'] = this.id;
        data['image'] = this.image;
        data['name'] = this.name;
        data['price'] = this.price;
        data['stock'] = this.stock;
        return data;
    }
}

class Menu {
    int? height;
    String? image;
    String? title;
    int? width;

    Menu({this.height, this.image, this.title, this.width});

    factory Menu.fromJson(Map<String, dynamic> json) {
        return Menu(
            height: json.get('height'),
            image: json.get('image'),
            title: json.get('title'),
            width: json.get('width'),
        );
    }

    Map<String, dynamic> toJson() {
        final Map<String, dynamic> data = new Map<String, dynamic>();
        data['height'] = this.height;
        data['image'] = this.image;
        data['title'] = this.title;
        data['width'] = this.width;
        return data;
    }
}