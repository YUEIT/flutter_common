package cn.yue.base.middle.mvvm;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.activity.rx.LifecycleTransformer;
import cn.yue.base.common.activity.rx.RxLifecycle;
import cn.yue.base.common.activity.rx.RxLifecycleAndroid;
import cn.yue.base.common.activity.rx.RxLifecycleTransformer;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvvm.data.FinishModel;
import cn.yue.base.middle.mvvm.data.LoaderLiveData;
import cn.yue.base.middle.mvvm.data.RouterModel;
import cn.yue.base.middle.router.RouterCard;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class BaseViewModel extends AndroidViewModel
        implements ILifecycleProvider<Event>, DefaultLifecycleObserver, IWaitView {

    public LoaderLiveData loader = new LoaderLiveData();
    public MutableLiveData<String> waitEvent = new MutableLiveData<>();
    public MutableLiveData<RouterModel> routerEvent = new MutableLiveData<>();
    public MutableLiveData<FinishModel> finishEvent = new MutableLiveData<>();

    public List<BaseViewModel> childViewModels = new ArrayList<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void addLifecycle(BaseViewModel childViewModel) {
        childViewModels.add(childViewModel);
    }

    private final BehaviorSubject<Event> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<Event> lifecycle() {
        return this.lifecycleSubject.hide();
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Event event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bind(this.lifecycleSubject);
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

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onCreate(owner);
            }
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onDestroy(owner);
            }
        }
        childViewModels.clear();
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_START);
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onStart(owner);
            }
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onStop(owner);
            }
        }
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onResume(owner);
            }
        }
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onPause(owner);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!childViewModels.isEmpty()) {
            for (BaseViewModel childViewModel : childViewModels) {
                childViewModel.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void showWaitDialog(String title) {
        waitEvent.postValue(title);
    }

    @Override
    public void dismissWaitDialog() {
        waitEvent.postValue("");
    }

    public void navigation(RouterCard routerCard) {
        navigation(routerCard, 0);
    }

    public void navigation(RouterCard routerCard, int requestCode) {
       navigation(routerCard, requestCode, null);
    }

    public void navigation(RouterCard routerCard, int requestCode, String toActivity) {
        RouterModel routerModel = new RouterModel(routerCard, requestCode, toActivity);
        routerEvent.postValue(routerModel);
    }

    public void finish() {
        finishEvent.postValue(new FinishModel());
    }

    public void finishForResult(int resultCode, Bundle bundle) {
        FinishModel finishModel = new FinishModel();
        finishModel.setResultCode(resultCode);
        finishModel.setBundle(bundle);
        finishEvent.postValue(finishModel);
    }
}
