package cn.yue.base.common.widget.emoji;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;

import cn.yue.base.common.R;

/**
 * Description :
 * Created by yue on 2022/1/26
 */

public class EmojiEmotionHandler {

    public static void addEmojis(Context context, Spannable text, int emojiSize,
                                 int textSize, boolean useSystemDefault) {
        addEmojis(context, text, emojiSize, textSize, 0, -1, useSystemDefault);
    }

    public static void addEmojis(Context context, Spannable text, int emojiSize,
                                 int textSize, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault) {
            return;
        }
        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length + index;
        if (length < 0 || length >= textLengthToProcessMax) {
            textLengthToProcess = textLength;
        }
        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }
        int i = index;
        while (i < textLengthToProcess) {
            int skip = 0;
            int icon = 0;
            int unicode = Character.codePointAt(text, i);
            skip = Character.charCount(unicode);
            if (unicode > 0xff) {
                icon = getEmojiResource(unicode);
            }
            if (icon == 0 && i + skip < textLengthToProcess) {
                int followUnicode = Character.codePointAt(text, i + skip);
                if (followUnicode == 0x20e3) {
                    int followSkip = 0;
                    skip += followSkip;
                } else {
                    int followSkip = Character.charCount(followUnicode);
                    if (unicode == 0x1f1e8) {
                        if (followUnicode == 0x1f1f3) {
                            icon = R.drawable.emoji_1f1e8_1f1f3;
                        } else {
                            icon = 0;
                        }
                    } else {
                        followSkip = 0;
                    }
                    skip += followSkip;
                }
            }
            if (icon > 0) {
                text.setSpan(new EmojiconSpan(context, icon, emojiSize, textSize),
                        i, i+skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            i += skip;
        }
    }

    public static void ensure(Context context, Spannable spannable, int emojiSize, int textSize) {
        char[] chars = spannable.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isHighSurrogate(chars[i])) {
                int codePoint;
                boolean isSurrogatePair;
                if (Character.isLowSurrogate(chars[i])) {
                    if (i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                        continue;
                    }
                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                } else {
                    codePoint = chars[i];
                    isSurrogatePair = false;
                }
                int resId = getEmojiResource(codePoint);
                if (resId != 0) {
                    spannable.setSpan(new EmojiconSpan(context, resId, emojiSize, textSize),
                            isSurrogatePair? i-1: i, i+1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }
        }
    }

    private static int getEmojiResource(int codePoint) {
        for (EmojiEmotion emoji : EmojiCollect.emojiCollectArray) {
            if (emoji.getCodePoint() == codePoint) {
                return emoji.getImageResId();
            }
        }
        return 0;
    }

    public static EmojiEmotion getEmojiEmotion(int codePoint) {
        for (EmojiEmotion emoji : EmojiCollect.emojiCollectArray) {
            if (emoji.getCodePoint() == codePoint) {
                return emoji;
            }
        }
        return null;
    }

    public static EmojiEmotion getEmojiEmotion(String content) {
        for (EmojiEmotion emoji : EmojiCollect.emojiCollectArray) {
            if (emoji.getContent().equals(content)) {
                return emoji;
            }
        }
        return null;
    }
}
