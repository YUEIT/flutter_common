package cn.yue.base.common.widget.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Locale;

import cn.yue.base.common.R;

public class SampleTabStrip extends HorizontalScrollView {

    public interface LayoutTabProvider {

        /**
         * 创建一个 tab item
         * @param position
         * @return
         */
        View createTabView(int position);

        /**
         * viewPager切换时，回调所有的item
         * @param v
         * @param isSelect
         */
        void changeTabStyle(View v, boolean isSelect);

    }

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;
    private AdapterChangeListener mAdapterChangeListener;
    private LinearLayout tabsContainer;
    private ViewPager pager;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private int tabCount;
    private int currentPosition = 0;
    private int movePosition = 0;
    private float movePositionOffset = 0f;
    private Paint indicatorPaint;
    private int indicatorWidth = 0;
    private int indicatorHeight = 0;
    private boolean shouldExpand = false;
    private boolean isTextAllCaps = true;
    private int scrollOffset = 52;
    private int tabPadding = 10;
    private int lastScrollX = 0;
    private int tabBackground = 0;

    private Locale locale;

    public SampleTabStrip(Context context) {
        this(context, null);
    }

    public SampleTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleTabStrip);

        tabPadding = a.getDimensionPixelSize(R.styleable.SampleTabStrip_stsTabPaddingLeftRight, tabPadding);
        tabBackground= a.getResourceId(R.styleable.SampleTabStrip_stsTabBackground, tabBackground);
        shouldExpand = a.getBoolean(R.styleable.SampleTabStrip_stsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.SampleTabStrip_stsScrollOffset, scrollOffset);
        isTextAllCaps = a.getBoolean(R.styleable.SampleTabStrip_stsTextAllCaps, isTextAllCaps);
        indicatorWidth = a.getDimensionPixelSize(R.styleable.SampleTabStrip_stsIndicatorWidth, indicatorWidth);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.SampleTabStrip_stsIndicatorHeight, indicatorHeight);
        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(a.getColor(R.styleable.SampleTabStrip_stsIndicatorColor, Color.TRANSPARENT));
        a.recycle();
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void setViewPagerAutoRefresh(ViewPager viewPager) {
        if (pager != null) {
            if (pageListener != null) {
                pager.removeOnPageChangeListener(pageListener);
            }
            if (mAdapterChangeListener != null) {
                pager.removeOnAdapterChangeListener(mAdapterChangeListener);
            }
        }
        if (viewPager != null) {
            pager = viewPager;
            pager.addOnPageChangeListener(pageListener);
            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                // Now we'll populate ourselves from the pager adapter, adding an observer if
                // autoRefresh is enabled
                setPagerAdapter(adapter, true);
            }

            // Add a listener so that we're notified of any adapter changes
            if (mAdapterChangeListener == null) {
                mAdapterChangeListener = new AdapterChangeListener();
            }
            mAdapterChangeListener.setAutoRefresh(true);
            viewPager.addOnAdapterChangeListener(mAdapterChangeListener);
        }
    }

    void setPagerAdapter(@Nullable final PagerAdapter adapter, final boolean addObserver) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            // If we already have a PagerAdapter, unregister our observer
            mPagerAdapter.unregisterDataSetObserver(mPagerAdapterObserver);
        }

        mPagerAdapter = adapter;

        if (addObserver && adapter != null) {
            // Register our observer on the new adapter
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver);
        }

        // Finally make sure we reflect the new adapter
        populateFromPagerAdapter();
    }

    void populateFromPagerAdapter() {
        tabsContainer.removeAllViews();

        if (mPagerAdapter != null) {
            final int adapterCount = mPagerAdapter.getCount();
            tabCount = adapterCount;
            for (int i = 0; i < adapterCount; i++) {
                if (pager.getAdapter() instanceof LayoutTabProvider) {
                    addTab(i, ((LayoutTabProvider) pager.getAdapter()).createTabView(i));
                }

            }
            updateTabStyles();

            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    currentPosition = pager.getCurrentItem();
                    scrollToChild(currentPosition, 0);
                }
            });

        }
    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            if (pager.getAdapter() instanceof LayoutTabProvider) {
                addTab(i, ((LayoutTabProvider) pager.getAdapter()).createTabView(i));
            }
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackground);
            if (pager.getAdapter() instanceof LayoutTabProvider) {
                ((LayoutTabProvider) pager.getAdapter()).changeTabStyle(v, i == currentPosition);
            }
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0 && position >= tabsContainer.getChildCount()) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0 || indicatorHeight == 0 || indicatorWidth == 0) {
            return;
        }
        final int height = getHeight();

        // default: line below current tab
        View moveTab = tabsContainer.getChildAt(movePosition);
        float moveLeft = moveTab.getLeft();
        float moveRight = moveTab.getRight();
        float moveCenter = (moveLeft + moveRight) / 2;
        int nextPosition = movePosition + 1;
        int defaultMarginBottom = 5;
        if (nextPosition < tabCount) {
            View nextTab = tabsContainer.getChildAt(nextPosition);
            float nextLeft = nextTab.getLeft();
            float nextRight = nextTab.getRight();
            float nextCenter = (nextLeft + nextRight) / 2;
            float currentCenter = moveCenter + (nextCenter - moveCenter) * movePositionOffset;
            canvas.drawRect(currentCenter - indicatorWidth / 2,
                    getMeasuredHeight() - indicatorHeight - defaultMarginBottom,
                    currentCenter + indicatorWidth / 2,
                    getMeasuredHeight() - defaultMarginBottom,
                    indicatorPaint);
        } else {
            canvas.drawRect(moveCenter - indicatorWidth / 2,
                    getMeasuredHeight() - indicatorHeight - defaultMarginBottom,
                    moveCenter + indicatorWidth / 2,
                    getMeasuredHeight() - defaultMarginBottom,
                    indicatorPaint);
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        @Override
        public void onChanged() {
            populateFromPagerAdapter();
        }

        @Override
        public void onInvalidated() {
            populateFromPagerAdapter();
        }
    }

    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean mAutoRefresh;

        AdapterChangeListener() {
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager,
                                     @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if (pager == viewPager) {
                setPagerAdapter(newAdapter, mAutoRefresh);
            }
        }

        void setAutoRefresh(boolean autoRefresh) {
            mAutoRefresh = autoRefresh;
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            movePosition = position;
            movePositionOffset = positionOffset;
            if (position < tabsContainer.getChildCount()) {
                scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
                updateTabStyles();
                invalidate();
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            if (position < tabsContainer.getChildCount()) {
                updateTabStyles();
                invalidate();
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return isTextAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.isTextAllCaps = textAllCaps;
    }

    public void setTabBackground(int resId) {
        this.tabBackground = resId;
    }

    public int getTabBackground() {
        return tabBackground;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
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

