package cn.yue.base.middle.mvvm.components.binding;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import cn.yue.base.middle.mvvm.PullViewModel;
import cn.yue.base.middle.mvvm.components.BasePullVMFragment;
import cn.yue.base.middle.mvvm.data.BR;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullVMBindFragment<VM extends PullViewModel, T extends ViewDataBinding> extends BasePullVMFragment<VM> {

    protected T binding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        binding.setVariable(getVariableId(), viewModel);
    }

    public int getVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void bindLayout(View inflated) {
        binding = DataBindingUtil.bind(inflated);
    }
}
