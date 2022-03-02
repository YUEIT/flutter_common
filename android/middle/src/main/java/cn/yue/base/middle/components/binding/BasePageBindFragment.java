package cn.yue.base.middle.components.binding;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BaseListFragment;
import cn.yue.base.middle.net.wrapper.BaseListBean;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
public abstract class BasePageBindFragment<DB extends ViewDataBinding, S> extends BaseListFragment<BaseListBean<S>, S> {

    protected DB binding;
    @Override
    protected CommonAdapter<S> getAdapter() {
        return new CommonAdapter<S>(mActivity, new ArrayList<S>()) {

            @Override
            protected int getViewType(int position) {
                return getItemType(position);
            }

            @Override
            public int getLayoutIdByType(int viewType) {
                return getItemLayoutId(viewType);
            }

            @Override
            public void bindData(CommonViewHolder<S> holder, int position, S s) {
                binding = DataBindingUtil.bind(holder.itemView);
                bindItemData(binding, position, s);
            }
        };
    }

    @Override
    protected void bindItemData(CommonViewHolder<S> holder, int position, S s) {
        throw new IllegalStateException("this function is deprecated");
    }

    protected abstract void bindItemData(DB binding, int position, S s);

}
