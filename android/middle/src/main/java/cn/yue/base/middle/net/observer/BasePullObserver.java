package cn.yue.base.middle.net.observer;


import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IPullView;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;

/**
 * Description :
 * Created by yue on 2019/4/1
 */

public abstract class BasePullObserver<T> extends BaseNetObserver<T> {

    private IPullView iPullView;

    public BasePullObserver(IPullView iPullView) {
        this.iPullView = iPullView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(T t) {
        if (iPullView != null) {
            iPullView.finishRefresh();
            iPullView.loadComplete(PageStatus.NORMAL);
        }
        onNext(t);
    }

    @Override
    public void onException(ResultException e) {
        if (iPullView != null) {
            if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                iPullView.loadComplete(PageStatus.NO_NET);
            } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                iPullView.loadComplete(PageStatus.NO_DATA);
            } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                iPullView.loadComplete(PageStatus.ERROR);
                ToastUtils.showShort(e.getMessage());
            } else {
                iPullView.loadComplete(PageStatus.ERROR);
                ToastUtils.showShort(e.getMessage());
            }
            iPullView.finishRefresh();
        }
    }

    @Override
    protected void onCancel(ResultException e) {
        super.onCancel(e);
        if (iPullView != null) {
            iPullView.finishRefresh();
        }
    }

    public abstract void onNext(T t);
}
