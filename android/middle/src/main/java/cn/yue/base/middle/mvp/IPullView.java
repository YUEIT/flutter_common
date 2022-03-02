package cn.yue.base.middle.mvp;

import cn.yue.base.middle.components.load.PageStatus;

public interface IPullView {
    void finishRefresh();
    void loadComplete(PageStatus pageStatus);
}
