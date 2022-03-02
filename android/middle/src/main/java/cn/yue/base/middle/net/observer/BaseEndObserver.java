package cn.yue.base.middle.net.observer;

import cn.yue.base.middle.net.ResultException;

/**
 * Description :
 * Created by yue on 2019/3/6
 */

public abstract class BaseEndObserver<T> extends BaseNetObserver<T> {

    @Override
    public void onException(ResultException e) {
        onEnd(false, null, e);
    }

    @Override
    public void onSuccess(T t) {
        onEnd(true, t, null);
    }

    @Override
    protected void onCancel(ResultException e) {
        onEnd(false,null, e);
    }

    public abstract void onEnd(boolean success, T t, ResultException e);
}
