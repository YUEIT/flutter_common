package cn.yue.base.common.widget.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import cn.yue.base.common.R;

public class HeaderScrollView extends LinearLayout {

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = 2;

    /**
     * 滚动的最大偏移量
     */
    private int topOffset = 0;

    private Scroller mScroller;
    /**
     * 表示滑动的时候，手的移动要大于这个距离才开始移动控件。
     */
    private int mTouchSlop;
    /**
     * 允许执行一个fling手势动作的最小速度值
     */
    private int mMinimumVelocity;
    /**
     * 允许执行一个fling手势动作的最大速度值
     */
    private int mMaximumVelocity;
    /**
     * 当前sdk版本，用于判断api版本
     */
    private int sysVersion;
    /**
     * 需要被滑出的头部
     */
    private View mHeadView;
    /**
     * 滑出头部的高度
     */
    private int mHeadHeight;
    /**
     * 实际显示区域大小
     */
    private int screenHeight;
    /**
     * 最大滑出的距离，等于 mHeadHeight
     */
    private int maxY = 0;
    /**
     * 最小的距离， 头部在最顶部
     */
    private int minY = 0;
    /**
     * 当前已经滚动的距离
     */
    private int mCurY;
    private VelocityTracker mVelocityTracker;
    private int mDirection;
    private int mLastScrollerY;
    /**
     * 是否允许拦截事件
     */
    private boolean mDisallowIntercept;
    /**
     * 当前点击区域是否在头部
     */
    private boolean isClickHead;
    /**
     * 滚动的监听
     */
    private OnScrollListener onScrollListener;
    private HeaderScrollHelper mScrollable;

    public interface OnScrollListener {
        void onScroll(int currentY, int maxY);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public HeaderScrollView(Context context) {
        this(context, null);
    }

    public HeaderScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderScrollView);
        topOffset = a.getDimensionPixelSize(a.getIndex(R.styleable.HeaderScrollView_hvp_topOffset), topOffset);
        a.recycle();

