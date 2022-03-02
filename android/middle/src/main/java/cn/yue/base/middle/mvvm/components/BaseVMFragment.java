package cn.yue.base.middle.mvvm.components;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.mvvm.data.FinishModel;
import cn.yue.base.middle.mvvm.data.RouterModel;
import cn.yue.base.middle.router.FRouter;

public abstract class BaseVMFragment<VM extends BaseViewModel> extends BaseFragment implements IWaitView {

    protected VM viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(modelClass);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ILifecycleProvider<Lifecycle.Event> initLifecycleProvider() {
        return viewModel;
    }

    protected VM initViewModel() {
        return null;
    }

    public VM createViewModel(Class<VM> cls) {
        return new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(mActivity.getApplication())).get(cls);
    }

    @Override
    protected void initOther() {
        super.initOther();
        viewModel.waitEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (TextUtils.isEmpty(s)) {
                    dismissWaitDialog();
                } else {
                    showWaitDialog(s);
                }
            }
        });
        viewModel.routerEvent.observe(this, new Observer<RouterModel>() {
            @Override
            public void onChanged(RouterModel routerModel) {
                FRouter.getInstance()
                        .bindRouterCard(routerModel.getRouterCard())
                        .navigation(mActivity, routerModel.getRequestCode(), routerModel.getToActivity());
            }
        });
        viewModel.finishEvent.observe(this, new Observer<FinishModel>() {
            @Override
            public void onChanged(FinishModel finishModel) {
                if (finishModel.getResultCode() < 0) {
                    finishAll();
                } else {
                    Intent intent = new Intent();
                    intent.putExtras(finishModel.getBundle());
                    finishAllWithResult(finishModel.getResultCode(), intent);
                }
            }
        });
    }

    private WaitDialog waitDialog;
    @Override
    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title);
    }

    @Override
    public void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(viewModel);
        // 通过Transaction的移除再添加或者替换的操作，会出现复用已经被destroy的fragment情况
        // 这种情况下如果使用缓存，会出现liveData无法监听，因为liveData的观察者已经在destroy时被移除了
        // 这里就不使用缓存了，那么重复初始化的逻辑就需要检查了
        clearCacheView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }
}
