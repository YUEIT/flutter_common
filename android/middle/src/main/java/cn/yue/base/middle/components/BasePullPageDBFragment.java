package cn.yue.base.middle.components;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.net.wrapper.BaseListBean;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
public abstract class BasePullPageDBFragment<DB extends ViewDataBinding, S> extends BasePullListFragment<BaseListBean<S>, S> {

    protected DB binding;
    @Override
    protected CommonAdapter<S> getAdapter() {
        CommonAdapter adapter = new CommonAdapter<S>(mActivity, new ArrayList<S>()) {

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
                bindItemData(holder, position, s);
            }
        };
        return adapter;
    }

    @Override
    protected void bindItemData(CommonViewHolder<S> holder, int position, S s) {
        bindItemData(position, s);
    }

    protected abstract void bindItemData(int position, S s);

}
