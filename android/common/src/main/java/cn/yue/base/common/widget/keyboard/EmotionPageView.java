package cn.yue.base.common.widget.keyboard;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.widget.keyboard.mode.IEmotionPage;

/**
 * Description :
 * Created by yue on 2018/11/14
 */
public class EmotionPageView extends LinearLayout {

    public EmotionPageView(Context context) {
        this(context, null);
    }

    public EmotionPageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private EmotionPageAdapter emotionPageAdapter;
    private ViewPager viewPager;
    private int lastPosition;
    private void initView(final Context context) {
        inflate(context, R.layout.layout_emotion_page, this);
        viewPager = findViewById(R.id.emotionVP);
        viewPager.setAdapter(emotionPageAdapter = new EmotionPageAdapter(null));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position, lastPosition);
                }
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setData(List<? extends IEmotionPage> pageList) {
        if (emotionPageAdapter != null) {
            emotionPageAdapter.setPageList(pageList);
        }
    }

    public void setCurrentItem(int position) {
        lastPosition = position;
        if (viewPager != null) {
            viewPager.setCurrentItem(position);
        }
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position, int lastPosition);
    }

    private OnPageChangeListener onPageChangeListener;
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
