package cn.yue.base.middle.net.observer;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.net.ResultException;

/**
 * Description :
 * Created by yue on 2019/3/31
 */

public abstract class BaseWaitObserver<T> extends BaseNetObserver<T> {

    private IWaitView iBaseView;
    private String title;
    public BaseWaitObserver(IWaitView iBaseView) {
        this.iBaseView = iBaseView;
    }

    public BaseWaitObserver(IWaitView iBaseView, String title) {
        this.iBaseView = iBaseView;
        this.title = title;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (iBaseView != null) {
            iBaseView.showWaitDialog(title);
        }
    }

    @Override
    public void onException(ResultException e) {
        ToastUtils.showShort(e.getMessage());
        if (iBaseView != null) {
            iBaseView.dismissWaitDialog();
        }
    }

    @Override
    public void onSuccess(T t) {
        if (iBaseView != null) {
            iBaseView.dismissWaitDialog();
        }
        onNext(t);
    }

    @Override
    protected void onCancel(ResultException e) {
        super.onCancel(e);
        if (iBaseView != null) {
            iBaseView.dismissWaitDialog();
        }
    }

    public abstract void onNext(T t);
}
