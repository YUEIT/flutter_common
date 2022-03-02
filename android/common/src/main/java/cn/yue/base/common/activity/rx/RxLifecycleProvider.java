package cn.yue.base.common.activity.rx;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class RxLifecycleProvider implements ILifecycleProvider<Event>, DefaultLifecycleObserver {

    private final BehaviorSubject<Event> lifecycleSubject = BehaviorSubject.create();

    @NonNull
    @CheckResult
    @Override
    public final Observable<Event> lifecycle() {
        return this.lifecycleSubject.hide();
    }

    @Override
    public <T> RxLifecycleTransformer<T> toBindLifecycle() {
        return new RxLifecycleTransformer<T>() {
            @Override
            public LifecycleTransformer<T> toBindUntilEvent() {
                return bindUntilEvent(Event.ON_DESTROY);
            }
        };
    }

    @Override
    public <T> RxLifecycleTransformer<T> toBindLifecycle(Event event) {
        return new RxLifecycleTransformer<T>() {
            @Override
            public LifecycleTransformer<T> toBindUntilEvent() {
                return bindUntilEvent(event);
            }
        };
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Event event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_START);
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
    }

}
