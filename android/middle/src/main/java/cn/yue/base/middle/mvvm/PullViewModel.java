package cn.yue.base.middle.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IPullView;

public abstract class PullViewModel extends BaseViewModel implements IPullView {

    public PullViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 刷新
     */
    public void refresh() {
        refresh(loader.isFirstLoad());
    }

    /**
     * 刷新
     */
    public void refresh(boolean isPageRefreshAnim) {
        if (loader.getLoadStatus() == LoadStatus.REFRESH
                || loader.getPageStatus() == PageStatus.LOADING) {
            return;
        }
        if (isPageRefreshAnim) {
            loader.setPageStatus(PageStatus.LOADING);
        } else {
            loader.setLoadStatus(LoadStatus.REFRESH);
        }
        loadData();
    }

    protected abstract void loadData();

    private void startRefresh() {
        loader.setLoadStatus(LoadStatus.REFRESH);
    }

    @Override
    public void finishRefresh() {
        loader.setLoadStatus(LoadStatus.NORMAL);
    }

    @Override
    public void loadComplete(PageStatus status) {
        loader.setPageStatus(status);
    }

}
