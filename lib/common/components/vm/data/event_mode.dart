class FinishEvent {
  final Map<String, String>? result;
  FinishEvent({this.result});
}

class WaitEvent {
  final bool isShow;
  final String message;
  WaitEvent({this.isShow = true, this.message = "loading..."});
}