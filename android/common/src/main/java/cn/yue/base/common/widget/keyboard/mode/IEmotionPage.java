package cn.yue.base.common.widget.keyboard.mode;

import java.util.List;

/**
 * Description : 单页表情需实现
 * Created by yue on 2018/11/15
 */
public interface IEmotionPage{

    int getEmotionPageIndex();

    int getColumnNum(); //行数

    int getRowNum();    //列数

    int getCount();

    List<? extends IEmotion> getEmotionList();
}
