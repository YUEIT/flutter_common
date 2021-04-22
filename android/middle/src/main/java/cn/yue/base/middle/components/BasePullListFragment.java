package cn.yue.base.middle.components;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetSingleObserver;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullListFragment<P extends BaseListBean<S>, S> extends BaseFragment implements IStatusView, IWaitView, IBaseView, IPhotoView {

    private String pageNt = "1";
    private String lastNt = "1";
    protected List<S> dataList = new ArrayList<>();
    protected int total;    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    private CommonAdapter<S> adapter;
    private BasePullFooter footer;
    private PageStatus status = PageStatus.STATUS_NORMAL;
    private boolean isFirstLoading = true;
    private IRefreshLayout refreshL;
    private RecyclerView baseRV;
    protected PageHintView hintView;
    private PhotoHelper photoHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull_page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hintView = findViewById(R.id.hintView);
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    mActivity.recreateFragment(BasePullListFragment.this.getClass().getName());
                } else {
                    ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
                }
            }

            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (autoRefresh()) {
                        refreshWithLoading();
                    }
                } else {
                    showStatusView(PageStatus.STATUS_ERROR_NET);
                }
            }
        });

        refreshL = findViewById(R.id.refreshL);
        refreshL.init();
        refreshL.setEnabled(canPullDown());
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        if (canPullDown()) {
            hintView.setRefreshTarget((ViewGroup) refreshL);
        }
        footer = getFooter();
        if (footer != null) {
            footer.setOnReloadListener(new BasePullFooter.OnReloadListener() {
                @Override
                public void onReload() {
                    loadData();
                }
            });
        }
        baseRV = findViewById(R.id.baseRV);
        refreshL.setTargetView(baseRV);
        initRecyclerView(baseRV);
        baseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dataList.isEmpty()) {
                    return;
                }
                boolean isTheLast = false;
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    isTheLast = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= dataList.size() - 1;
                } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    isTheLast = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= dataList.size() - ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount() - 1;
                } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
                    int[] lastSpan = layoutManager.findLastVisibleItemPositions(null);
                    for (int position : lastSpan) {
                        if (position >= dataList.size() - layoutManager.getSpanCount() - 1) {
                            isTheLast = true;
                            break;
                        }
                    }
                }
                if (isTheLast && status == PageStatus.STATUS_SUCCESS) {
                    status = PageStatus.STATUS_LOADING_ADD;
                    footer.showStatusView(status);
                    loadData();
                }
            }
        });
    }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            if (autoRefresh()) {
                refreshWithLoading();
            }
        } else {
            showStatusView(PageStatus.STATUS_ERROR_NET);
        }
    }

    protected boolean autoRefresh() {
        return true;
    }

    protected boolean canPullDown() {
        return true;
    }

    protected void initRecyclerView(RecyclerView baseRV) {
        baseRV.setLayoutManager(getLayoutManager());
        baseRV.setAdapter(adapter = getAdapter());
        adapter.addFooterView(footer);
        footer.showStatusView(status);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected CommonAdapter<S> getAdapter() {
        if (adapter != null) {
            return adapter;
        }
        return new CommonAdapter<S>(mActivity, new ArrayList<S>()) {

            @Override
            protected int getViewType(int position) {
                return getItemType(position);
            }

            @Override
            public int getLayoutIdByType(int viewType) {
                return getItemLayoutId(viewType);
            }

            @Override
            public void bindData(CommonViewHolder<S> holder, int position, S s) {
                bindItemData(holder, position, s);
            }
        };
    }

    /**
     * 返回viewType，可选实现
     */
    protected int getItemType(int position) {
        return 0;
    }

    /**
     * 根据viewType 返回对应的layout
     */
    protected abstract int getItemLayoutId(int viewType);

    /**
     * 布局
     */
    protected abstract void bindItemData(CommonViewHolder<S> holder, int position, S s);

    protected BasePullFooter getFooter() {
        if (footer == null) {
            footer = new BasePullFooter(mActivity);
        }
        return footer;
    }

    /**
     * 刷新 loading动画
     */
    public void refreshWithLoading() {
        baseRV.setVisibility(View.GONE);
        showPageHintLoading();
        refresh(false);
    }

    /**
     * 刷新 swipe动画
     */
    public void refresh() {
        refresh(true);
    }

    /**
     * 刷新 选择是否有swipe动画
     * @param hasRefreshAnim
     */
    public void refresh(boolean hasRefreshAnim) {
        if (status == PageStatus.STATUS_LOADING_ADD || status == PageStatus.STATUS_LOADING_REFRESH) {
            return;
        }
        status = PageStatus.STATUS_LOADING_REFRESH;
        if (hasRefreshAnim) {
            refreshL.startRefresh();
        }
        pageNt = initPageNt();
        loadData();
    }

    protected String initPageNt() {
        return "1";
    }

    protected abstract Single<P> getRequestSingle(String nt);

    private void loadData() {
        if (getRequestSingle(pageNt) == null) {
            return;
        }
        getRequestSingle(pageNt)
//                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(this.<P>toBindLifecycle())
                .subscribe(new BaseNetSingleObserver<P>() {

                    private boolean isLoadingRefresh = false;
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (status == PageStatus.STATUS_LOADING_REFRESH) {
                            isLoadingRefresh = true;
                        } else {
                            isLoadingRefresh = false;
                        }
                    }

                    @Override
                    public void onSuccess(P p) {
                        refreshL.finishRefreshing();
                        if (isLoadingRefresh) {
                            dataList.clear();
                        }
                        if (isLoadingRefresh && p.getCurrentPageTotal() == 0) {
                            loadEmpty();
                        } else {
                            loadSuccess(p);
                            if (p.getCurrentPageTotal() < p.getPageSize()) {
                                loadNoMore();
                            } else if (p.getTotal() > 0 && p.getTotal() <= dataList.size()) {
                                loadNoMore();
                            } else if (p.getCurrentPageTotal() == 0) {
                                loadNoMore();
                            } else if (TextUtils.isEmpty(p.getPageNt()) && !initPageNt().matches("\\d+")) {
                                loadNoMore();
                            }
                        }
                        if (isLoadingRefresh) {
                            onRefreshComplete(p, null);
                        }
                    }

                    @Override
                    public void onException(ResultException e) {
                        refreshL.finishRefreshing();
                        loadFailed(e);
                        if (isLoadingRefresh) {
                            onRefreshComplete(null, e);
                        }
                    }

                    @Override
                    protected void onCancel(ResultException e) {
                        super.onCancel(e);
                        loadFailed(e);
                    }
                });
    }

    protected void loadSuccess(P p) {
        showStatusView(PageStatus.STATUS_SUCCESS);
        footer.showStatusView(status);
        if (TextUtils.isEmpty(p.getPageNt())) {
            try {
                if (p.getPageNo() == 0) {
                    pageNt = String.valueOf(Integer.valueOf(pageNt) + 1);
                } else {
                    pageNt = String.valueOf(p.getPageNo() + 1);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            pageNt = p.getPageNt();
        }
        if (p.getTotal() > 0) {
            total = p.getTotal();
        } else {
            total += p.getCurrentPageTotal();
        }
        lastNt = pageNt;
        dataList.addAll(p.getList() == null? new ArrayList<S>() : p.getList());
        notifyDataSetChanged();
    }

    protected void loadFailed(ResultException e) {
        pageNt = lastNt;
        if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
            if (this.status == PageStatus.STATUS_LOADING_REFRESH) {
                showStatusView(PageStatus.STATUS_ERROR_NET);
            } else if (this.status == PageStatus.STATUS_LOADING_ADD) {
                footer.showStatusView(status);
            }
        } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
            showStatusView(PageStatus.STATUS_ERROR_NO_DATA);
        } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
            showStatusView(PageStatus.STATUS_SUCCESS);
            footer.showStatusView(PageStatus.STATUS_SUCCESS);
        } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
            showStatusView(PageStatus.STATUS_ERROR_OPERATION);
            ToastUtils.showShortToast(e.getMessage());
        } else {
            showStatusView(PageStatus.STATUS_ERROR_SERVER);
            ToastUtils.showShortToast(e.getMessage());
        }
    }

    protected void loadNoMore() {
        showStatusView(PageStatus.STATUS_END);
    }

    protected void loadEmpty() {
        total = 0;
        dataList.clear();
        notifyDataSetChanged();
        if (showSuccessWithNoData()) {
            showStatusView(PageStatus.STATUS_SUCCESS);
            footer.showStatusView(PageStatus.STATUS_ERROR_NO_DATA);
        } else {
            showStatusView(PageStatus.STATUS_ERROR_NO_DATA);
        }
    }

    protected boolean showSuccessWithNoData() {
        return false;
    }

    protected void onRefreshComplete(P p, ResultException e) {

    }

    protected void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.setList(dataList);
        }
    }

    protected void notifyItemChangedReally() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showStatusView(PageStatus status) {
        this.status = status;
        switch (status) {
            case STATUS_LOADING_REFRESH:
                showPageHintLoading();
                break;
            case STATUS_SUCCESS:
                showPageHintSuccess();
                break;
            case STATUS_END:
                showPageHintSuccess();
                footer.showStatusView(status);
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
    }

    private void showPageHintLoading() {
        if (hintView != null) {
            hintView.showLoading();
        }
    }

    private void showPageHintSuccess() {
        if (baseRV != null) {
            baseRV.setVisibility(View.VISIBLE);
        }
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
