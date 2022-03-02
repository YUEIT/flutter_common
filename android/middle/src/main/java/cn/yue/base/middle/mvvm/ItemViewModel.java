package cn.yue.base.middle.mvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import cn.yue.base.common.activity.rx.RxLifecycleTransformer;
import cn.yue.base.middle.router.RouterCard;
import io.reactivex.SingleTransformer;

public abstract class ItemViewModel extends BaseViewModel {

    private BaseViewModel parentViewModel;

    public ItemViewModel(@NonNull BaseViewModel parentViewModel) {
        super(parentViewModel.getApplication());
        this.parentViewModel = parentViewModel;
    }

    protected int getItemType() {
        return this.hashCode() % 100;
    }

    public abstract int getLayoutId();


    @Override
    public <T> RxLifecycleTransformer<T> toBindLifecycle() {
        return parentViewModel.toBindLifecycle();
    }

    @Override
    public <T> RxLifecycleTransformer<T> toBindLifecycle(Lifecycle.Event event) {
        return parentViewModel.toBindLifecycle(event);
    }

    @Override
    public void showWaitDialog(String title) {
        parentViewModel.showWaitDialog(title);
    }

    @Override
    public void dismissWaitDialog() {
        parentViewModel.dismissWaitDialog();
    }

    @Override
    public void navigation(RouterCard routerCard) {
        parentViewModel.navigation(routerCard);
    }

    @Override
    public void navigation(RouterCard routerCard, int requestCode) {
        parentViewModel.navigation(routerCard, requestCode);
    }

    @Override
    public void navigation(RouterCard routerCard, int requestCode, String toActivity) {
        parentViewModel.navigation(routerCard, requestCode, toActivity);
    }

    @Override
    public void finish() {
        parentViewModel.finish();
    }

    @Override
    public void finishForResult(int resultCode, Bundle bundle) {
        parentViewModel.finishForResult(resultCode, bundle);
    }
}
