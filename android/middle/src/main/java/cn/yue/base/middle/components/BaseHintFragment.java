package cn.yue.base.middle.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.view.PageHintView;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
public abstract class BaseHintFragment extends BaseFragment implements IStatusView, IWaitView, IBaseView , IPhotoView{

    private boolean isFirstLoading = true;
    protected PageStatus status = PageStatus.STATUS_NORMAL;
    protected PageHintView hintView;
    private ViewStub baseVS;
    private PhotoHelper photoHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_hint;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hintView = findViewById(R.id.hintView);
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    mActivity.recreateFragment(BaseHintFragment.this.getClass().getName());
                } else {
                    ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
                }
            }
        });
        baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                stubInflate(stub, inflated);
            }
        });
        baseVS.inflate();
    }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            showStatusView(status);
        } else {
            showStatusView(PageStatus.STATUS_ERROR_NET);
        }
    }

    protected abstract int getContentLayoutId();

    protected void stubInflate(ViewStub stub, View inflated) {}

    @Override
    public void showStatusView(PageStatus status) {
        switch (status) {
            case STATUS_NORMAL:
            case STATUS_SUCCESS:
                showPageHintSuccess();
                break;
            case STATUS_LOADING_REFRESH:
                showPageHintLoading();
                break;
            case STATUS_END:
                showPageHintSuccess();
                break;
            case STATUS_ERROR_NET:
                showPageHintErrorNet();
                break;
            case STATUS_ERROR_NO_DATA:
                showPageHintErrorNoData();
                break;
            case STATUS_ERROR_OPERATION:
                showPageHintErrorOperation();
                break;
            case STATUS_ERROR_SERVER:
                showPageHintErrorServer();
                break;
        }
        this.status = status;
    }

    private void showPageHintLoading() {
        if (hintView != null) {
            hintView.showLoading();
        }
    }

    private void showPageHintSuccess() {
        if (hintView != null) {
            hintView.showSuccess();
        }
        isFirstLoading = false;
    }

    private void showPageHintErrorNet() {
        if (hintView != null) {
            if (isFirstLoading) {
                hintView.showErrorNet();
            } else {
                ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
            }
        }
    }

    private void showPageHintErrorNoData() {
        if (hintView != null) {
            hintView.showErrorNoData();
        }
    }

    private void showPageHintErrorOperation() {
        if (hintView != null && isFirstLoading) {
            hintView.showErrorOperation();
        }
    }

    private void showPageHintErrorServer() {
        if (hintView != null && isFirstLoading) {
            hintView.showErrorOperation();
        }
    }

    private WaitDialog waitDialog;
    @Override
    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title, true, null);
    }

    @Override
    public void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
        }
    }

    public PhotoHelper getPhotoHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mActivity, this);
        }
        return photoHelper;
    }

    @Override
    public void selectImageResult(List<String> selectList) {

    }

    @Override
    public void cropImageResult(String image) {

    }

    @Override
    public void uploadImageResult(List<String> serverList) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (photoHelper != null) {
            photoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
