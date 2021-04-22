package cn.yue.base.common.widget.emoji;

import java.util.List;

import cn.yue.base.common.widget.keyboard.mode.IEmotionPage;
import cn.yue.base.common.widget.keyboard.mode.IEmotionSort;

/**
 * Description : emoji分类
 * Created by yue on 2018/11/16
 */
public class EmojiSort implements IEmotionSort {

    private int sortIndex;
    private int firstPagePosition;
    private String sortName;
    private String iconUrl;
    private int iconResId;
    private int count;
    private List<EmojiPage> pageList;

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public void setFirstPagePosition(int firstPagePosition) {
        this.firstPagePosition = firstPagePosition;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<EmojiPage> getPageList() {
        return pageList;
    }

    public void setPageList(List<EmojiPage> pageList) {
        this.pageList = pageList;
    }

    @Override
    public int getSortIndex() {
        return sortIndex;
    }

    @Override
    public int getFirstPagePosition() {
        return firstPagePosition;
    }

    @Override
    public String getSortName() {
        return sortName;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public int getImageResId() {
        return iconResId;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public List<? extends IEmotionPage> getEmotionPage() {
        return pageList;
    }
}

