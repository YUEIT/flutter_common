package cn.yue.base.common.binding.recycler;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ViewAdapter {

    @BindingAdapter(value = {"adapter"})
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"layoutManager", "linearLayoutManager", "gridLayoutManager",
            "staggeredGridLayoutManager", "spanCount"}, requireAll = false)
    public static void setLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                          String linearLayoutManager, String gridLayoutManager, String staggeredGridLayoutManager, Integer spanCount) {
        RecyclerView.LayoutManager mLayoutManager = null;
        if (layoutManager != null) {
            mLayoutManager = layoutManager;
        }
        if (linearLayoutManager != null) {
            if (("horizontal").contains(linearLayoutManager)) {
                mLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            } else if ("vertical".contains(linearLayoutManager)) {
                mLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            }
        }
        if (gridLayoutManager != null && spanCount != null) {
            if ("horizontal".contains(gridLayoutManager)) {
                mLayoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount, GridLayoutManager.HORIZONTAL, false);
            } else if ("vertical".contains(gridLayoutManager)) {
                mLayoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount, GridLayoutManager.VERTICAL, false);
            }
        }
        if (staggeredGridLayoutManager != null && spanCount != null) {
            if (("horizontal").contains(staggeredGridLayoutManager)) {
                mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL);
            } else if ("vertical".contains(staggeredGridLayoutManager)) {
                mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
            }
        }
        if (mLayoutManager != null) {
            recyclerView.setLayoutManager(mLayoutManager);
        }
    }

    @BindingAdapter(value = {"nestedScrollingEnabled"})
    public static void setAdapter(RecyclerView recyclerView, boolean nestedScrollingEnabled) {
        recyclerView.setNestedScrollingEnabled(nestedScrollingEnabled);
    }

}
