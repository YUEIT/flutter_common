package cn.yue.base.common.activity;


import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.SingleTransformer;
import io.reactivex.annotations.CheckReturnValue;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public interface ILifecycleProvider<E> extends LifecycleProvider<E> {

    @CheckReturnValue
    <T> SingleTransformer<T, T> toBindLifecycle();

    @CheckReturnValue
    <T> SingleTransformer<T, T> toBindLifecycle(E e);
}
