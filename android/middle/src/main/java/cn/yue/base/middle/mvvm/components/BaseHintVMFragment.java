package cn.yue.base.middle.mvvm.components;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.lifecycle.Observer;

import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.R;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.view.PageHintView;

public abstract class BaseHintVMFragment<VM extends BaseViewModel> extends BaseVMFragment<VM> implements IStatusView {

    protected PageHintView hintView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_hint;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected void initView(Bundle savedInstanceState) {
        hintView = findViewById(R.id.hintView);
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    mActivity.recreateFragment(BaseHintVMFragment.this.getClass().getName());
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
                }
            }
        });
        ViewStub baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                bindLayout(inflated);
            }
        });
        baseVS.inflate();
    }

    protected void bindLayout(View inflated) { }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            viewModel.loader.setPageStatus(PageStatus.NORMAL);
        } else {
            viewModel.loader.setPageStatus(PageStatus.NO_NET);
        }
        viewModel.loader.observePage(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                showStatusView(pageStatus);
            }
        });
    }

    @Override
    public void showStatusView(PageStatus status) {
        if (hintView != null && viewModel.loader.isFirstLoad()) {
            hintView.show(status);
        }
        if (status == PageStatus.NORMAL) {
            viewModel.loader.setFirstLoad(false);
        }
    }

}
