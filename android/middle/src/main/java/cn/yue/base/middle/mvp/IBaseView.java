package cn.yue.base.middle.mvp;


import androidx.lifecycle.Lifecycle;

import cn.yue.base.common.activity.rx.ILifecycleProvider;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public interface IBaseView extends IStatusView, IWaitView {
    ILifecycleProvider<Lifecycle.Event> getLifecycleProvider();
}
