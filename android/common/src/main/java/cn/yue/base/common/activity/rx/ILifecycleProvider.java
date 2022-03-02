package cn.yue.base.common.activity.rx;


import androidx.lifecycle.LifecycleObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.CheckReturnValue;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public interface ILifecycleProvider<E> extends LifecycleObserver {

    @CheckReturnValue
    Observable<E> lifecycle();

    @CheckReturnValue
    <T> RxLifecycleTransformer<T> toBindLifecycle();

    @CheckReturnValue
    <T> RxLifecycleTransformer<T> toBindLifecycle(E e);

}
