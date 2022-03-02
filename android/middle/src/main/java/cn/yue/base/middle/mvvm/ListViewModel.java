package cn.yue.base.middle.mvvm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvvm.data.MutableListLiveData;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetObserver;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class ListViewModel<P extends BaseListBean<S>, S> extends BaseViewModel{

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private String pageNt = "1";
    private String lastNt = "1";
    public int total = 0;    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    
    public MutableListLiveData<S> dataLiveData = new MutableListLiveData<S>();
    private ArrayList<S> dataList = new ArrayList<>();

    protected String initPageNt() {
        return "1";
    }

    protected int initPageSize() {
        return 20;
    }

    /**
     * 刷新
     */
    public void refresh() {
        refresh(loader.isFirstLoad());
    }

    /**
     * 刷新
     */
    public void refresh(boolean isPageRefreshAnim) {
        if (loader.getLoadStatus() == LoadStatus.LOADING
                || loader.getLoadStatus() == LoadStatus.REFRESH
                || loader.getPageStatus() == PageStatus.LOADING) {
            return;
        }
        if (isPageRefreshAnim) {
            loader.setPageStatus(PageStatus.LOADING);
        } else {
            loader.setLoadStatus(LoadStatus.REFRESH);
        }
        pageNt = initPageNt();
        loadData();
    }


    public void loadData() {
        doLoadData(pageNt);
    }

    protected abstract void doLoadData(String nt);

    public class PageTransformer implements SingleTransformer<P, P> {

        @NonNull
        @Override
        public SingleSource<P> apply(@NonNull Single<P> upstream) {
            BaseNetObserver<P> pageObserver = getPageObserver();
            return upstream
                    .compose(toBindLifecycle())
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

    protected BaseNetObserver<P> getPageObserver() {
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
                if (isLoadingRefresh) {
                    dataList.clear();
                }
                if (isLoadingRefresh && p.getCurrentPageTotal() == 0) {
                    loadEmpty();
                } else {
                    loadSuccess(p);
                    if (p.getCurrentPageTotal() < p.getPageSize()
                        || (p.getTotal() > 0 && p.getTotal() <= dataList.size())
                        || (p.getCurrentPageTotal() == 0)
                        || (TextUtils.isEmpty(p.getPageNt()) && !initPageNt().matches("\\d+"))
                        || (p.getCurrentPageTotal() < initPageSize())) {
                        loadNoMore();
                    }
                }
                if (isLoadingRefresh) {
                    onRefreshComplete(p, null);
                }
            }

            @Override
            public void onException(ResultException e) {
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
            loader.setPageStatus(PageStatus.NORMAL);
            loader.setLoadStatus(LoadStatus.NORMAL);
            if (TextUtils.isEmpty(p.getPageNt())) {
                try {
                    if (p.getPageNo() == 0) {
                        pageNt = String.valueOf(Integer.valueOf(pageNt + 1));
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
            dataLiveData.postValue(dataList);
        }

        protected void loadFailed(ResultException e) {
            pageNt = lastNt;
            if (loader.isFirstLoad()) {
                if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                    loader.setPageStatus(PageStatus.NO_NET);
                } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                    loader.setPageStatus(PageStatus.NO_DATA);
                } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                    loader.setPageStatus(PageStatus.NO_NET);
                    ToastUtils.showShort(e.getMessage());
                } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                    loader.setPageStatus(PageStatus.ERROR);
                    ToastUtils.showShort(e.getMessage());
                } else {
                    loader.setPageStatus(PageStatus.ERROR);
                    ToastUtils.showShort(e.getMessage());
                }
            } else {
                if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                    loader.setLoadStatus(LoadStatus.NO_NET);
                } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                    loader.setLoadStatus(LoadStatus.NO_DATA);
                } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                    loader.setLoadStatus(LoadStatus.NORMAL);
                    ToastUtils.showShort(e.getMessage());
                } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                    loader.setLoadStatus(LoadStatus.NORMAL);
                    ToastUtils.showShort(e.getMessage());
                } else {
                    loader.setLoadStatus(LoadStatus.NORMAL);
                    ToastUtils.showShort(e.getMessage());
                }
            }
        }

        protected void loadNoMore() {
            loader.setLoadStatus(LoadStatus.END);
        }

        protected void loadEmpty() {
            total = 0;
            dataList.clear();
            dataLiveData.postValue(dataList);
            if (showSuccessWithNoData()) {
                loader.setPageStatus(PageStatus.NORMAL);
                loader.setLoadStatus(LoadStatus.NO_DATA);
            } else {
                loader.setPageStatus(PageStatus.NO_DATA);
                loader.setLoadStatus(LoadStatus.NORMAL);
            }
        }

        protected void onRefreshComplete(P p, ResultException e) { }
    }

    protected boolean showSuccessWithNoData() {
        return false;
    }

    public void hasLoad(RecyclerView.LayoutManager layoutManager) {
        if (dataList.size() <= 0) {
            return;
        }
        boolean isTheLast = false;
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager lm = (GridLayoutManager)layoutManager;
            isTheLast = lm.findLastVisibleItemPosition() >= dataList.size() - lm.getSpanCount() - 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager)layoutManager;
            int[] lastSpan = lm.findLastVisibleItemPositions(null);
            for (int position : lastSpan) {
                if (position >= dataList.size() - lm.getSpanCount() - 1) {
                    isTheLast = true;
                    break;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            isTheLast = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition() >= dataList.size() - 1;
        }
        if (isTheLast && loader.getPageStatus() == PageStatus.NORMAL 
                && loader.getLoadStatus() == LoadStatus.NORMAL) {
            loader.setLoadStatus(LoadStatus.LOADING);
            loadData();
        }
    }
}
