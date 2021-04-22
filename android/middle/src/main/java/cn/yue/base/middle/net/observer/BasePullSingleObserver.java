package cn.yue.base.middle.net.observer;


import cn.yue.base.middle.components.BasePullFragment;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.net.ResultException;

/**
 * Description :
 * Created by yue on 2019/4/1
 */

public abstract class BasePullSingleObserver<T> extends BaseNetSingleObserver<T> {

    private BasePullFragment fragment;
    public BasePullSingleObserver(BasePullFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fragment != null) {
            fragment.showLoadingView();
        }
    }

    @Override
    public void onSuccess(T t) {
        if (fragment != null) {
            fragment.stopRefreshAnim();
            fragment.showStatusView(PageStatus.STATUS_SUCCESS);
        }
        onNext(t);
    }

    @Override
    public void onException(ResultException e) {
        if (fragment != null) {
            fragment.showFailedView(e);
            fragment.stopRefreshAnim();
        }
    }

    @Override
    protected void onCancel(ResultException e) {
        super.onCancel(e);
        if (fragment != null) {
            fragment.stopRefreshAnim();
        }
    }

    public abstract void onNext(T t);
}
