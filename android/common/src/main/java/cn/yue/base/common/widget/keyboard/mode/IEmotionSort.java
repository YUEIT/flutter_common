package cn.yue.base.common.widget.keyboard.mode;

import java.util.List;

/**
 * Description : 表情分类需实现
 * Created by yue on 2018/11/15
 */
public interface IEmotionSort {

    int getFirstPagePosition();

    int getSortIndex();

    String getSortName();

    String getIconUrl();

    int getImageResId();

    int getCount();

    List<? extends IEmotionPage> getEmotionPage();
}
