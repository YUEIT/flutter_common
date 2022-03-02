package cn.yue.base.common.widget.linear;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class LinearFillingHelper {

    private LinearLayout linearLayout;
    private Adapter adapter;

    public LinearFillingHelper(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        adapter.injectionFillingHelper(this);
        fillLayout();
    }

    private void fillLayout() {
        linearLayout.removeAllViews();
        for (int i = 0; i < adapter.getItemCount(); i++ ) {
            View child = View.inflate(linearLayout.getContext(), adapter.getLayoutId(), null);
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            if (linearLayout.getOrientation() == LinearLayout.HORIZONTAL) {
                width = LinearLayout.LayoutParams.WRAP_CONTENT;
                height = LinearLayout.LayoutParams.MATCH_PARENT;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            if (adapter.shouldExpand()) {
                layoutParams.weight = 1f;
            } else {
                layoutParams.weight = 0f;
            }
            linearLayout.addView(child, layoutParams);
            adapter.bindView(child, i);
        }
    }

    private void notifyLayout() {
        if (linearLayout.getChildCount() != adapter.getItemCount()) {
            fillLayout();
            return;
        }
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            adapter.bindView(child, i);
        }
    }

    public abstract class Adapter {

        boolean shouldExpand() {
            return false;
        }
        abstract int getItemCount();
        abstract int getLayoutId();
        abstract void bindView(View contentView, int position);

        private LinearFillingHelper fillingHelper;

        void injectionFillingHelper(LinearFillingHelper fillingHelper) {
            this.fillingHelper = fillingHelper;
        }

        void notifyDataSetChanged() {
            if (fillingHelper != null) {
                fillingHelper.notifyLayout();
            }
        }
    }

    public abstract class SimpleAdapter<T> extends Adapter {

        private List<T> dataList = new ArrayList<>();

        public SimpleAdapter(List<T> dataList) {
            this.dataList.addAll(dataList);
        }

        public void setData(List<T> dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @Override
        int getItemCount() {
            return dataList.size();
        }

        @Override
        void bindView(View contentView, int position) {
            if (position < dataList.size()) {
                bindView(contentView, position, dataList.get(position));
            }
        }

        abstract void bindView(View contentView, int position, T itemData);
    }
}
