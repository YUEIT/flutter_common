package cn.yue.base.common.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import cn.yue.base.common.R;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class PhotoView extends androidx.appcompat.widget.AppCompatImageView {

    private static final String TAG = "PhotoView";
    private Context mContext;
    /**
     * 最小缩放比例
     */
    private static final float MIN_SCALE = 0.95F;
    /**
     * 最大缩放比例
     */
    private static final float MAX_SCALE = 3.0F;
    /**
     * 初始比例
     */
    private static final float ORIGINAL_SCALE = 1.0F;
    /**
     * 下拉退出能达到的最小缩放比例
     */
    private static final float MIN_PULL_SCALE = 0.2F;
    /**
     * 下拉退出的临界缩放比例
     */
    private static final float PULL_FINISH_SCALE = 0.7F;
    /**
     * 初始状态下控件的宽高
     */
    private int mWidth = 0;
    private int mHeight = 0;
    /**
     * 初始状态下图片内容的宽高
     */
    private int mImageWidth = 0;
    private int mImageHeight = 0;
    /**
     * 图片内容在ImageView中的显示区域（包括缩放后）
     */
    private RectF mImageRectF;
    /**
     * 控件是否加载成功
     */
    private boolean isWidgetLoaded = false;
    /**
     * 待缩放图片是否已经设置
     */
    private boolean isImageLoaded = false;
    /**
     * 手势监听
     */
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    /**
     * 最小惯性滑动速度
     */
    private int mMinimumVelocity;
    /**
     * 最大惯性滑动速度
     */
    private int mMaximumVelocity;
    /**
     * 缩放焦点
     */
    private float focusX, focusY;
    /**
     * 是否单指操作（多指操作都交给缩放）
     */
    protected boolean isAlwaysSingleTouch = true;
    /**
     * 惯性滑动工具
     */
    private FlingUtil mFlingUtil;
    /**
     * ZoomImageView的状态
     */
    private Matrix mMatrix;
    /**
     * 当前缩放比例
     */
    private float mScale = ORIGINAL_SCALE;
    /**
     * 是否触及左边界
     */
    private boolean isLeftSide = true;
    /**
     * 是否触及右边界
     */
    private boolean isRightSide = true;
    /**
     * 单击监听
     */
    private OnClickListener mOnClickListener;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mMatrix = new Matrix();
        mScaleGestureDetector = new ScaleGestureDetector(mContext, mOnScaleGestureListener);
        mGestureDetector = new GestureDetector(mContext, mOnGestureListener);
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mFlingUtil = new FlingUtil();
    }

    public void loadImage(String url) {
        loadImage(1080, 1920, url);
    }

    public void loadImage(int resizeX, int resizeY, String uri) {
        RequestOptions requestOptions = new RequestOptions()
                .override(resizeX, resizeY)
                .placeholder(R.drawable.drawable_default_big)
                .error(R.drawable.drawable_default_big)
                .fitCenter()
                .priority(Priority.HIGH);

        Glide.with(getContext())
                .load(uri)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource == null) {
                            return false;
                        }
                        int preWidth = resource.getIntrinsicWidth();
                        int preHeight = resource.getIntrinsicHeight();
                        Log.i(TAG, "Glide drawable size:(" + preWidth + "," + preHeight + ")");
                        if (preWidth != mWidth || preHeight != mHeight) {
                            mWidth = preWidth;
                            mHeight = preHeight;
                            onRender(mWidth, mHeight);
                        }
                        return false;
                    }
                })
                .into(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(mMatrix);
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            isAlwaysSingleTouch = true;
        }
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
            pointerUp();
        }
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            pointerUp();
        }
        if (event.getPointerCount() > 1) {
            mScaleGestureDetector.onTouchEvent(event);
            isAlwaysSingleTouch = false;
        } else {
            if (!mScaleGestureDetector.isInProgress() && isAlwaysSingleTouch) {
                mGestureDetector.onTouchEvent(event);
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int preWidth = MeasureSpec.getSize(widthMeasureSpec);
        int preHeight = MeasureSpec.getSize(heightMeasureSpec);

        boolean hasSizeChanged = false;
        if (preWidth != mWidth || preHeight != mHeight) {
            hasSizeChanged = true;
        }
        mWidth = preWidth;
        mHeight = preHeight;
        isWidgetLoaded = true;

        Log.d(TAG, "*****************控件加载成功");
        //图片资源已有并且控件还没有加载 || 控件已经加载但控件尺寸发生变化
        boolean needUpdate = isImageLoaded && hasSizeChanged;
        if (needUpdate) {
            setDrawableToView();
        }
    }

    /**
     * 获得缩放移动后的图片的位置区域
     *
     * @param matrix
     * @return RectF
     */
    private RectF getScaledRect(Matrix matrix) {
        RectF rectF = new RectF(mImageRectF);
        //转化为缩放后的相对位置
        matrix.mapRect(rectF);
        return rectF;
    }

    /**
     * 有手指离开，检查当前缩放值，并规范
     */
    private void pointerUp() {
        if (mScale < ORIGINAL_SCALE) {
            reset();
            checkBorder();
        } else if (mScale > MAX_SCALE) {
            //超出最大后增加回弹
            float scaleFactor = MAX_SCALE / mScale;
            mScale = MAX_SCALE;
            mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
            invalidate();
            checkBorder();
        }
    }

    /**
     * 缩放时检查图片边缘，并添加相应的移动做调整
     */
    private void constrainMatrix() {
        constrainMatrix(0, 0);
    }

    /**
     * 缩放时检查图片边缘，并添加相应的移动做调整
     *
     * @param dx
     * @param dy
     */
    private void constrainMatrix(float dx, float dy) {
        RectF rectF = getScaledRect(mMatrix);
        float scaleImageWidth = rectF.width();
        float scaleImageHeight = rectF.height();

        if (scaleImageWidth > mWidth) {
            //right
            if (rectF.right + dx < mWidth) {
                dx = -rectF.right + mWidth;
            }
            //left
            if (rectF.left + dx > 0) {
                dx = -rectF.left;
            }
        } else {
            //center
            dx = -rectF.left + ((float) mWidth - scaleImageWidth) / 2;
        }

        if (scaleImageHeight > mHeight) {
            //bottom
            if (rectF.bottom + dy < mHeight) {
                dy = -rectF.bottom + mHeight;
            }
            //top
            if (rectF.top + dy > 0) {
                dy = -rectF.top;
            }
        } else {
            //center
            dy = -rectF.top + ((float) mHeight - scaleImageHeight) / 2;
        }

        mMatrix.postTranslate(dx, dy);
        invalidate();
        checkBorder();
    }

    /**
     * 检查图片边界
     */
    private void checkBorder() {
        RectF rectF = getScaledRect(mMatrix);
        if (rectF.left >= 0) {
            isLeftSide = true;
        } else {
            isLeftSide = false;
        }
        if (rectF.right <= mWidth) {
            isRightSide = true;
        } else {
            isRightSide = false;
        }
        printStatusLog();
    }

    /**
     * 打印图片的状态信息
     */
    private void printStatusLog() {
        RectF rectF = getScaledRect(mMatrix);
        Log.i(TAG, "位置：(" + rectF.left + "," + rectF.top + "," + rectF.right + "," + rectF.bottom + ")");
        Log.i(TAG, "是否原始大小：" + isOriginalSize() + ", 是否靠左：" + isLeftSide() + " ,是否靠右：" + isRightSide());
    }

    /**
     * 缩放手势监听
     */
    private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (!isImageLoaded) {
                return false;
            }
            float scaleFactor = detector.getScaleFactor();
            float wantScale = mScale * scaleFactor;
            if (wantScale >= MIN_SCALE) {
                mScale = wantScale;
                focusX = detector.getFocusX();
                focusY = detector.getFocusY();
                mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                invalidate();
                constrainMatrix();
            }
            return true;
        }
    };

    /**
     * 简单手势监听
     */
    private GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            if (!isAlwaysSingleTouch) {
                return true;
            }
            forceFinishScroll();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isAlwaysSingleTouch) {
                return true;
            }
            if (!isImageLoaded) {
                return true;
            }
            constrainMatrix(-distanceX, -distanceY);
            checkBorder();
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(PhotoView.this);
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!isAlwaysSingleTouch) {
                return true;
            }
            if (!isImageLoaded) {
                return true;
            }
            float x = e.getX();
            float y = e.getY();
            if (mScale == ORIGINAL_SCALE) {
                float scaleFactor = MAX_SCALE / mScale;
                mScale = MAX_SCALE;
                mMatrix.postScale(scaleFactor, scaleFactor, x, y);
                invalidate();
                checkBorder();
            } else {
                reset();
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!isAlwaysSingleTouch) {
                return true;
            }
            if (!isImageLoaded) {
                return true;
            }
            if (mScale == ORIGINAL_SCALE) {
                return true;
            }
            float absVelocityX = Math.abs(velocityX);
            if (absVelocityX < mMinimumVelocity) {
                absVelocityX = 0F;
            } else {
                absVelocityX = Math.max(mMinimumVelocity, Math.min(absVelocityX, mMaximumVelocity));
            }
            float absVelocityY = Math.abs(velocityY);
            if (absVelocityY < mMinimumVelocity) {
                absVelocityY = 0F;
            } else {
                absVelocityY = Math.max(mMinimumVelocity, Math.min(absVelocityY, mMaximumVelocity));
            }
            if (absVelocityX != 0 || absVelocityY != 0) {
                mFlingUtil.fling((int) (velocityX > 0 ? absVelocityX : -absVelocityX), (int) (velocityY > 0 ? absVelocityY : -absVelocityY));
            }
            return true;
        }
    };

    /**
     * 强制停止控件的惯性滑动
     */
    private void forceFinishScroll() {
        mFlingUtil.stop();
    }

    /**
     * 惯性滑动工具类
     * 使用fling方法开始滑动
     * 使用stop方法停止滑动
     */
    private class FlingUtil implements Runnable {
        private int mLastFlingX = 0;
        private int mLastFlingY = 0;
        private OverScroller mScroller;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        /**
         * RecyclerView使用的惯性滑动插值器
         * f(x) = (x-1)^5 + 1
         */
        private final Interpolator sQuinticInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                t -= 1.0f;
                return t * t * t * t * t + 1.0f;
            }
        };

        public FlingUtil() {
            mScroller = new OverScroller(getContext(), sQuinticInterpolator);
        }

        @Override
        public void run() {
            disableRunOnAnimationRequests();
            final OverScroller scroller = mScroller;
            if (scroller.computeScrollOffset()) {
                final int y = scroller.getCurrY();
                int dy = y - mLastFlingY;
                final int x = scroller.getCurrX();
                int dx = x - mLastFlingX;
                mLastFlingY = y;
                mLastFlingX = x;
                constrainMatrix(dx, dy);
                postOnAnimation();
            }
            enableRunOnAnimationRequests();
        }

        public void fling(int velocityX, int velocityY) {
            mLastFlingX = 0;
            mLastFlingY = 0;
            mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void stop() {
            removeCallbacks(this);
            mScroller.abortAnimation();
        }

        private void disableRunOnAnimationRequests() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                removeCallbacks(this);
                ViewCompat.postOnAnimation(PhotoView.this, this);
            }
        }
    }

    public void onRender(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
        isImageLoaded = true;
        if (isWidgetLoaded) {
            setDrawableToView();
        }
    }

    private void setDrawableToView() {
        float imageRatio = (float) mImageWidth / (float) mImageHeight;
        float widgetRatio = (float) mWidth / (float) mHeight;
        if (imageRatio > widgetRatio) {
            mImageHeight = mWidth * mImageHeight / mImageWidth;
            mImageWidth = mWidth;
        } else {
            mImageWidth = mHeight * mImageWidth / mImageHeight;
            mImageHeight = mHeight;
        }
        mImageRectF = new RectF((float) (mWidth - mImageWidth) / 2, (float) (mHeight - mImageHeight) / 2, (float) (mWidth + mImageWidth) / 2, (float) (mHeight + mImageHeight) / 2);
        Log.i(TAG, "widget size：（" + mWidth + " ," + mHeight + "）");
        Log.i(TAG, "drawable size：（" + mImageWidth + " ," + mImageHeight + "）");
        Log.i(TAG, "drawable rect：[" + mImageRectF.left + " ," + mImageRectF.top + " ," + mImageRectF.right + " ," + mImageRectF.bottom + "]");
    }

    /**
     * reset the image
     */
    public void reset() {
        mMatrix.reset();
        mScale = ORIGINAL_SCALE;
        isLeftSide = true;
        isRightSide = true;
        invalidate();
    }

    public float getScale() {
        return mScale;
    }

    public boolean isOriginalSize() {
        return mScale == ORIGINAL_SCALE;
    }

    public boolean isLeftSide() {
        return isLeftSide;
    }

    public boolean isRightSide() {
        return isRightSide;
    }

    /**
     * for method 'canScroll'
     *
     * @param direction
     * @return
     */
    public boolean canScroll(int direction) {
        return !((direction < 0 && isRightSide) || (direction > 0 && isLeftSide));
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }
}