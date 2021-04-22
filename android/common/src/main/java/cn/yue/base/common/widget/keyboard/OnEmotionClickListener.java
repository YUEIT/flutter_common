package cn.yue.base.common.widget.keyboard;

import android.content.Context;

import cn.yue.base.common.widget.keyboard.mode.IEmotion;

/**
 * Description : 表情点击
 * Created by yue on 2018/11/17
 */
public abstract class OnEmotionClickListener {

    private Context context;
    public OnEmotionClickListener(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public boolean isThis(Context context) {
        return this.context == context;
    }

    public abstract void onClick(IEmotion emotion);
}
