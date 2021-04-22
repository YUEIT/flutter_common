/// 分页模型

abstract class PageMode<T> {

  /// 服务器未返回情况下，实现体可赋为默认值，但在接口成功后手动计算当前页数
  int? getPageNo();
  int getPageSize();
  List<T> getDataList();
  bool isEmpty();
  bool isEnd();

}