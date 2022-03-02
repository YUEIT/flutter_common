package cn.yue.base.common.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import cn.yue.base.common.R;

public class SampleTabStrip2 extends RecyclerView {

    public interface LayoutTabProvider {

        int getItemCount();
        /**
         * 创建一个 tab item
         * @return
         */
        View createTabView();

        void bindTabView(View view, int position, boolean isSelected);

    }

    private LayoutTabProvider layoutTabProvider;
    private final PageListener pageListener = new PageListener();
    private ViewPager2 pager;
    private RecyclerView.Adapter<?> tabAdapter;
    private int currentPosition = 0;
    private boolean shouldExpand = false;


    public SampleTabStrip2(Context context) {
        this(context, null);
    }

    public SampleTabStrip2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleTabStrip2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleTabStrip2);
        shouldExpand = a.getBoolean(R.styleable.SampleTabStrip2_sts2ShouldExpand, shouldExpand);
        a.recycle();
        setLayoutManager(new LinearLayoutManager(context, HORIZONTAL, false));
    }

    public void setViewPager(ViewPager2 viewPager) {
        if (viewPager != null) {
            pager = viewPager;
            if (pageListener != null) {
                pager.unregisterOnPageChangeCallback(pageListener);
                pager.registerOnPageChangeCallback(pageListener);
            }
            final RecyclerView.Adapter<?> adapter = viewPager.getAdapter();
            if (adapter != null) {
                layoutTabProvider = (LayoutTabProvider) adapter;
                tabAdapter = new TabAdapter();
                setAdapter(tabAdapter);
            }
        }
    }

    class TabAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(layoutTabProvider.createTabView());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (shouldExpand) {
                int width = getMeasuredWidth() / layoutTabProvider.getItemCount();
                LayoutParams layoutParams = new RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.itemView.setLayoutParams(layoutParams);
            } else {
                LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.itemView.setLayoutParams(layoutParams);
            }
            layoutTabProvider.bindTabView(holder.itemView, position, currentPosition == position);
            holder.itemView.setOnClickListener(view -> {
                pager.setCurrentItem(position);
            });
        }

        @Override
        public int getItemCount() {
            return layoutTabProvider.getItemCount();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void scrollToChild(int position) {
        if (position >= getChildCount()) {
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (position > linearLayoutManager.findLastCompletelyVisibleItemPosition()) {
            smoothScrollToPosition(position);
        }
        tabAdapter.notifyDataSetChanged();
    }

    private class PageListener extends ViewPager2.OnPageChangeCallback {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem());
            }
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            if (position < getChildCount()) {
                scrollToChild(position);
            }
        }

    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

