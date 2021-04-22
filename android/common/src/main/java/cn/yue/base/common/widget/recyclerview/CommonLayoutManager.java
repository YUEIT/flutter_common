package cn.yue.base.common.widget.recyclerview;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description : 通用LayoutManager 即简化编写流程
 * Created by yue on 2016/12/16
 */

public class CommonLayoutManager extends  RecyclerView.LayoutManager {

    private String TAG = "CommonLayoutManager";
    private static boolean DEBUG = true;
    public int mOrientation;
    public OrientationHelper mOrientationHelper;
    private LayoutState mLayoutState;
    private SavedState savedState = null;
    public final LayoutChunkResult mLayoutChunkResult = new LayoutChunkResult();

    public CommonLayoutManager(){
        setAutoMeasureEnabled(true); //设置该值可以兼容viewHolder设置为wrap_content
        mOrientation = OrientationHelper.VERTICAL;
        ensureLayoutState();
    }

    public CommonLayoutManager(int mOrientation){
        setAutoMeasureEnabled(true); //设置该值可以兼容viewHolder设置为wrap_content
        if(mOrientation != OrientationHelper.HORIZONTAL && mOrientation != OrientationHelper.VERTICAL){
            throw new IllegalArgumentException("invalid orientation:" + mOrientation);
        }
        this.mOrientation = mOrientation;
        ensureLayoutState();
    }

    /**
     * 初始化LayoutState和OrientationHelper
     */
    private void ensureLayoutState() {
        if (mLayoutState == null) {
            mLayoutState = createLayoutState();
        }
        if (mOrientationHelper == null) {
            mOrientationHelper = OrientationHelper.createOrientationHelper(this, mOrientation);
        }
    }

    private LayoutState createLayoutState() {
        return new LayoutState();
    }

