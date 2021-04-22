package cn.yue.base.common.widget.keyboard.mode;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.yue.base.common.widget.emoji.EmojiUtils;
import cn.yue.base.common.widget.keyboard.OnEmotionClickListener;

/**
 * Description : 表情管理类，初始化和清空表情
 * Created by yue on 2018/11/16
 */
public class EmotionUtils {

    private EmotionUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static List<IEmotionSort> allEmotionSort;
    public static void initAllEmotion() {
        EmojiUtils.initEmojiSort(0);
        List<IEmotionSort> list = new ArrayList<>();
        if (EmojiUtils.getEmojiSort() != null) {
            list.add(EmojiUtils.getEmojiSort());
        }
        IEmotionSort sort1 = EmojiUtils.initEmojiSortTest(EmojiUtils.getEmojiSort().getCount(), 1);
        IEmotionSort sort2 = EmojiUtils.initEmojiSortTest(sort1.getFirstPagePosition() + sort1.getCount(), 2);
        IEmotionSort sort3 = EmojiUtils.initEmojiSortTest(sort2.getFirstPagePosition() + sort2.getCount(), 3);
        IEmotionSort sort4 = EmojiUtils.initEmojiSortTest(sort3.getFirstPagePosition() + sort3.getCount(), 4);
        IEmotionSort sort5 = EmojiUtils.initEmojiSortTest(sort4.getFirstPagePosition() + sort4.getCount(), 5);
        IEmotionSort sort6 = EmojiUtils.initEmojiSortTest(sort5.getFirstPagePosition() + sort5.getCount(), 6);
        IEmotionSort sort7 = EmojiUtils.initEmojiSortTest(sort6.getFirstPagePosition() + sort6.getCount(), 7);
        IEmotionSort sort8 = EmojiUtils.initEmojiSortTest(sort7.getFirstPagePosition() + sort7.getCount(), 8);
        IEmotionSort sort9 = EmojiUtils.initEmojiSortTest(sort8.getFirstPagePosition() + sort8.getCount(), 9);
        list.add(sort1);
        list.add(sort2);
        list.add(sort3);
        list.add(sort4);
        list.add(sort5);
        list.add(sort6);
        list.add(sort7);
        list.add(sort8);
        list.add(sort9);
        allEmotionSort = list;
    }

    public static void clearAllEmotion(Context context) {
        EmojiUtils.clearEmojiSort();
        for(Iterator iterator = emotionClickListenerList.iterator(); iterator.hasNext(); ) {
            OnEmotionClickListener e = (OnEmotionClickListener) iterator.next();
            if (e.isThis(context)) {
                iterator.remove();
            }
        }
    }

    public static List<IEmotionSort> getAllEmotionSort() {
        if (allEmotionSort != null) {
            return allEmotionSort;
        }
        return new ArrayList<>();
    }

    public static List<IEmotionPage> getAllEmotionPage() {
        List<IEmotionPage> list = new ArrayList<>();
        for (IEmotionSort sort : getAllEmotionSort()) {
            if (sort.getEmotionPage() != null) {
                list.addAll(sort.getEmotionPage());
            }
        }
        return list;
    }

    public static boolean isSameSort(int position, int lastPosition) {
        return getSortIndexByPosition(position) == getSortIndexByPosition(lastPosition);
    }

    public static int getSortIndexByPosition(int position) {
        if (getEmotionSortByPosition(position) != null) {
            return getEmotionSortByPosition(position).getSortIndex();
        }
        return -1;
    }

    public static IEmotionSort getEmotionSortByPosition(int position) {
        for (IEmotionSort sort : getAllEmotionSort()) {
            if (sort.getFirstPagePosition() <= position && sort.getFirstPagePosition() + sort.getCount() > position) {
                return sort;
            }
        }
        return null;
    }

    private static List<OnEmotionClickListener> emotionClickListenerList = new ArrayList<>();

    public static void setOnEmotionClickListener(OnEmotionClickListener clickListener) {
        if (clickListener != null) {
            for(Iterator iterator = emotionClickListenerList.iterator(); iterator.hasNext(); ) {
                OnEmotionClickListener e = (OnEmotionClickListener) iterator.next();
                if (e.getContext() == clickListener.getContext()) {
                    iterator.remove();
                }
            }
            emotionClickListenerList.add(clickListener);
        }
    }

    public static List<OnEmotionClickListener> getOnEmotionClickListener() {
        return emotionClickListenerList;
    }
}

