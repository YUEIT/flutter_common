package cn.yue.base.middle.view;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.yue.base.middle.router.FRouter;
import cn.yue.base.common.image.ImageLoader;
import cn.yue.base.middle.R;

/**
 * Description :
 * Created by yue on 2018/11/13
 */

public class PageHintView extends RelativeLayout{

    public PageHintView(Context context) {
        this(context, null);
    }

    public PageHintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setClickable(true);
        setDefault(context);
    }

    private View noNetView;
    private View noDataView;
    private View loadingView;
    private View serverErrorView;

    private void setDefault(Context context) {
        loadingView = inflate(context, R.layout.layout_page_hint_loading, null);
        noNetView = inflate(context, R.layout.layout_page_hint_no_net, null);
        noDataView = inflate(context, R.layout.layout_page_hint_no_data, null);
        serverErrorView = inflate(context, R.layout.layout_page_hint_server_error, null);
        ImageView loadingIV = loadingView.findViewById(R.id.loadingIV);
        ImageLoader.getLoader().loadGif(loadingIV, R.drawable.icon_page_loading);

        noNetView.findViewById(R.id.reloadTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReloadListener != null) {
                    onReloadListener.onReload();
                }
            }
        });
        noNetView.findViewById(R.id.checkNetTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance().build("/middle/noNet").navigation(context);
            }
        });

        serverErrorView.findViewById(R.id.reloadTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReloadListener != null) {
                    onReloadListener.onRefresh();
                }
            }
        });

        serverErrorView.findViewById(R.id.checkNetTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance().build("/middle/noNet").navigation(context);
            }
        });
    }

    private OnReloadListener onReloadListener;

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    public abstract static class OnReloadListener {
        public abstract void onReload();
        public void onRefresh(){}
    }

    public void setNoNetView(View noNetView) {
        if (noNetView != null) {
            this.noNetView = noNetView;
        }
    }

    public View setNoNetViewById(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setNoNetView(view);
        return view;
    }

    public void setNoDataView(View noDataView) {
        if (noDataView != null) {
            this.noDataView = noDataView;
        }
    }

    public View setNoDataViewById(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setNoDataView(view);
        return view;
    }

    public void setLoadingView(View loadingView) {
        if (noDataView != null) {
            this.loadingView = loadingView;
        }
    }

    public View setLoadingViewById(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setLoadingView(view);
        return view;
    }

    public void showLoading() {
        if (loadingView != null) {
            setVisibility(View.VISIBLE);
            removeAllViews();
            addView(loadingView);
            setRefreshEnable(false);
        }
    }

    public void showSuccess() {
        setVisibility(View.GONE);
        setRefreshEnable(true);
    }

    public void showErrorNet() {
        if (noNetView != null) {
            setVisibility(View.VISIBLE);
            removeAllViews();
            addView(noNetView);
            setRefreshEnable(false);
        }
    }

    public void showErrorNoData() {
        if (noDataView != null) {
            setVisibility(View.VISIBLE);
            removeAllViews();
            addView(noDataView);
            setRefreshEnable(true);
        }
    }

    public void showErrorOperation() {
        if (noNetView != null) {
            setVisibility(View.VISIBLE);
            removeAllViews();
            addView(serverErrorView);
            setRefreshEnable(false);
        }
    }

    private ViewGroup refreshLayout;
    public void setRefreshTarget(ViewGroup refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public void setRefreshEnable(boolean enable) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(enable);
        }
    }

    @Override
    public void addView(View child) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(child, params);
    }
}
