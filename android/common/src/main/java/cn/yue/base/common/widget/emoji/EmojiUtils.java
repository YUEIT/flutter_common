package cn.yue.base.common.widget.emoji;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.R;

/**
 * Description : emoji管理工具类，主要初始化获取类别、分页数据
 * Created by yue on 2018/11/16
 */
public final class EmojiUtils {

    private static EmojiSort emojiSort;

    public static EmojiSort getEmojiSort() {
        return emojiSort;
    }

    public static void clearEmojiSort() {
        emojiSort = null;
    }

    private static Context context;

    public static void setContext(Context mContext) {
        context = mContext;
    }

    public static void initEmojiSort(int position) {
        EmojiSort sort = new EmojiSort();
        List<Emoji> emojiList = EmojiConstant.initEmojiList();
        List<EmojiPage> pageList = new ArrayList<>();
        for (int i = 0; i < emojiList.size(); ) {
            EmojiPage emojiPage = new EmojiPage();
            int end = i + emojiPage.getRowNum() * emojiPage.getColumnNum();
            if (end > emojiList.size()) {
                end = emojiList.size();
            }
            emojiPage.setIndex(i / emojiPage.getRowNum() * emojiPage.getColumnNum() + 1);
            emojiPage.setEmotionList(emojiList.subList(i, end));
            pageList.add(emojiPage);
            i = end;
        }
        sort.setPageList(pageList);
        sort.setFirstPagePosition(position);
        sort.setCount(pageList.size());
        sort.setSortIndex(0);
        sort.setSortName("emoji");
        sort.setIconResId(R.drawable.icon_post_smile);
        emojiSort = sort;
    }

    public static EmojiSort initEmojiSortTest(int position, int sortId) {
        EmojiSort sort = new EmojiSort();
        List<Emoji> emojiList = EmojiConstant.initEmojiList();
        List<EmojiPage> pageList = new ArrayList<>();
        for (int i = 0; i < emojiList.size(); ) {
            EmojiPage emojiPage = new EmojiPage();
            int end = i + emojiPage.getRowNum() * emojiPage.getColumnNum();
            if (end > emojiList.size()) {
                end = emojiList.size();
            }
            emojiPage.setIndex(i / emojiPage.getRowNum() * emojiPage.getColumnNum() + 1);
            emojiPage.setEmotionList(emojiList.subList(i, end));
            pageList.add(emojiPage);
            i = end;
        }
        sort.setPageList(pageList);
        sort.setFirstPagePosition(position);
        sort.setCount(pageList.size());
        sort.setSortIndex(sortId);
        sort.setSortName("emoji");
        sort.setIconUrl("");
        return sort;
    }
}
