import 'package:flutter_common/common/components/vm/base_view_model.dart';
import 'package:flutter_common/common/components/vm/live_data.dart';

class OptionViewModel extends BaseViewModel {

  LiveData<bool> iconStatus = LiveData(initialData: false);

  void toggle() {
    iconStatus.postValue(!iconStatus.value!);
  }
}