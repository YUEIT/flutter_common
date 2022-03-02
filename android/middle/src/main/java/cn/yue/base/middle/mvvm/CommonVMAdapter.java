package cn.yue.base.middle.mvvm;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.mvvm.data.BR;

abstract public class CommonVMAdapter<T> extends CommonAdapter<T> {

    // key为itemType; value为layoutId
    private Map<Integer, Integer> typeToLayoutMap = new HashMap<>();
    // key为data.hashCode; value为ItemViewModel
    private Map<Integer, ItemViewModel> modelList = new LinkedHashMap<>();
    public CommonVMAdapter(Context context) {
        super(context);
    }

    public CommonVMAdapter(Context context, List<T> list) {
        super(context, list);
        modelList.clear();
        addAllModel(list);
    }

    public abstract ItemViewModel initItemViewModel(T t);

    @Override
    public void setList(List<T> list) {
        if (list != null) {
            modelList.clear();
            addAllModel(list);
            super.setList(list);
        }
    }

    @Override
    public void addList(Collection<T> list) {
        if (list != null) {
            addAllModel(list);
            super.addList(list);
        }
    }

    private void addAllModel(Collection<T> list) {
        for (T t: list) {
            addModel(t);
        }
    }

    private void addModel(T t) {
        ItemViewModel itemViewModel = initItemViewModel(t);
        modelList.put(t.hashCode(), itemViewModel);
        typeToLayoutMap.put(itemViewModel.getItemType(), itemViewModel.getLayoutId());
    }

    @Override
    public void addItem(T t) {
        if (t != null) {
            ItemViewModel itemViewModel = initItemViewModel(t);
            modelList.put(t.hashCode(), itemViewModel);
            typeToLayoutMap.put(itemViewModel.getItemType(), itemViewModel.getLayoutId());
            super.addItem(t);
        }
    }

    @Override
    public void clear() {
        modelList.clear();
        typeToLayoutMap.clear();
        super.clear();
    }

    @Override
    public void remove(T t) {
        if (t != null) {
            modelList.remove(t.hashCode());
            super.remove(t);
        }
    }

    @Override
    public void remove(int position) {
        if (getData().size() > position && getData().get(position) != null) {
            modelList.remove(list.get(position).hashCode());
        }
        super.remove(position);
    }

    public int getVariable(){
        return BR.viewModel;
    }

    @Override
    protected int getViewType(int position) {
        if (getData().size() > position && getData().get(position) != null) {
            ItemViewModel itemViewModel = modelList.get(getData().get(position).hashCode());
            if (itemViewModel != null) {
                return itemViewModel.getItemType();
            }
        }
        return super.getViewType(position);
    }

    @Override
    public int getLayoutIdByType(int viewType) {
        Integer layoutId = typeToLayoutMap.get(viewType);
        return layoutId == null? 0 : layoutId;
    }

    @Override
    public void bindData(CommonViewHolder<T> holder, int position, T t) {
        ViewDataBinding binding = DataBindingUtil.bind(holder.itemView);
        if (binding != null) {
            binding.setVariable(getVariable(), modelList.get(t.hashCode()));
            binding.executePendingBindings();
        }
    }
}
