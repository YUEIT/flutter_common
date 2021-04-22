import 'package:flutter/material.dart';
import 'package:flutter_common/common/components/vm/data/page_state.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';
import 'refresh.dart';
import 'scaffold_widget.dart';
import 'vm/page_view_model.dart';
import 'vm/pull_view_model.dart';
import 'vm/simple_pull_view_model.dart';
import 'provider/mode_provider.dart';

typedef BuildPageWidget = Widget Function(
    BuildContext context, dynamic data);

/// 带页面状态和下拉刷新、上拉加载分页功能
class NormalPageWidget extends StatelessWidget {
  NormalPageWidget({
    required this.pageViewModel,
    required this.buildPageWidget,
    this.autoLoad = true,
    this.pullLoad = true,
    this.stateWidget
  });

  final PageViewModel pageViewModel;
  final BuildPageWidget buildPageWidget;
  final bool autoLoad;
  final bool pullLoad;
  final PageStateWidget? stateWidget;

  @override
  Widget build(BuildContext context) {
    if (autoLoad) {
      return NotificationListener<ScrollNotification>(
          onNotification: (notification) {
            pageViewModel.loadMore(notification: notification);
            return false;
          },
          child: _content());
    } else {
      return _content();
    }
  }

  Widget _content() {
    return ModeProvider(
        viewModel: pageViewModel,
        child: LiveStreamBuilder<PageStateEvent>(
            liveData: pageViewModel.pageStateLiveData,
            builder: (context, PageStateEvent pageStateEvent) {
              return ScaffoldContainer(
                currentStateEvent: pageStateEvent,
                reload: () async {
                  pageViewModel.refresh();
                },
                stateWidget: stateWidget,
                content: LiveStreamBuilder<List<dynamic>>(
                    liveData: pageViewModel.dataListLiveData,
                    builder: (context, List<dynamic> dataList) {
                      return RefreshWidget(
                        noMore: pageViewModel.noMore(),
                        onRefresh: () async {
                          pageViewModel.refresh();
                        },
                        onLoad: pullLoad ? () async {
                          pageViewModel.loadMore();
                        } : null,
                        child: buildPageWidget(context, dataList),
                      );
                    }),
              );
            })
    );
  }

}

typedef BuildItemWidget = Widget Function(
    BuildContext context, dynamic data, int position);

typedef BuildPortWidget = Widget Function(
    BuildContext context, int position);

/// 带页面状态和下拉刷新、上拉加载分页功能 封装了ListView
class SimplePageWidget extends StatelessWidget {
  SimplePageWidget({
    required this.pageViewModel,
    required this.buildItemWidget,
    this.buildHeaderWidget,
    this.headerCount = 0,
    this.buildFooterWidget,
    this.footerCount = 0,
    this.autoLoad = true,
    this.pullLoad = true,
    this.stateWidget
  });

  final PageViewModel pageViewModel;
  final BuildItemWidget buildItemWidget;
  final bool autoLoad;
  final bool pullLoad;
  final BuildPortWidget? buildHeaderWidget;
  final int headerCount;
  final BuildPortWidget? buildFooterWidget;
  final int footerCount;
  final PageStateWidget? stateWidget;

  @override
  Widget build(BuildContext context) {
    if (autoLoad) {
      return NotificationListener<ScrollNotification>(
          onNotification: (notification) {
            pageViewModel.loadMore(notification: notification);
            return false;
          },
          child: _content());
    } else {
      return _content();
    }
  }

  Widget _content() {
    return ModeProvider(
      viewModel: pageViewModel,
      child: LiveStreamBuilder<PageStateEvent>(
          liveData: pageViewModel.pageStateLiveData,
          builder: (context, PageStateEvent pageStateEvent) {
            return ScaffoldContainer(
              currentStateEvent: pageStateEvent,
              reload: () async {
                pageViewModel.refresh();
              },
              stateWidget: stateWidget,
              content: LiveStreamBuilder<List<dynamic>>(
                  liveData: pageViewModel.dataListLiveData,
                  builder: (context, List<dynamic> dataList) {
                    int length = _dataLength(dataList.length);
                    return RefreshWidget(
                      noMore: pageViewModel.noMore(),
                      onRefresh: () async {
                        pageViewModel.refresh();
                      },
                      onLoad: pullLoad ? () async {
                        pageViewModel.loadMore();
                      } : null,
                      child: ListView.builder(
                        itemBuilder: (context, position) {
                          if (_hasHeader() && position < _headerCount()) {
                            return buildHeaderWidget!(context, position);
                          } else if (position < dataList.length + _headerCount()) {
                            return buildItemWidget(
                                context, dataList[position - _headerCount()], position - _headerCount());
                          } else if (_hasFooter() && position < length) {
                            return buildFooterWidget!(context, position - _headerCount() - dataList.length);
                          } else {
                            return Container();
                          }
                        },
                        shrinkWrap: false,
                        itemCount: length,
                      ),
                    );
                  }),
            );
          })
    );
  }

  bool _hasHeader() {
    return buildHeaderWidget != null;
  }

  bool _hasFooter() {
    return buildFooterWidget != null;
  }

  int _headerCount() {
    if (_hasHeader()) {
      if (headerCount == 0) {
        return 1;
      } else {
        return headerCount;
      }
    } else {
      return 0;
    }
  }

  int _footerCount() {
    if (_hasFooter()) {
      if (footerCount == 0) {
        return 1;
      } else {
        return footerCount;
      }
    } else {
      return 0;
    }
  }

  int _dataLength(int length) {
    return length + _headerCount() + _footerCount();
  }

}

/// 带页面状态
class NormalPullWidget extends StatelessWidget {
  NormalPullWidget({
    required this.pullViewModel,
    required this.buildWidget,
    this.stateWidget
  });

  final PullViewModel pullViewModel;
  final BuildWidget buildWidget;
  final PageStateWidget? stateWidget;

  @override
  Widget build(BuildContext context) {
    return ModeProvider(
        viewModel: pullViewModel,
        child: LiveStreamBuilder<PageStateEvent>(
            liveData: pullViewModel.pageStateLiveData,
            builder: (context, pageStateEvent) {
              return ScaffoldContainer(
                  currentStateEvent: pageStateEvent,
                  reload: () async {
                    pullViewModel.refresh();
                  },
                  stateWidget: stateWidget,
                  content: buildWidget(context));
            }));
  }
}

typedef BuildWidget = Widget Function(BuildContext context);
typedef BuildPullWidget = Widget Function(BuildContext context, dynamic data);

/// 带页面状态和下拉刷新
class SimplePullWidget extends StatelessWidget {
  SimplePullWidget({
    required this.pullViewModel,
    required this.buildWidget,
    this.stateWidget
  });

  final SimplePullViewModel pullViewModel;
  final BuildPullWidget buildWidget;
  final PageStateWidget? stateWidget;

  @override
  Widget build(BuildContext context) {
    return ModeProvider(
      viewModel: pullViewModel,
      child: LiveStreamBuilder<PageStateEvent>(
          liveData: pullViewModel.pageStateLiveData,
          builder: (context, PageStateEvent pageStateEvent) {
            return ScaffoldContainer(
              currentStateEvent: pageStateEvent,
              reload: () async {
                pullViewModel.refresh();
              },
              stateWidget: stateWidget,
              content: LiveStreamBuilder<dynamic>(
                liveData: pullViewModel.data,
                builder: (context, data) {
                  return RefreshWidget(
                      onRefresh: () async {
                        pullViewModel.refresh();
                      },
                      child: buildWidget(context, data));
                },
              ),
            );
          }),
    );
  }
}
