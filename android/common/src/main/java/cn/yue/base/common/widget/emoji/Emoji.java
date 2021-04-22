package cn.yue.base.common.widget.emoji;

import java.io.Serializable;

import cn.yue.base.common.widget.keyboard.mode.IEmotion;

/**
 * Description : emoji表情
 * Created by yue on 2019/3/11
 */

public class Emoji implements Serializable, IEmotion {

    private int icon;
    private String emoji;   // http: gif
    private int type = 0;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getImageResId() {
        return icon;
    }

    @Override
    public String getImageUrl() {
        return emoji;
    }
}
