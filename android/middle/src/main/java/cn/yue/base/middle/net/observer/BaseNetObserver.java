package cn.yue.base.middle.net.observer;

import java.util.concurrent.CancellationException;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.module.IAppModule;
import cn.yue.base.middle.module.manager.ModuleManager;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Description :
 * Created by yue on 2019/3/6
 */

public abstract class BaseNetObserver<T> extends DisposableSingleObserver<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onError(Throwable e) {
        ResultException resultException;
        if (e instanceof ResultException) {
            resultException = (ResultException) e;
            if (NetworkConfig.ERROR_TOKEN_INVALID.equals(resultException.getCode())
                    || NetworkConfig.ERROR_LOGIN_INVALID.equals(resultException.getCode())) {
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

    private void onLoginInvalid() {
        IAppModule iAppModule = ModuleManager.getModuleService(IAppModule.class);
        ToastUtils.showShort("登录失效~");
        iAppModule.loginInvalid();
    }
}
