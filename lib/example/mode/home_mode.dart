class HomeMode {
    List<HomeData>? homeData;

    HomeMode({this.homeData});

    factory HomeMode.fromJson(Map<String, dynamic> json) {
        return HomeMode(
            homeData: json['homeData'] != null ? (json['homeData'] as List).map((i) => HomeData.fromJson(i)).toList() : null,
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
            banner: json['banner'] != null ? (json['banner'] as List).map((i) => Banner.fromJson(i)).toList() : null,
            goods: json['goods'] != null ? (json['goods'] as List).map((i) => Good.fromJson(i)).toList() : null,
            id: json['id'],
            menu: json['menu'] != null ? (json['menu'] as List).map((i) => Menu.fromJson(i)).toList() : null,
            title: json['title'],
            type: json['type'],
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
            height: json['height'],
            image: json['image'],
            title: json['title'],
            width: json['width'],
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
            des: json['des'],
            id: json['id'],
            image: json['image'],
            name: json['name'],
            price: json['price'],
            stock: json['stock'],
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
            height: json['height'],
            image: json['image'],
            title: json['title'],
            width: json['width'],
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