    boolean resolveIsInfinite() {
        return mOrientationHelper.getMode() == View.MeasureSpec.UNSPECIFIED
                && mOrientationHelper.getEnd() == 0;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        if (savedState != null) {
            return new SavedState(savedState);
        }
        SavedState state = new SavedState();
        if (getChildCount() > 0) {
            ensureLayoutState();
            final View refChild = getChildClosestToStart();
            state.position = getPosition(refChild);
            state.offset = mOrientationHelper.getDecoratedStart(refChild) -
                    mOrientationHelper.getStartAfterPadding();
        }
        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            savedState = (SavedState) state;
            requestLayout();
            if (DEBUG) {
                Log.d(TAG, "loaded saved state");
            }
        } else if (DEBUG) {
            Log.d(TAG, "invalid saved state class");
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        /*  按照LinearLayoutManager源码应该是以下步骤：（这里省略了第4项）
            1、检查children和其他变量，获取到anchor的坐标和position，这里代码上主要是从onRestoreInstanceState中获取SavedState对象
               该对象保存了position、offset、LayoutFromEnd(布局方向)
            2、由当前position开始，position++ 布局
            3、由当前position结束，position-- 布局
            4、滚动以满足堆栈从底部的要求？（翻译的，看不懂），可以理解为滚动布局以调整，避免出现一些缝隙
         */
        if (DEBUG) {
            Log.d(TAG, "is pre layout:" + state.isPreLayout());
        }
        int position = 0; //这里指屏幕第一项
        int offset = 0;
        if (savedState != null) {
            if (state.getItemCount() == 0) {
                removeAndRecycleAllViews(recycler);
                return;
            }
            position = savedState.position;
            offset = savedState.offset;
        }
        ensureLayoutState();
        mLayoutState.mRecycle = false;

        detachAndScrapAttachedViews(recycler);
        mLayoutState.mInfinite = resolveIsInfinite();

        updateLayoutStateToFillEnd(position, offset);
        fill(recycler, mLayoutState, state);

        int endOffset = mLayoutState.mOffsetFirst;
        final int lastElement = mLayoutState.mCurrentPosition;
        updateLayoutStateToFillStart(position, offset);
        mLayoutState.mCurrentPosition += mLayoutState.mItemDirection;
        fill(recycler, mLayoutState, state);
        if (mLayoutState.mAvailable > 0) {
            // start could not consume all it should. add more items towards end
            updateLayoutStateToFillEnd(lastElement, endOffset);
            fill(recycler, mLayoutState, state);
        }
        if (!state.isPreLayout()) {
            mOrientationHelper.onLayoutComplete();
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 更新布局变量，方向为从当前项往前（即position--）
     * @param itemPosition
     * @param offset
     */
    private void updateLayoutStateToFillStart(int itemPosition, int offset) {
        mLayoutState.mAvailable = offset - mOrientationHelper.getStartAfterPadding();
        mLayoutState.mCurrentPosition = itemPosition;
        mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_HEAD;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_START;
        mLayoutState.mOffsetFirst = offset;
        mLayoutState.mOffsetSecond = getOffsetSecond();
        mLayoutState.mScrollingOffset = LayoutState.SCROLLING_OFFSET_NaN;
    }

    /**
     * 更新布局变量，方向为从当前项往后（即position++）
     * @param itemPosition
     * @param offset
     */
    private void updateLayoutStateToFillEnd(int itemPosition, int offset) {
        mLayoutState.mAvailable = mOrientationHelper.getEndAfterPadding() - offset;
        mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_TAIL;
        mLayoutState.mCurrentPosition = itemPosition;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_END;
        mLayoutState.mOffsetFirst = offset;
        mLayoutState.mOffsetSecond = getOffsetSecond();
        mLayoutState.mScrollingOffset = LayoutState.SCROLLING_OFFSET_NaN;
    }

    public int getOffsetSecond(){
        return 0;
    }


    /**
     * 水平滑动
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    /**
     * 垂直滑动
     * @return
     */
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mOrientation == OrientationHelper.VERTICAL) {
            return 0;
        }
        return scrollBy(dx, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mOrientation == OrientationHelper.HORIZONTAL) {
            return 0;
        }
        return scrollBy(dy, recycler, state);
    }

    /**
     * 实际滑动操作
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    private int scrollBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state){
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }
        mLayoutState.mRecycle = true;
        ensureLayoutState();
        final int layoutDirection = dy > 0 ? LayoutState.LAYOUT_END : LayoutState.LAYOUT_START;
        final int absDy = Math.abs(dy);
        updateLayoutState(layoutDirection, absDy, true, state);
        final int consumed = mLayoutState.mScrollingOffset
                + fill(recycler, mLayoutState, state);

        if (consumed < 0) {
            if (DEBUG) {
                Log.d(TAG, "Don't have any more elements to scroll");
            }
            return 0;
        }
        final int scrolled = absDy > consumed ? layoutDirection * consumed : dy;
        mOrientationHelper.offsetChildren(-scrolled);
        if (DEBUG) {
            Log.d(TAG, "scroll dy: " + dy + " scrolled: " + scrolled);
        }
        mLayoutState.mLastScrollDelta = scrolled;
        return scrolled;
    }

    /**
     * 滑动时，更新布局变量
     * @param layoutDirection
     * @param requiredSpace
     * @param canUseExistingSpace
     * @param state
     */
    public void updateLayoutState(int layoutDirection, int requiredSpace,
                                   boolean canUseExistingSpace, RecyclerView.State state) {
        // If parent provides a hint, don't measure unlimited.
        mLayoutState.mInfinite = false;
        mLayoutState.mLayoutDirection = layoutDirection;
        int scrollingOffset;
        if (layoutDirection == LayoutState.LAYOUT_END) { //列表从下往上移动
            // get the first child in the direction we are going
            final View child = getChildClosestToEnd();
            // the direction in which we are traversing children
            mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_TAIL; //布局在尾部
            mLayoutState.mCurrentPosition = getPosition(child) + mLayoutState.mItemDirection;
            mLayoutState.mOffsetFirst = mOrientationHelper.getDecoratedEnd(child);
            mLayoutState.mOffsetSecond = getDecoratedOtherEnd(child);
            // calculate how much we can scroll without adding new children (independent of layout)
            scrollingOffset = mOrientationHelper.getDecoratedEnd(child)
                    - mOrientationHelper.getEndAfterPadding();
        } else {
            final View child = getChildClosestToStart();
            mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_HEAD;
            mLayoutState.mCurrentPosition = getPosition(child) + mLayoutState.mItemDirection;
            mLayoutState.mOffsetFirst = mOrientationHelper.getDecoratedStart(child);
            mLayoutState.mOffsetSecond = getDecoratedOtherEnd(child);
            scrollingOffset = -mOrientationHelper.getDecoratedStart(child)
                    + mOrientationHelper.getStartAfterPadding();
        }
        mLayoutState.mAvailable = requiredSpace;
        if (canUseExistingSpace) {
            mLayoutState.mAvailable -= scrollingOffset;
        }
        mLayoutState.mScrollingOffset = scrollingOffset;
    }

