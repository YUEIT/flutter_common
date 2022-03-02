package cn.yue.base.common.widget.emoji;

import java.io.Serializable;

import cn.yue.base.common.widget.keyboard.mode.IEmotion;

/**
 * Description :
 * Created by yue on 2022/1/26
 */

public class EmojiEmotion implements Serializable, IEmotion {

    public EmojiEmotion(){

    }

    public EmojiEmotion(int codePoint, int icon, String content) {
        this.codePoint = codePoint;
        this.icon = icon;
        this.content = content;
    }

    private int icon;
    private String content = "[表情]";
    private int codePoint = 0;
    private String emoji = "";
    private int type;

    public int getCodePoint() {
        return codePoint;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int getImageResId() {
        return icon;
    }

    @Override
    public String getImageUrl() {
        return emoji;
    }

    public static EmojiEmotion fromCodePoint(int codePoint) {
        EmojiEmotion emoji = new EmojiEmotion();
        EmojiEmotion emotion = EmojiEmotionHandler.getEmojiEmotion(codePoint);
        emoji.icon = emotion.icon;
        emoji.content = emotion.content;
        emoji.codePoint = emotion.codePoint;
        emoji.type = 0;
        return emoji;
    }

    public static EmojiEmotion fromContent(String fromContent) {
        EmojiEmotion emoji = new EmojiEmotion();
        EmojiEmotion emotion = EmojiEmotionHandler.getEmojiEmotion(fromContent);
        emoji.icon = emotion.icon;
        emoji.content = emotion.content;
        emoji.codePoint = emotion.codePoint;
        emoji.type = 0;
        return emoji;
    }

    public static String newString(int codePoint) {
        return new String(Character.toChars(codePoint));
    }
}
