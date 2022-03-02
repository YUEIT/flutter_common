package cn.yue.base.common.widget.keyboard;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.utils.device.KeyboardUtils;
import cn.yue.base.common.utils.code.SPUtils;
import cn.yue.base.common.widget.keyboard.mode.EmotionUtils;
import cn.yue.base.common.widget.keyboard.mode.IEmotionSort;

/**
 * Description : 表情布局
 * Created by yue on 2018/11/14
 */
public class EmotionLayout extends LinearLayout implements IKeyboard{

    public EmotionLayout(Context context) {
        this(context, null);
    }

    public EmotionLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private EmotionPageView pageView;
    private void initView(Context context) {
        EmotionUtils.initAllEmotion();
        inflate(context, R.layout.layout_emotion_sort, this);
        pageView = findViewById(R.id.emotionPageLayout);
        final EmotionIndicatorView indicatorView = findViewById(R.id.indicatorView);
        if (!EmotionUtils.getAllEmotionSort().isEmpty()) {
            indicatorView.playTo(0, EmotionUtils.getAllEmotionSort().get(0).getCount());
        }
        final EmotionBottomSortLayout bottomSortLayout = findViewById(R.id.bottomLayout);
        bottomSortLayout.setEmotionSortList(EmotionUtils.getAllEmotionSort());
        bottomSortLayout.setOnClickEmotionSortListener(new EmotionBottomSortLayout.OnClickEmotionSortListener() {
            @Override
            public void onClick(IEmotionSort sort) {
                pageView.setCurrentItem(sort.getFirstPagePosition());
                indicatorView.playTo(0, sort.getCount());
            }
        });
        pageView.setData(EmotionUtils.getAllEmotionPage());
        pageView.setOnPageChangeListener(new EmotionPageView.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position, int lastPosition) {
                IEmotionSort currentSort = EmotionUtils.getEmotionSortByPosition(position);
                IEmotionSort lastSort = EmotionUtils.getEmotionSortByPosition(lastPosition);
                if(currentSort == null || lastSort == null) {
                    return;
                }
                Log.d("keyboard", "position:" +position+ ",last:" + lastPosition + "; sort id" + currentSort.getSortIndex() +",last"+ lastSort.getSortIndex());
                if (currentSort.getSortIndex() == lastSort.getSortIndex()) {
                    indicatorView.playBy(lastPosition - currentSort.getFirstPagePosition(),
                            position - currentSort.getFirstPagePosition(), currentSort.getCount());
                } else {
                    if (position - lastPosition > 0) {
                        indicatorView.playTo(0, currentSort.getCount());
                    } else {
                        indicatorView.playTo(currentSort.getCount() - 1, currentSort.getCount());
                    }
                    bottomSortLayout.smoothScrollToPosition(currentSort.getSortIndex());
                }
            }
        });
    }

    public void setOnEmotionClickListener(OnEmotionClickListener onEmotionClickListener) {
        pageView.setOnEmotionClickListener(onEmotionClickListener);
    }

    protected final static int MIN_HEIGHT = 50;

    private boolean visible;
    public void toggleEmotionShow(EditText editText) {
        visible = !visible;
        if (visible) {
            KeyboardUtils.hideSoftInput((Activity) getContext());
        } else {
            KeyboardUtils.showSoftInput(editText);
        }
        changeSize(keyboardHeight);
    }

    @Override
    public void onKeyboardOpen() {
        setVisibility(VISIBLE);
        visible = false;
        changeSize(getKeyboardHeight());
    }

    @Override
    public void onKeyboardClose() {
        if (!visible) {
            changeSize(0);
        }
    }

    protected void changeSize(int height) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        setLayoutParams(layoutParams);
        if (listenerList != null && !listenerList.isEmpty()) {
            for (OnSizeChangeListener onSizeChangeListener : listenerList) {
                onSizeChangeListener.onSizeChange(height);
            }
        }
    }

    private List<OnSizeChangeListener> listenerList = new ArrayList<>();
    public void addOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        if (onSizeChangeListener != null) {
            listenerList.add(onSizeChangeListener);
        }
    }

    public interface OnSizeChangeListener {
        void onSizeChange(int height);
    }

    private int keyboardHeight;
    @Override
    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
        setMaxKeyboardHeight(keyboardHeight);
    }

    private int maxKeyboardHeight;  //弹起状态下的键盘高度
    public void setMaxKeyboardHeight(int keyboardHeight) {
        if (keyboardHeight > maxKeyboardHeight) {
            this.maxKeyboardHeight = keyboardHeight;
            SPUtils.getInstance().put("keyboardHeight", keyboardHeight);
        }
    }

    public int getMaxKeyboardHeight() {
        if (maxKeyboardHeight < MIN_HEIGHT) {
            return SPUtils.getInstance().getInt("keyboardHeight");
        }
        return maxKeyboardHeight;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //此方法较慢于组件的destroy
        EmotionUtils.clearAllEmotion();
    }

}
