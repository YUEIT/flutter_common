/// Description : 
/// Created by yue on 4/21/21
enum PageState {
  loading,
  normal,
  noData,
  error,
  noNet,
  unKnown
}

class PageStateEvent {
  final PageState state;
  String? showInfo;
  PageStateEvent({required this.state, this.showInfo});

  factory PageStateEvent.loading() {
    return PageStateEvent(state: PageState.loading);
  }

  factory PageStateEvent.normal() {
    return PageStateEvent(state: PageState.normal);
  }

  factory PageStateEvent.noData() {
    return PageStateEvent(state: PageState.noData);
  }

  factory PageStateEvent.error() {
    return PageStateEvent(state: PageState.error);
  }

  factory PageStateEvent.noNet() {
    return PageStateEvent(state: PageState.noNet);
  }

}

