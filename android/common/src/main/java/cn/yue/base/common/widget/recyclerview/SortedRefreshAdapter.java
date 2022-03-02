package cn.yue.base.common.widget.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.SortedList;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Description :
 * Created by yue on 2022/1/26
 */

abstract class SortedRefreshAdapter<T> extends CommonAdapter<T> {

    public SortedRefreshAdapter(Context context) {
        super(context);
    }

    public SortedRefreshAdapter(Context context, List<T> list) {
        super(context, list);
    }

    private SortedList<T> list = new SortedList<>(getType(), new AutoSortedListCallback());

    private Class<T> getType() {
        Class<T> clazz;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("泛型获取错误");
        }
        return clazz;
    }

    class AutoSortedListCallback extends SortedList.Callback<T> {

        @Override
        public int compare(T o1, T o2) {
            return SortedRefreshAdapter.this.compare(o1, o2);
        }

        @Override
        public boolean areContentsTheSame(T oldItem, T newItem) {
            return SortedRefreshAdapter.this.areContentsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areItemsTheSame(T item1, T item2) {
            return SortedRefreshAdapter.this.areItemsTheSame(item1, item2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemChangedReally(position);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemInsertedReally(position);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRemovedReally(position);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMovedReally(fromPosition, toPosition);
        }
    }

    abstract int compare(T item1, T item2);

    abstract boolean areItemsTheSame(T item1, T item2);

    abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Override
    public void clear() {
        if (list.size() > 0) {
            list.clear();
        }
    }

    @Override
    public void addList(Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.list.addAll(list);
    }

    @Override
    public void addItem(T t) {
        if (null != t) {
            list.add(t);
        }
    }

    @Override
    public void remove(T t) {
        if (null != t) {
            list.remove(t);
        }
    }

    @Override
    public void remove(int position) {
        if (position > -1 && list.size() > position) {
            list.removeItemAt(position);
        }
    }

    public void updateItemAt(int index, T t) {
        if (null != t && index > 0) {
            list.updateItemAt(index, t);
        }
    }

    public SortedList<T> getSortedList() {
        return list;
    }

    @Override
    public T getItem(int position) {
        if (position < list.size() && position >= 0) {
            return list.get(position);
        }
        return null;
    }

    public int indexOf(T t) {
        return list.indexOf(t);
    }

    @Override
    public int getListSize() {
        return list.size();
    }
}
