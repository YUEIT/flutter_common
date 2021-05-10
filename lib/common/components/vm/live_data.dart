import 'package:flutter/cupertino.dart';
import 'package:rxdart/rxdart.dart';

class LiveData<T> {

  LiveData({T? initialData}) {
    this._data = initialData;
  }

  final _dataFetcher = BehaviorSubject<T>();
  Stream<T> get dataStream => _dataFetcher.stream;
  T? _data;
  dynamic _pendingData;
  T? get value => _data;

  void postValue(T? value) {
    if (value == null) {
      return;
    }
    if (_pendingData == value) {
      return;
    }
    _pendingData = value;
    var newValue = _pendingData;
    if (!_dataFetcher.isClosed) {
      _data = newValue;
      _dataFetcher.add(newValue);
    }
  }

  void setValue(T? value) {
    if (value == null) {
      return;
    }
    if (!_dataFetcher.isClosed) {
      _data = value;
      _dataFetcher.add(value);
    }
  }

  void observe(void onData(T value)?,
      {Function? onError, void onDone()?, bool? cancelOnError}) {
    _dataFetcher.listen(onData,
        onError: onError, onDone: onDone, cancelOnError: cancelOnError);
  }

  void dispose() {
    _dataFetcher.close();
  }

  void notify() {
    if (_data != null) {
      setValue(_data!);
    }
  }
}

class ListLiveData<T> extends LiveData<List<T>> {

  @override
  postValue(List<T>? value) {
    if (value == null) {
      super.postValue([]);
    } else {
      super.postValue(List.of(value));
    }
  }

  add(T t) {
    var list = this.value;
    if (list == null) {
      list = [];
    }
    list.add(t);
    postValue(list);
  }

  addAll(Iterable<T> iterable) {
    var list = this.value;
    if (list == null) {
      list = [];
    }
    list.addAll(iterable);
    postValue(list);
  }

  remove(int index) {
    var list = this.value;
    if (list == null) {
      list = [];
    }
    var t = list.removeAt(index);
    postValue(list);
    return t;
  }

  clear() {
    var list = this.value;
    if (list == null) {
      list = [];
    }
    list.clear();
    postValue(list);
  }
}

typedef LiveBuilder<T> = Widget Function(BuildContext context, T data);

class LiveStreamBuilder<T> extends StatelessWidget {
  final LiveData<T> liveData;

  LiveStreamBuilder({required this.liveData, required this.builder});

  final LiveBuilder<T> builder;

  @override
  Widget build(BuildContext context) {
    return BaseStreamBuilder(
        initialData: liveData.value,
        stream: liveData.dataStream,
        builder: (context, AsyncSnapshot snapshot) {
          if (snapshot.data == null) {
            return Container();
          }
          return builder(context, snapshot.data as T);
        });
  }
}

class BaseStreamBuilder<T> extends StreamBuilder<T> {

  const BaseStreamBuilder({
    Key? key,
    T? initialData,
    Stream<T>? stream,
    @required builder,
  }) : assert(builder != null),
        super(key: key, initialData: initialData, stream: stream, builder: builder);

  @override
  AsyncSnapshot<T> afterConnected(AsyncSnapshot<T> current) {
    //更新widget，数据不会初始化，而是使用缓存的，这里手动调用
    return super.afterConnected(initial());
  }

  @override
  AsyncSnapshot<T> initial() {
    return super.initial();
  }
}
