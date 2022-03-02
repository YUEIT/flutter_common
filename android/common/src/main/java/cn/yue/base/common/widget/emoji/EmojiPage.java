package cn.yue.base.common.widget.emoji;

import java.util.List;

import cn.yue.base.common.widget.keyboard.mode.IEmotionPage;

/**
 * Description : 一页emoji
 * Created by yue on 2018/11/15
 */
public class EmojiPage implements IEmotionPage {

    private int index;

    private int column = 3;

    private int row = 7;

    private List<EmojiEmotion> emotionList;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setEmotionList(List<EmojiEmotion> emotionList) {
        this.emotionList = emotionList;
    }

    @Override
    public int getEmotionPageIndex() {
        return index;
    }

    @Override
    public List<EmojiEmotion> getEmotionList() {
        return emotionList;
    }

    @Override
    public int getColumnNum() {
        return column;
    }

    @Override
    public int getRowNum() {
        return row;
    }

    @Override
    public int getCount() {
        if (emotionList != null) {
            emotionList.size();
        }
        return 0;
    }
}
