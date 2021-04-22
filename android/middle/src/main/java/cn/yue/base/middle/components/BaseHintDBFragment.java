package cn.yue.base.middle.components;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewStub;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
public abstract class BaseHintDBFragment<T extends ViewDataBinding> extends BaseHintFragment {

    protected T binding;

    @Override
    protected void stubInflate(ViewStub stub, View inflated) {
        super.stubInflate(stub, inflated);
        binding = DataBindingUtil.bind(inflated);
    }
}