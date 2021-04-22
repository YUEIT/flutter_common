class PullListMode {

  int? id;
  String? name;
  String? phone;
  String? address;
  bool isSelected = false;

  PullListMode({this.id, this.name, this.phone, this.address});

  factory PullListMode.fromJson(Map<String, dynamic> json) {
    return PullListMode(
      id: json['id'],
      name: json['name'],
      phone: json['phone'],
      address: json['address']
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['phone'] = this.phone;
    data['address'] = this.address;
    return data;
  }

  @override
  String toString() {
    return 'TestModel{id: $id, name: $name, phone: $phone, address: $address, isSelected: $isSelected}';
  }

}