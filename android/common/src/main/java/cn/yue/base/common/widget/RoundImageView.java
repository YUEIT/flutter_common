package cn.yue.base.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;

import cn.yue.base.common.R;

/**
 * Description : 圆角图片（目前模式支持FIT_XY、CENTER_CROP）
 * Created by yue on 2017/7/19
 */

public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {
    /**
     * 图片的类型，圆形or圆角
     */
    private int type = 1;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    /**
     * 圆角大小的默认值
     */
    private static final int BORDER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    private Paint mForePaint;
    private final Paint mBorderPaint = new Paint();
    /**
     * 圆角的半径
     */
    private int mRadius;
    private int borderColor;
    private int borderWidth;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    private int mWidth;
    private RectF mRoundRect;
    private Drawable foreground; //View的foreground需要M，这里自定义一个

    public RoundImageView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mForePaint = new Paint();
        mForePaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);

        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.RoundImageView_border_radius, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                BORDER_RADIUS_DEFAULT, getResources()
                                        .getDisplayMetrics()));// 默认为10dp
        type = a.getInt(R.styleable.RoundImageView_view_type, TYPE_ROUND);// 默认为Circle
        borderColor = a.getColor(R.styleable.RoundImageView_border_color, Color.TRANSPARENT);
        borderWidth = a.getDimensionPixelOffset(R.styleable.RoundImageView_border_width, 0);
        foreground = a.getDrawable(R.styleable.RoundImageView_foreground);
        a.recycle();
        setScaleType(getScaleType());
    }

    public RoundImageView(Context context) {
        this(context, null);
    }


    @Override
    public void setScaleType(ScaleType scaleType) {
        if(scaleType!= ScaleType.CENTER_CROP && scaleType!= ScaleType.FIT_XY){
            scaleType = ScaleType.CENTER_CROP;
        }
        super.setScaleType(scaleType);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    //涉及缩放  支持其他模式，参考ImageView 中configureBounds中的操作添加
    private BitmapShader getBitmapShader(Drawable drawable){
        Bitmap bmp = drawableToBitmap(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        float scaleX = 1.0f;
        float scaleY = 1.0f;

        final int dwidth = bmp.getWidth();
        final int dheight = bmp.getHeight();
        final int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int vheight = getHeight() - getPaddingTop() - getPaddingBottom();

        if(ScaleType.FIT_XY == getScaleType()){
            scaleX = (float) getWidth()/(float)bmp.getWidth();
            scaleY = (float) getHeight()/(float)bmp.getHeight();
            mMatrix.setScale(scaleX, scaleY);
        }
        if(ScaleType.CENTER_CROP == getScaleType()) {
            float dx = 0, dy = 0;
            if (dwidth * vheight > vwidth * dheight) {
                scale = (float) vheight / (float) dheight;
                dx = (vwidth - dwidth * scale) * 0.5f;
            } else {
                scale = (float) vwidth / (float) dwidth;
                dy = (vheight - dheight * scale) * 0.5f;
            }
            mMatrix.setScale(scale, scale);
            mMatrix.postTranslate(Math.round(dx), Math.round(dy));
        }
        return mBitmapShader;
    }

    private void setBitmapPaint(){
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        BitmapShader bitmapShader = getBitmapShader(drawable);
        // 设置变换矩阵
        bitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(bitmapShader);
    }

    private void setBorderPaint(){
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(borderWidth);
    }

    private void setForePaint(){
        Drawable drawable ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && foreground != null) {
            drawable = foreground;
        }else{
            drawable = new ColorDrawable(Color.TRANSPARENT);
        }
        if (drawable == null) {
            return;
        }
        BitmapShader bitmapShader = getBitmapShader(drawable);
        // 设置变换矩阵
        bitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mForePaint.setShader(bitmapShader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setBitmapPaint();
        setBorderPaint();
        setForePaint();
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mForePaint);
            if(borderWidth >0) {
                canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                        mBorderPaint);
            }
        } else {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
            canvas.drawCircle(mRadius, mRadius, mRadius, mForePaint);
            // drawSomeThing(canvas);
        }
    }


    @Override
    public void onDrawForeground(Canvas canvas) {
        //super.onDrawForeground(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 圆角图片的范围
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, w, h);
    }

    /**
     * drawable转bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if(w<0 || h<0){
            w = getMeasuredWidth();
            h = getMeasuredHeight();
            if(w<0 || h<0){
                w = 1;
                h = 1;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setBorderStyle(int width, int color){
        borderColor = color;
        borderWidth = width;
        invalidate();
    }


    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

}
