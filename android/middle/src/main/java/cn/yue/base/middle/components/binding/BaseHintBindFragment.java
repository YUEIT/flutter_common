package cn.yue.base.middle.components.binding;

import android.view.View;
import android.view.ViewStub;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import cn.yue.base.middle.components.BaseHintFragment;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
public abstract class BaseHintBindFragment<T extends ViewDataBinding> extends BaseHintFragment {

    protected T binding;

    @Override
    protected void stubInflate(ViewStub stub, View inflated) {
        super.stubInflate(stub, inflated);
        binding = DataBindingUtil.bind(inflated);
    }
}