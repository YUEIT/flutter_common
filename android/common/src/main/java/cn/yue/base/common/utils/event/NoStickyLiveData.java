package cn.yue.base.common.utils.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Description :
 * Created by yue on 2022/1/27
 */

public class NoStickyLiveData<T> extends MutableLiveData<T> {

    private int version = -1;

    @Override
    public void setValue(T value) {
        version++;
        super.setValue(value);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, new WrapperObserver(observer));
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(new WrapperObserver(observer));
    }

    class WrapperObserver implements Observer<T>{

        WrapperObserver(Observer<? super T> observer) {
            this.observer = observer;
        }

        private int bindVersion;
        private final Observer<? super T> observer;

        @Override
        public void onChanged(T t) {
            if (bindVersion < version) {
                observer.onChanged(t);
            }
        }
    }
}


