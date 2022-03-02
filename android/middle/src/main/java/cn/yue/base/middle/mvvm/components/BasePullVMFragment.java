package cn.yue.base.middle.mvvm.components;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.lifecycle.Observer;

import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.R;
import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvvm.PullViewModel;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullVMFragment<VM extends PullViewModel> extends BaseVMFragment<VM> implements IStatusView {

    protected IRefreshLayout refreshL;
    protected PageHintView hintView;
    private View contentView;

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
                    viewModel.refresh();
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
                }
            }
        });
        refreshL = findViewById(R.id.refreshL);
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
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
        super.initOther();
        if (NetworkUtils.isConnected()) {
            viewModel.refresh();
        } else {
            viewModel.loader.setPageStatus(PageStatus.NO_NET);
        }
        viewModel.loader.observePage(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                showStatusView(pageStatus);
            }
        });
        viewModel.loader.observeLoad(this, new Observer<LoadStatus>() {
            @Override
            public void onChanged(LoadStatus loadStatus) {
                if (loadStatus == LoadStatus.REFRESH) {
                    refreshL.startRefresh();
                } else {
                    refreshL.finishRefreshing();
                }
            }
        });
    }

    protected abstract int getContentLayoutId();

    protected boolean canPullDown() {
        return true;
    }

    @Override
    public void showStatusView(PageStatus status) {
        if (hintView != null) {
            if (viewModel.loader.isFirstLoad()) {
                hintView.show(status);
                if (status == PageStatus.NORMAL) {
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
            viewModel.loader.setFirstLoad(false);
        }
    }

}
