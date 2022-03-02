package cn.yue.base.middle.components.binding;

import android.view.View;
import android.view.ViewStub;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import cn.yue.base.middle.components.BasePullFragment;

/**
 * 介绍: (这里用一句话描述这个类的作用)
 * 作者: zhanghui
 * 邮箱: zhangh@imcoming.cn
 * 时间: 2019/4/18 10:30 AM
 */
public abstract class BasePullBindFragment<T extends ViewDataBinding> extends BasePullFragment {
    protected T binding;

    @Override
    protected void bindLayout(View inflated) {
        super.bindLayout(inflated);
        binding = DataBindingUtil.bind(inflated);
    }
}
