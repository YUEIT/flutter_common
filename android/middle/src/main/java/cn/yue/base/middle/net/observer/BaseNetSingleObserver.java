package cn.yue.base.middle.net.observer;

import java.util.concurrent.CancellationException;

import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Description :
 * Created by yue on 2019/3/6
 */

public abstract class BaseNetSingleObserver<T> extends DisposableSingleObserver<T> {

    @Override
    public void onError(Throwable e) {
        ResultException resultException;
        if (e instanceof ResultException) {
            resultException = (ResultException) e;
            if (NetworkConfig.ERROR_TOKEN_INVALID.equals(resultException.getCode()) || NetworkConfig.ERROR_LOGIN_INVALID.equals(resultException.getCode())) {
                onLoginInvalid();
                return;
            }
            onException(resultException);
        } else if (e instanceof CancellationException) {
            onCancel(new ResultException(NetworkConfig.ERROR_CANCEL, "请求取消~"));
        } else {
            onException(new ResultException(NetworkConfig.ERROR_OPERATION, e.getMessage()));
        }
    }

    public abstract void onException(ResultException e);

    protected void  onCancel(ResultException e) {}

    private static LoginInvalidListener invalidListener;

    public static void setLoginInvalidListener(LoginInvalidListener invalidListener) {
        BaseNetSingleObserver.invalidListener = invalidListener;
    }

    public interface LoginInvalidListener {
        void onInvalid();
    }

    private void onLoginInvalid() {
        if (invalidListener != null) {
            ToastUtils.showShortToast("登录失效~");
            invalidListener.onInvalid();
        }
    }
}
