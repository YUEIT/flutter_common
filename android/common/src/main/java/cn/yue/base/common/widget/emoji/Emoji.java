package cn.yue.base.common.widget.emoji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emoji {

    private static List<EmojiEmotion> emojiEmotions;

    public static List<EmojiEmotion> getEmojiList() {
        if (null == emojiEmotions) {
            emojiEmotions = new ArrayList<>();
            emojiEmotions.addAll(Arrays.asList(DATA));
        }
        return emojiEmotions;
    }

    private static EmojiEmotion[] DATA = new EmojiEmotion[] {
            EmojiEmotion.fromCodePoint(0x1f642),
    };
}
