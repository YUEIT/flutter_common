package cn.yue.base.common.activity.rx;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class RxLifecycleTransformer<T> implements ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {

    public abstract LifecycleTransformer<T> toBindUntilEvent();

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.
                compose(toBindUntilEvent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.
                compose(toBindUntilEvent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.
                compose(toBindUntilEvent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.
                compose(toBindUntilEvent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.
                compose(toBindUntilEvent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
