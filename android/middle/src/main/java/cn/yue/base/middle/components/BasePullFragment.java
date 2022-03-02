package cn.yue.base.middle.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.ContentView;

import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.R;
import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.Loader;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.IPullView;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullFragment extends BaseFragment implements IStatusView, IWaitView, IBaseView, IPhotoView, IPullView {

    protected Loader loader = new Loader();
    private IRefreshLayout refreshL;
    private PageHintView hintView;
    private View contentView;
    private PhotoHelper photoHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hintView = findViewById(R.id.hintView);
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    refresh();
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
                }
            }
        });
        refreshL = findViewById(R.id.refreshL);
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refreshL.setEnabledRefresh(canPullDown());
        if (canPullDown()) {
            hintView.setRefreshTarget(refreshL);
        }
        ViewStub baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                contentView = inflated;
                bindLayout(inflated);
            }
        });
        baseVS.inflate();
    }

    protected void bindLayout(View inflated) { }

    @Override
    protected void initOther() {
        if (NetworkUtils.isConnected()) {
            refresh();
        } else {
            showStatusView(loader.setPageStatus(PageStatus.NO_NET));
        }
    }

    protected abstract int getContentLayoutId();

    //回调继承 BasePullSingleObserver 以适应加载逻辑
    protected abstract void loadData();

    protected boolean canPullDown() {
        return true;
    }

    /**
     * 刷新 swipe动画
     */
    public void refresh() {
        refresh(loader.isFirstLoad());
    }

    /**
     * 刷新 选择是否页面加载动画
     */
    public void refresh(boolean isPageRefreshAnim) {
        if (loader.getPageStatus() == PageStatus.LOADING
                || loader.getLoadStatus() == LoadStatus.REFRESH) {
            return;
        }
        if (isPageRefreshAnim) {
            contentView.setVisibility(View.GONE);
            showStatusView(loader.setPageStatus(PageStatus.LOADING));
        } else {
            startRefresh();
        }
        loadData();
    }

    private void startRefresh() {
        loader.setLoadStatus(LoadStatus.REFRESH);
        refreshL.startRefresh();
    }

    @Override
    public void finishRefresh() {
        loader.setLoadStatus(LoadStatus.NORMAL);
        refreshL.finishRefreshing();
    }

    @Override
    public void loadComplete(PageStatus status) {
        showStatusView(loader.setPageStatus(status));
    }

    @Override
    public void showStatusView(PageStatus status) {
        if (hintView != null) {
            if (loader.isFirstLoad()) {
                hintView.show(status);
                if (loader.getPageStatus() == PageStatus.NORMAL) {
                    contentView.setVisibility(View.VISIBLE);
                } else {
                    contentView.setVisibility(View.GONE);
                }
            } else {
                hintView.show(PageStatus.NORMAL);
                contentView.setVisibility(View.VISIBLE);
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
