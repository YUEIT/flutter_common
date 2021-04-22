package cn.yue.base.common.widget.recyclerview;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Description : 流式布局
 * Created by yue on 2016/12/16
 */

public class SwipeLayoutManager extends CommonLayoutManager{

    public SwipeLayoutManager(){
        super();
    }

    @Override
    public int getOffsetSecond() {
        if(mOrientation == OrientationHelper.HORIZONTAL){
            return 0;
        }
        return 0;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public int getWidthMode() {
        //默认情况下如果设置setAutoMeasureEnabled(true)，测量模式为EXACTLY
        return View.MeasureSpec.AT_MOST;
    }

    @Override
    public void fillLayout(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state) {
        int remainingSpace = layoutState.mAvailable;
        LayoutChunkResult layoutChunkResult = mLayoutChunkResult;
        layoutState.mOffsetSecond = getOffsetSecond();
        while ((layoutState.mInfinite || remainingSpace > 0) && layoutState.hasMore(state)) {
            layoutChunkResult.resetInternal();
            fillScrollLayout(recycler, layoutState, state, layoutChunkResult); //填充一行
            if (layoutChunkResult.mFinished) {
                break;
            }
            layoutState.mOffsetFirst += layoutChunkResult.mConsumed * layoutState.mLayoutDirection;
            if ( !state.isPreLayout()) {
                layoutState.mAvailable -= layoutChunkResult.mConsumed;
                // we keep a separate remaining space because mAvailable is important for recycling
                remainingSpace -= layoutChunkResult.mConsumed; //可用空间将去布局一行的空间，即剩余空间
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
        lastView = null;
    }


    private ArrayList<View> fillCachedView = new ArrayList<>();
    private View lastView = null;
    /**
     * 填充一行
     * @param recycler
     * @param layoutState
     * @param state
     * @param layoutChunkResult
     */
    private void fillScrollLayout(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state, LayoutChunkResult layoutChunkResult){
        int remainingSpace = layoutState.mAvailable;
        layoutState.mOffsetSecond = getOffsetSecond();
        if((layoutState.mInfinite || remainingSpace > 0) && layoutState.hasMore(state)) {
            while (layoutState.mOffsetSecond < getWidth() && layoutState.hasMore(state)){ //计算出一行所用child数量
                View view;
                if(lastView == null) {
                    //view = recycler.getViewForPosition(lastPosition);
                    view = layoutState.next(recycler);
                }else {
                    view = lastView;
                    lastView = null;
                }
                measureChildWithMargins(view, 0, 0);
                if(layoutState.mOffsetSecond + mOrientationHelper.getDecoratedMeasurementInOther(view) > getWidth()){
                    lastView = view;
                    break;
                }
                fillCachedView.add(view);
                layoutState.mOffsetSecond += mOrientationHelper.getDecoratedMeasurementInOther(view);
            }
            if (layoutState.mItemDirection == LayoutState.ITEM_DIRECTION_TAIL) {
                layoutState.mOffsetSecond = getOffsetSecond();
                for(int i =0; i<fillCachedView.size(); i++){
                    View view =  fillCachedView.get(i);
                    measureChildWithMargins(view, 0, 0);
                    layoutState.mOffsetSecond += mOrientationHelper.getDecoratedMeasurementInOther(view);
                    addView(view, layoutState, layoutChunkResult);
                    onLayout(view, state, layoutState, layoutChunkResult);
                }
            } else {
                for(int i =0; i<fillCachedView.size(); i++){
                    View view = fillCachedView.get(i);
                    measureChildWithMargins(view, 0, 0);
                    addView(view, layoutState, layoutChunkResult);
                    onLayout(view, state, layoutState, layoutChunkResult);
                    layoutState.mOffsetSecond -= mOrientationHelper.getDecoratedMeasurementInOther(view);
                }
            }

        }
        fillCachedView.clear();
    }

    @Override
    public void onLayout(View view, RecyclerView.State state, LayoutState layoutState, LayoutChunkResult result) {
        result.mConsumed = mOrientationHelper.getDecoratedMeasurement(view); //源码为实际高度+marginTop+marginBottom，即item的高度
        int left, top, right, bottom;
        if (mOrientation == OrientationHelper.VERTICAL) {
            left = layoutState.mOffsetSecond - mOrientationHelper.getDecoratedMeasurementInOther(view);
            right = layoutState.mOffsetSecond;
            if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
                bottom = layoutState.mOffsetFirst;
                top = layoutState.mOffsetFirst - result.mConsumed;
            } else {
                top = layoutState.mOffsetFirst;
                bottom = layoutState.mOffsetFirst + result.mConsumed;
            }
        } else {
            top = layoutState.mOffsetSecond - mOrientationHelper.getDecoratedMeasurementInOther(view);
            bottom = layoutState.mOffsetSecond ;
            if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
                right = layoutState.mOffsetFirst;
                left = layoutState.mOffsetFirst - result.mConsumed;
            } else {
                left = layoutState.mOffsetFirst;
                right = layoutState.mOffsetFirst + result.mConsumed;
            }
        }
        layoutDecoratedWithMargins(view, left, top, right, bottom);
    }
}
