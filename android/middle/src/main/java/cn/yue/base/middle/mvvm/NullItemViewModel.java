package cn.yue.base.middle.mvvm;

import androidx.annotation.NonNull;

import cn.yue.base.middle.R;

public class NullItemViewModel extends ItemViewModel{


    public NullItemViewModel(@NonNull BaseViewModel parentViewModel) {
        super(parentViewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_space_binding;
    }
}