    private int getDecoratedOtherEnd(View child){
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        if(mOrientation == OrientationHelper.HORIZONTAL){
            return getDecoratedRight(child) + params.rightMargin;
        }else{
            return getDecoratedBottom(child) + params.bottomMargin;
        }
    }

    /**
     * 这里获取的是相对于屏幕而言，即获取屏幕上的最后一项
     * @return
     */
    private View getChildClosestToEnd() {
        return getChildAt(getChildCount() - 1);
    }

    /**
     * 这里获取的是相对于屏幕而言，即获取屏幕上的第一项
     * @return
     */
    private View getChildClosestToStart() {
        return getChildAt(0);
    }

    /**
     * 回收child，添加新的child
     * @param recycler
     * @param layoutState
     * @param state
     * @return
     */
    private int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state){
        final int start = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != LayoutState.SCROLLING_OFFSET_NaN) {
            // TODO ugly bug fix. should not happen
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            recycleByLayoutState(recycler, layoutState);
        }
        fillLayout(recycler, layoutState, state);
        return start - layoutState.mAvailable;
    }

    public void fillLayout(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state){
        int remainingSpace = layoutState.mAvailable;
        LayoutChunkResult layoutChunkResult = mLayoutChunkResult;
        while ((layoutState.mInfinite || remainingSpace > 0) && layoutState.hasMore(state)) {
            layoutChunkResult.resetInternal();
            View view = layoutState.next(recycler);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            measureChildWithMargins(view, 0, 0);
            // Consume the available space if the view is not removed OR changed
            if (params.isItemRemoved() || params.isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            addView(view, layoutState, layoutChunkResult);
            onLayout(view, state, layoutState, layoutChunkResult);
            if (layoutChunkResult.mFinished) {
                break;
            }
            layoutState.mOffsetFirst += layoutChunkResult.mConsumed * layoutState.mLayoutDirection;
            /**
             * Consume the available space if:
             * * layoutChunk did not request to be ignored
             * * OR we are laying out scrap children
             * * OR we are not doing pre-layout
             */
            if (!layoutChunkResult.mIgnoreConsumed || !state.isPreLayout()) {
                layoutState.mAvailable -= layoutChunkResult.mConsumed;
                // we keep a separate remaining space because mAvailable is important for recycling
                remainingSpace -= layoutChunkResult.mConsumed;
            }
            //初始布局时mScrollingOffset时为最小值
            if (layoutState.mScrollingOffset != LayoutState.SCROLLING_OFFSET_NaN) {
                layoutState.mScrollingOffset += layoutChunkResult.mConsumed;
                if (layoutState.mAvailable < 0) {
                    layoutState.mScrollingOffset += layoutState.mAvailable;
                }
                recycleByLayoutState(recycler, layoutState);
            }
        }
    }
    /**
     * 布局填充
     * @param view
     * @param layoutState
     * @param result
     */
    void addView(View view, LayoutState layoutState, LayoutChunkResult result) {
        if (view == null) {
            // if we are laying out views in scrap, this may return null which means there is
            // no more items to layout.
            result.mFinished = true;
            return;
        }
        if (layoutState.mLayoutDirection != LayoutState.LAYOUT_START) {
            addView(view);
        } else {
            addView(view, 0);
        }
        result.mFocusable = view.isFocusable();
    }

    /**
     * 具体布局填充，自定义时重写
     * @param view
     * @param state
     * @param layoutState
     * @param result
     */
    public void onLayout(View view,  RecyclerView.State state, LayoutState layoutState, LayoutChunkResult result){
        result.mConsumed = mOrientationHelper.getDecoratedMeasurement(view); //源码为实际高度+marginTop+marginBottom，即item的高度
        int left, top, right, bottom;
        if (mOrientation == OrientationHelper.VERTICAL) {
            left = getPaddingLeft();
            right = left + mOrientationHelper.getDecoratedMeasurementInOther(view);
            if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
                bottom = layoutState.mOffsetFirst;
                top = layoutState.mOffsetFirst - result.mConsumed;
            } else {
                top = layoutState.mOffsetFirst;
                bottom = layoutState.mOffsetFirst + result.mConsumed;
            }
        } else {
            top = getPaddingTop();
            bottom = top + mOrientationHelper.getDecoratedMeasurementInOther(view);

            if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
                right = layoutState.mOffsetFirst;
                left = layoutState.mOffsetFirst - result.mConsumed;
            } else {
                left = layoutState.mOffsetFirst;
                right = layoutState.mOffsetFirst + result.mConsumed;
            }
        }
        // We calculate everything with View's bounding box (which includes decor and margins)
        // To calculate correct layout position, we subtract margins.
        layoutDecoratedWithMargins(view, left, top, right, bottom);
        if (DEBUG) {
            Log.d(TAG, "laid out child at position " + getPosition(view) + ", l:" + left +
                    ", t:" + top + ", r:" + right + ", b:" + bottom );
        }
    }

    /**
     * 回收child
     * @param recycler
     * @param layoutState
     */
    public void recycleByLayoutState(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (!layoutState.mRecycle || layoutState.mInfinite) {
            return;
        }
        if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
            recycleViewsFromEnd(recycler, layoutState.mScrollingOffset);
        } else {
            recycleViewsFromStart(recycler, layoutState.mScrollingOffset);
        }
    }

    /**
     * 列表自上而下，回收最下面的child，规则是从屏幕内消失
     * @param recycler
     * @param dt
     */
    private void recycleViewsFromEnd(RecyclerView.Recycler recycler, int dt) {
        final int childCount = getChildCount();
        if (dt < 0) {
            if (DEBUG) {
                Log.d(TAG, "Called recycle from end with a negative value. This might happen"
                        + " during layout changes but may be sign of a bug");
            }
            return;
        }
        final int limit = mOrientationHelper.getEnd() - dt;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (mOrientationHelper.getDecoratedStart(child) < limit
                    || mOrientationHelper.getTransformedStartWithDecoration(child) < limit) {
                // stop here
                recycleChildren(recycler, childCount - 1, i);
                return;
            }
        }
    }

    /**
     * 列表自下而上移动，回收最上面的child
     * @param recycler
     * @param dt
     */
    private void recycleViewsFromStart(RecyclerView.Recycler recycler, int dt) {
        if (dt < 0) {
            if (DEBUG) {
                Log.d(TAG, "Called recycle from start with a negative value. This might happen"
                        + " during layout changes but may be sign of a bug");
            }
            return;
        }
        // ignore padding, ViewGroup may not clip children.
        final int limit = dt;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (mOrientationHelper.getDecoratedEnd(child) > limit
                    || mOrientationHelper.getTransformedEndWithDecoration(child) > limit) {
                // stop here
                recycleChildren(recycler, 0, i);
                return;
            }
        }
    }

    /**
     * 回收child
     * @param recycler
     * @param startIndex
     * @param endIndex
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "Recycling " + Math.abs(startIndex - endIndex) + " items");
        }
        if (endIndex > startIndex) {
            for (int i = endIndex - 1; i >= startIndex; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
        } else {
            for (int i = startIndex; i > endIndex; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
        }
    }

    /**
     * 填充布局结果反馈
     */
    protected static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mIgnoreConsumed;
        public boolean mFocusable;

        void resetInternal() {
            mConsumed = 0;
            mFinished = false;
            mIgnoreConsumed = false;
            mFocusable = false;
        }
    }

    /**
     * 保存状态
     */
    public static class SavedState implements Parcelable {

        int position;

        int offset;

        public SavedState() {

        }

        SavedState(Parcel in) {
            position = in.readInt();
            offset = in.readInt();
        }

        public SavedState(SavedState other) {
            position = other.position;
            offset = other.offset;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(position);
            dest.writeInt(offset);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[0];
            }

        };
    }

    /**
     * 填充布局时的临时状态的帮助类
     */
    static class LayoutState {

        final static String TAG = "LayoutState";

        final static int LAYOUT_START = -1;

        final static int LAYOUT_END = 1;

        final static int INVALID_LAYOUT = Integer.MIN_VALUE;

        final static int ITEM_DIRECTION_HEAD = -1;

        final static int ITEM_DIRECTION_TAIL = 1;

        final static int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;

        /**
         * We may not want to recycle children in some cases (e.g. layout)
         */
        boolean mRecycle = true;

        /**
         * 布局时，用于确定child的位置（同mOrientation设置的方向上）
         */
        int mOffsetFirst;

        /**
         * 布局时，用于确定child的位置（二级）
         */
        int mOffsetSecond;

        /**
         * 布局可用距离，为布局前等于整个recyclerView的高度，每添加一个item，可用距离减去item的高度；
         * 滑动处理是可以理解为“滑动后”距离完全可见还需多少距离（该值是负值），即mAvailable = |dy| - mScrollingOffset
         */
        int mAvailable;

        /**
         * 布局时，当前位置
         */
        int mCurrentPosition;

        /**
         * 填充布局时的方向，ITEM_DIRECTION_HEAD（头部），ITEM_DIRECTION_TAIL（尾部）
         */
        int mItemDirection;

        /**
         * 列表移动方向 LAYOUT_START or LAYOUT_END
         */
        int mLayoutDirection;

        /**
         * 最后一个可见View “滑动前”距离完全可见还需多少距离（该值为正值）
         */
        int mScrollingOffset;

        /**
         * The most recent {@link #scrollBy(int, RecyclerView.Recycler, RecyclerView.State)}
         * amount.
         */
        int mLastScrollDelta;

        /**
         * 不限制填充child的数量，慎用！
         */
        boolean mInfinite;

        boolean hasMore(RecyclerView.State state) {
            return mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount();
        }

        /**
         * 从各级缓存中获取当前child，并将当前position在当前方向后移
         * （这里有个坑，这项操作除了会获取当前child，还会做一些处理，调用该方法获取了position的数据，
         * 这时如果获取的数据是来自于Scrap中的，那么下次如果继续调用该方法获取的数据却是来自recycledViewPool中的，
         * 此时页面会有闪屏的现象）
         */
        View next(RecyclerView.Recycler recycler) {
            try {
                final View view = recycler.getViewForPosition(mCurrentPosition);
                mCurrentPosition += mItemDirection;
                return view;
            }catch (IndexOutOfBoundsException e){
                return null;
            }
        }
    }
}