        mScroller = new Scroller(context);
        mScrollable = new HeaderScrollHelper();
        ViewConfiguration configuration = ViewConfiguration.get(context);
        //表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        mTouchSlop = configuration.getScaledTouchSlop();
        //允许执行一个fling手势动作的最小速度值
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        //允许执行一个fling手势动作的最大速度值
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        sysVersion = Build.VERSION.SDK_INT;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mHeadView != null && !mHeadView.isClickable()) {
            mHeadView.setClickable(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeadView = getChildAt(0);
        measureChildWithMargins(mHeadView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
        mHeadHeight = mHeadView.getMeasuredHeight();
        maxY = mHeadHeight - topOffset;
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        //让测量高度加上头部的高度
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(screenHeight + maxY, MeasureSpec.EXACTLY));
    }

    /** @param disallowIntercept 作用同 requestDisallowInterceptTouchEvent */
    public void requestHeaderViewPagerDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        mDisallowIntercept = disallowIntercept;
    }

    /**
     * 解决SwipeRefreshLayout与其滑动冲突
     */
    private SwipeRefreshLayout refreshLayout;
    public void setSwipeRefreshLayout(SwipeRefreshLayout refreshLayout){
        this.refreshLayout = refreshLayout;
    }

    /**
     * 解决溢出顶部的刷新控件的滑动冲突
     */
    private boolean isRefreshLayoutOverTop = false;
    public void setRefreshLayoutOverTop(boolean isRefreshLayoutOverTop) {
        this.isRefreshLayoutOverTop = isRefreshLayoutOverTop;
    }
    /**
     * 说明：一旦dispatTouchEvent返回true，即表示当前View就是事件传递需要的 targetView，事件不会再传递给
     * 其他View，如果需要将事件继续传递给子View，可以手动传递
     * 由于dispatchTouchEvent处理事件的优先级高于子View，也高于onTouchEvent,所以在这里进行处理
     * 好处一：当有子View，并且子View可以获取焦点的时候，子View的onTouchEvent会优先处理，如果当前逻辑
     * 在onTouchEnent中，则事件无法到达，逻辑失效
     * 好处二：当子View是拥有滑动事件时，例如ListView，ScrollView等，不需要对子View的事件进行拦截，可以
     * 全部让该父控件处理，在需要的地方手动将事件传递给子View，保证滑动的流畅性，结尾两行代码就是证明：
     * super.dispatchTouchEvent(ev);
     * return true;
     */

    /**
     * 第一次按下的x坐标
     */
    private float mDownX;
    /**
     * 第一次按下的y坐标
     */
    private float mDownY;
    /**
     * 最后一次移动的X坐标
     */
    private float mLastX;
    /**
     * 最后一次移动的Y坐标
     */
    private float mLastY;
    /**
     * 是否允许垂直滚动
     */
    private int scrollFlag = SCROLL_NULL;
    private static final int SCROLL_NULL = 0;
    private static final int SCROLL_HORIZONTAL = 1;
    private static final int SCROLL_VERTICAL = 2;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //当前手指相对于当前view的X坐标
        float currentX = ev.getX();
        //当前手指相对于当前view的Y坐标
        float currentY = ev.getY();
        //当前触摸位置与第一次按下位置的X偏移量
        float shiftX = Math.abs(currentX - mDownX);
        //当前触摸位置与第一次按下位置的Y偏移量
        float shiftY = Math.abs(currentY - mDownY);
        float deltaX;
        //滑动的偏移量，即连续两次进入Move的偏移量
        float deltaY;
        //初始化速度追踪器
        obtainVelocityTracker(ev);
        switch (ev.getAction()) {
            //Down事件主要初始化变量
            case MotionEvent.ACTION_DOWN:
                mDisallowIntercept = false;
                scrollFlag = SCROLL_NULL;
                mDownX = currentX;
                mDownY = currentY;
                mLastX = currentX;
                mLastY = currentY;
                checkIsClickHead((int) currentY, mHeadHeight, getScrollY());
                mScroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                if(refreshLayout !=null) {
                    if (canPtr()) {
                        if (!refreshLayout.isEnabled()) {
                            refreshLayout.setEnabled(true);
                        }
                    } else {
                        if (refreshLayout.isEnabled()) {
                            refreshLayout.setEnabled(false);
                        }
                    }
                }
                if (mDisallowIntercept) {
                    break;
                }
                deltaY = mLastY - currentY;
                deltaX = mLastX - currentX;
                mLastX = currentX;
                mLastY = currentY;
                if (Math.abs(deltaX) != 0 || Math.abs(deltaY) != 0) {
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        //水平滑动
                        if (scrollFlag == SCROLL_NULL) {
                            scrollFlag = SCROLL_HORIZONTAL;
                        }
                    } else if (Math.abs(deltaY) >= Math.abs(deltaX)) {
                        //垂直滑动
                        if (scrollFlag == SCROLL_NULL) {
                            scrollFlag = SCROLL_VERTICAL;
                        }
                    }
                }
                /*
                    对于垂直滑动来说
                    上滑时（当前优先） 如果当前没有置顶先滑动当前；如果已经置顶，当前不滑动，事件交给child
                    下滑时（child优先） 如果child没有置顶，先滑动child，然后滑动当前
                 */
                if (scrollFlag == SCROLL_VERTICAL) {
                    if (containerInTop() || deltaY > 0 || isClickHead) {
                        //如果是向下滑，则deltaY小于0，对于scrollBy来说
                        //正值为向上和向左滑，负值为向下和向右滑，这里要注意
                        // 将置顶向上滑，置底向下滑排除
                        if (canScrollToY(deltaY)) {
                            scrollBy(0, (int) (deltaY + 0.5));
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (scrollFlag == SCROLL_VERTICAL) {
                    //1000表示单位，每1000毫秒允许滑过的最大距离是mMaximumVelocity
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    //获取当前的滑动速度
                    float yVelocity = mVelocityTracker.getYVelocity();
                    //下滑速度大于0，上滑速度小于0
                    mDirection = yVelocity > 0 ? DIRECTION_DOWN : DIRECTION_UP;
                    //根据当前的速度和初始化参数，将滑动的惯性初始化到当前View，至于是否滑动当前View，取决于computeScroll中计算的值
                    //这里不判断最小速度，确保computeScroll一定至少执行一次
                    mScroller.fling(0, getScrollY(), 0, -(int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                    mLastScrollerY = getScrollY();
                    //更新界面，该行代码会导致computeScroll中的代码执行
                    invalidate();
                    //阻止快读滑动的时候点击事件的发生，滑动的时候，将Up事件改为Cancel就不会发生点击了
                    if ((shiftX > mTouchSlop || shiftY > mTouchSlop)) {
                        if (isClickHead || (!isInTop() && !isRefreshLayoutOverTop)) {
                            int action = ev.getAction();
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            boolean dd = super.dispatchTouchEvent(ev);
                            ev.setAction(action);
                            return dd;
                        }
                    }
                }
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        //手动将事件传递给子View，让子View自己去处理事件
        if (scrollFlag == SCROLL_VERTICAL) {
            //防止垂直滑动时，触发侧滑
            MotionEvent interceptEvent = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(),
                    ev.getAction(), mDownX, ev.getY(), ev.getMetaState());
            try {
                super.dispatchTouchEvent(interceptEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                super.dispatchTouchEvent(ev);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //消费事件，返回True表示当前View需要消费事件，就是事件的TargetView
        return true;
    }

    private boolean canScrollToY(float deltaY) {
        if (deltaY == 0) {
            return false;
        }
        if (isInTop() && deltaY > 0) {
            return false;
        }
        if (isInEnd() && deltaY < 0) {
            return false;
        }
        if (isInTop() && isInEnd()) {
            return false;
        }
        return true;
    }

    private void checkIsClickHead(int downY, int headHeight, int scrollY) {
        isClickHead = ((downY + scrollY) <= headHeight);
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();
            if (mDirection == DIRECTION_UP) {
                // 手势向上划
                if (isInTop()) {
                    //这里主要是将快速滚动时的速度对接起来，让布局看起来滚动连贯
                    //除去布局滚动消耗的时间后，剩余的时间
                    int distance = mScroller.getFinalY() - currY;
                    //除去布局滚动的距离后，剩余的距离
                    int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
                    mScrollable.smoothScrollBy(getScrollerVelocity(distance, duration), distance, duration);
                    //外层布局已经滚动到指定位置，不需要继续滚动了
                    mScroller.abortAnimation();
                    return;
                } else {
                    //将外层布局滚动到指定位置
                    scrollTo(0, currY);
                    //移动完后刷新界面
                    invalidate();
                }
            } else {
                // 手势向下划，内部View已经滚动到顶了，需要滚动外层的View
                if (containerInTop() || isClickHead) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    scrollTo(0, toY);
                    if (isInEnd()) {
                        mScroller.abortAnimation();
                        return;
                    }
                    invalidate();
                } else {
                    //这里主要是将快速滚动时的速度对接起来，让布局看起来滚动连贯
                    //除去布局滚动消耗的时间后，剩余的时间
                    int distance = mScroller.getFinalY() - currY;
                    //除去布局滚动的距离后，剩余的距离
                    int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
                    int velocity = -getScrollerVelocity(distance, duration);
                    if (isInTop()) {
                        //向下滑动时，初始状态可能不在顶部，所以要一直重绘，让computeScroll一直调用
                        //确保代码能进入上面的if判断
                        invalidate();
                    } else {
                        //向下滑动，且布局未置顶时，需要将加速度传给target
                        mScrollable.smoothScrollBy(velocity, distance, duration);
                    }
                }
            }
            mLastScrollerY = currY;
        }
    }

    @SuppressLint("NewApi")
    private int getScrollerVelocity(int distance, int duration) {
        if (mScroller == null) {
            return 0;
        } else if (sysVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return (int) mScroller.getCurrVelocity();
        } else {
            return distance / duration;
        }
    }

    /** 对滑动范围做限制 */
    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= maxY) {
            toY = maxY;
        } else if (toY <= minY) {
            toY = minY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    /** 对滑动范围做限制 */
    @Override
    public void scrollTo(int x, int y) {
        if (y >= maxY) {
            y = maxY;
        } else if (y <= minY) {
            y = minY;
        }
        mCurY = y;
        if (onScrollListener != null) {
            onScrollListener.onScroll(y, maxY);
        }
        super.scrollTo(x, y);
    }

    /** 头部置顶 最大层度隐藏 */
    public boolean isInTop() {
        if(mScrollable.getScrollableView() == null) {
            return mCurY >= maxY - screenHeight;
        }
        return mCurY >= maxY;
    }

    /** 头部置底 完全显示 */
    public boolean isInEnd() {
        return mCurY <= minY;
    }

    private int calcDuration(int duration, int timepass) {
        return duration - timepass;
    }

    public int getMaxY() {
        return maxY;
    }

    public boolean isHeadTop() {
        return mCurY == minY;
    }

    /** 是否允许下拉，与PTR结合使用 */
    public boolean canPtr() {
        return scrollFlag == SCROLL_VERTICAL && mCurY == minY && containerInTop();
    }

    private boolean containerInTop() {
        if (mScrollable.getScrollableView() == null) {
            return mCurY >= getMeasuredHeight() - screenHeight - screenHeight;
        }
        return mScrollable.isTop();
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

    public void setCurrentScrollableContainer(HeaderScrollHelper.ScrollableContainer scrollableContainer) {
        mScrollable.setCurrentScrollableContainer(scrollableContainer);
    }

    public void contentScrollToTop() {
        if (!isInTop()) {
            mScrollable.scrollToTop();
        }
    }

    public void scrollToTop() {
        mScrollable.scrollToTop();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ScrollAnimation scrollAnimation = new ScrollAnimation(mCurY);
                scrollAnimation.setInterpolator(new AccelerateInterpolator());
                scrollAnimation.setDuration(mCurY/10);
                startAnimation(scrollAnimation);
            }
        }, 50);
    }

    class ScrollAnimation extends Animation {

        private int currentY;
        ScrollAnimation(int currentY) {
            this.currentY = currentY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            int toY = (int)(((float)currentY) * (1 - interpolatedTime));
            scrollTo(0, toY);
        }
    }
}

