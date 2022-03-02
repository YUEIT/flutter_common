package cn.yue.base.middle.components;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.R;
import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.Loader;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetObserver;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.middle.net.wrapper.IListModel;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BaseListFragment<P extends IListModel<S>, S> extends BaseFragment
        implements IBaseView, IPhotoView {

    private String pageNt = "1";
    private String lastNt = "1";
    protected List<S> dataList = new ArrayList<>();
    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    protected int total;
    private CommonAdapter<S> adapter;
    private BaseFooter footer;
    private Loader loader = new Loader();
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
                    if (autoRefresh()) {
                        refresh();
                    }
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
                }
            }

        });

        refreshL = findViewById(R.id.refreshL);
        refreshL.setEnabledRefresh(canPullDown());
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        if (canPullDown()) {
            hintView.setRefreshTarget(refreshL);
        }
        footer = getFooter();
        if (footer != null) {
            footer.setOnReloadListener(new BaseFooter.OnReloadListener() {
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
                if (isTheLast
                        && loader.getPageStatus() == PageStatus.NORMAL
                        && loader.getLoadStatus() == LoadStatus.NORMAL) {
                    footer.showStatusView(loader.setLoadStatus(LoadStatus.LOADING));
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
                refresh();
            }
        } else {
            showStatusView(loader.setPageStatus(PageStatus.NO_NET));
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
        baseRV.setAdapter(adapter = initAdapter());
        adapter.addFooterView(footer);
        footer.showStatusView(loader.setLoadStatus(LoadStatus.NORMAL));
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected  CommonAdapter<S> initAdapter() {
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

    protected CommonAdapter<S> getAdapter() {
        return adapter;
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

    protected BaseFooter getFooter() {
        if (footer == null) {
            footer = new BaseFooter(mActivity);
        }
        return footer;
    }

    /**
     * 刷新
     */
    public void refresh() {
        refresh(loader.isFirstLoad());
    }

    /**
     * 刷新 选择是否页面加载动画
     */
    public void refresh(boolean isPageRefreshAnim) {
        if (loader.getPageStatus() == PageStatus.LOADING
                || loader.getLoadStatus() == LoadStatus.LOADING
                || loader.getLoadStatus() == LoadStatus.REFRESH) {
            return;
        }
        if (isPageRefreshAnim) {
            showStatusView(loader.setPageStatus(PageStatus.LOADING));
        } else {
            loader.setLoadStatus(LoadStatus.REFRESH);
            refreshL.startRefresh();
        }
        pageNt = initPageNt();
        loadData();
    }

    protected String  initPageNt() {
        return "1";
    }

    protected int initPageSize() {
        return 20;
    }

    private void loadData() {
        doLoadData(pageNt);
    }

    protected abstract void doLoadData(String nt);

    public class PageTransformer implements SingleTransformer<P, P> {

        @NonNull
        @Override
        public SingleSource<P> apply(@NonNull Single<P> upstream) {
            BaseNetObserver<P> pageObserver = getPageObserver();
            return upstream
                    .compose(getLifecycleProvider().toBindLifecycle())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            pageObserver.onStart();
                        }
                    })
                    .doOnSuccess(new Consumer<P>() {
                        @Override
                        public void accept(P p) throws Exception {
                            pageObserver.onSuccess(p);
                        }
                    })
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            pageObserver.onError(throwable);
                        }
                    });
        }
    }

    public class PageDelegateObserver extends BaseNetObserver<P> {

        private BaseNetObserver<P> observer;
        private BaseNetObserver<P> pageObserver = getPageObserver();

        PageDelegateObserver(BaseNetObserver<P> observer) {
            this.observer = observer;
        }

        @Override
        public void onStart() {
            super.onStart();
            pageObserver.onStart();
            if (observer != null) {
                observer.onStart();
            }
        }

        @Override
        public void onSuccess(@NonNull P p) {
            pageObserver.onSuccess(p);
            if (observer != null) {
                observer.onSuccess(p);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            pageObserver.onError(e);
            if (observer != null) {
                observer.onError(e);
            }
        }

        @Override
        protected void onCancel(ResultException e) {
            super.onCancel(e);
        }

        @Override
        public void onException(ResultException e) { }
    }

    public BaseNetObserver<P> getPageObserver() {
        return new PageObserver();
    }

    public class PageObserver extends BaseNetObserver<P> {
        private boolean isLoadingRefresh = false;
        @Override
        public void onStart() {
            super.onStart();
            if (loader.getPageStatus() == PageStatus.LOADING
                    || loader.getLoadStatus() == LoadStatus.REFRESH) {
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
                if (p.getCurrentPageTotal() < p.getPageSize()
                        || (p.getTotal() > 0 && p.getTotal() <= dataList.size())
                        || p.getCurrentPageTotal() == 0
                        || (TextUtils.isEmpty(p.getPageNt()) && !initPageNt().matches("\\d+"))
                        || p.getCurrentPageTotal() < initPageSize()) {
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

        protected void loadSuccess(P p) {
            showStatusView(loader.setPageStatus(PageStatus.NORMAL));
            footer.showStatusView(loader.setLoadStatus(LoadStatus.NORMAL));
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
            if (loader.isFirstLoad()) {
                if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                    showStatusView(loader.setPageStatus(PageStatus.NO_NET));
                } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                    showStatusView(loader.setPageStatus(PageStatus.NO_DATA));
                } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                    showStatusView(loader.setPageStatus(PageStatus.ERROR));
                } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                    showStatusView(loader.setPageStatus(PageStatus.ERROR));
                    ToastUtils.showShort(e.getMessage());
                } else {
                    showStatusView(loader.setPageStatus(PageStatus.ERROR));
                    ToastUtils.showShort(e.getMessage());
                }
            } else {
                if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                    footer.showStatusView(loader.setLoadStatus(LoadStatus.NO_NET));
                } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                    footer.showStatusView(loader.setLoadStatus(LoadStatus.NORMAL));
                } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                    footer.showStatusView(loader.setLoadStatus(LoadStatus.NORMAL));
                } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                    footer.showStatusView(loader.setLoadStatus(LoadStatus.NORMAL));
                    ToastUtils.showShort(e.getMessage());
                } else {
                    footer.showStatusView(loader.setLoadStatus(LoadStatus.NORMAL));
                    ToastUtils.showShort(e.getMessage());
                }
            }
        }

        protected void loadNoMore() {
            footer.showStatusView(loader.setLoadStatus(LoadStatus.END));
        }

        protected void loadEmpty() {
            total = 0;
            dataList.clear();
            notifyDataSetChanged();
            if (showSuccessWithNoData()) {
                showStatusView(loader.setPageStatus(PageStatus.NORMAL));
                footer.showStatusView(loader.setLoadStatus(LoadStatus.NO_DATA));
            } else {
                showStatusView(loader.setPageStatus(PageStatus.NO_DATA));
            }
        }

        protected void onRefreshComplete(P p, ResultException e) { }
    }

    protected boolean showSuccessWithNoData() {
        return false;
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
        if (hintView != null) {
            if (loader.isFirstLoad()) {
                hintView.show(status);
                if (loader.getPageStatus() == PageStatus.NORMAL) {
                    baseRV.setVisibility(View.VISIBLE);
                } else {
                    baseRV.setVisibility(View.GONE);
                }
            } else {
                hintView.show(PageStatus.NORMAL);
                baseRV.setVisibility(View.VISIBLE);
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
