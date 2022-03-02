package cn.yue.base.middle.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.components.load.Loader;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.view.PageHintView;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
public abstract class BaseHintFragment extends BaseFragment implements IStatusView, IWaitView, IBaseView , IPhotoView{

    protected Loader loader = new Loader();
    protected PageHintView hintView;
    private ViewStub baseVS;
    private PhotoHelper photoHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_hint;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        loader.setPageStatus(PageStatus.NORMAL);
        hintView = findViewById(R.id.hintView);
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    showStatusView(loader.setPageStatus(PageStatus.NORMAL));
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
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
            showStatusView(loader.setPageStatus(PageStatus.NORMAL));
        } else {
            showStatusView(loader.setPageStatus(PageStatus.NO_NET));
        }
    }

    protected abstract int getContentLayoutId();

    protected void stubInflate(ViewStub stub, View inflated) {}

    @Override
    public void showStatusView(PageStatus status) {
        if (hintView != null) {
            if (loader.isFirstLoad()) {
                hintView.show(status);
            } else {
                hintView.show(PageStatus.NORMAL);
            }
        }
       if (status == PageStatus.NORMAL) {
           loader.setFirstLoad(false);
       }
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
