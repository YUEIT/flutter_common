package cn.yue.base.common.widget.keyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import cn.yue.base.common.Constant;
import cn.yue.base.common.utils.device.KeyboardUtils;


/**
 * Description : 键盘
 * Created by yue on 2018/11/14
 */
public abstract class BaseKeyboardLayout extends RelativeLayout implements IKeyboard{

    protected EmotionLayout emotionLayout;
    private final KeyboardHelp keyboardHelp = new KeyboardHelp();

    public BaseKeyboardLayout(Context context) {
        this(context, null);
    }

    public BaseKeyboardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        keyboardHelp.addOnSoftKeyBoardVisibleListener(context, this);
        inflate(context, getLayoutId(), this);
        emotionLayout = getEmotionLayout();
        //初始时，还没测绘到尺寸，先隐藏
        emotionLayout.setVisibility(GONE);
        initView(context);
    }

    protected void initView(Context context) { }

    protected abstract int getLayoutId();

    protected abstract EmotionLayout getEmotionLayout();

    @Override
    public void onKeyboardOpen() {
        emotionLayout.setKeyboardHeight(keyboardHelp.getKeyboardHeight());
        emotionLayout.onKeyboardOpen();
    }

    @Override
    public void onKeyboardClose() {
        emotionLayout.setKeyboardHeight(keyboardHelp.getKeyboardHeight());
        emotionLayout.onKeyboardClose();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxParentHeight != 0) {
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected int mMaxParentHeight;
    private static final int ID_CHILD = 101;

    @SuppressLint("ResourceType")
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childSum = getChildCount();
        if (childSum > 1) {
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child, index, params);
        if (childSum == 0) {
            if (child.getId() < 0) {
                child.setId(ID_CHILD);
            }
            RelativeLayout.LayoutParams paramsChild = (RelativeLayout.LayoutParams) child.getLayoutParams();
            paramsChild.addRule(ALIGN_PARENT_BOTTOM);
            child.setLayoutParams(paramsChild);
        } else if (childSum == 1) {
            RelativeLayout.LayoutParams paramsChild = (RelativeLayout.LayoutParams) child.getLayoutParams();
            paramsChild.addRule(ABOVE, ID_CHILD);
            child.setLayoutParams(paramsChild);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    private boolean isFullScreen(Activity context) {
        return (context.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }
}
