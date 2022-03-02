package cn.yue.base.middle.mvvm.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.PageStatus;

public class LoaderLiveData {

    private boolean isFirstLoad;
    private MutableLiveData<PageStatus> pageStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<LoadStatus> loadStatusLiveData = new MutableLiveData<>();

    public LoaderLiveData() {
        isFirstLoad = true;
    }

    public void setPageStatus(PageStatus pageStatus) {
        pageStatusLiveData.setValue(pageStatus);
    }

    public PageStatus getPageStatus() {
        return pageStatusLiveData.getValue();
    }

    public void setLoadStatus(LoadStatus loadStatus) {
        loadStatusLiveData.setValue(loadStatus);
    }

    public LoadStatus getLoadStatus() {
        return loadStatusLiveData.getValue();
    }

    public void observePage(@NonNull LifecycleOwner lifecycleOwner, Observer<? super PageStatus> observer) {
        pageStatusLiveData.observe(lifecycleOwner, observer);
    }

    public void observeLoad(@NonNull LifecycleOwner lifecycleOwner, Observer<? super LoadStatus> observer) {
        loadStatusLiveData.observe(lifecycleOwner, observer);
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public void setFirstLoad(boolean firstLoad) {
        isFirstLoad = firstLoad;
    }
}
