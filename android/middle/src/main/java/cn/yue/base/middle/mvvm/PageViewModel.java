package cn.yue.base.middle.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import cn.yue.base.middle.net.wrapper.BaseListBean;

public abstract class PageViewModel<S> extends ListViewModel<BaseListBean<S>, S> {

    public PageViewModel(@NonNull Application application) {
        super(application);
    }
}