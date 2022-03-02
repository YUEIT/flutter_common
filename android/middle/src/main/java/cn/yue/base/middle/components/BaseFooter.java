package cn.yue.base.middle.components;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.yue.base.common.widget.dialog.AppProgressBar;
import cn.yue.base.middle.R;
import cn.yue.base.middle.components.load.LoadStatus;

import static cn.yue.base.middle.components.load.LoadStatus.*;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public class BaseFooter extends RelativeLayout {

    private LoadStatus loadStatus;
    private OnReloadListener onReloadListener;
    private AppProgressBar progressBar;
    private LinearLayout loadingLL;
    private LinearLayout endLL;
    private LinearLayout errorLL;
    private LinearLayout emptyLL;
    public BaseFooter(Context context) {
        super(context);
        View.inflate(context, R.layout.layout_footer_base_pull, this);
        progressBar = findViewById(R.id.progress);
        loadingLL = findViewById(R.id.loadingLL);
        endLL = findViewById(R.id.endLL);
        errorLL = findViewById(R.id.errorLL);
        errorLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadStatus == NO_NET && onReloadListener != null) {
                    onReloadListener.onReload();
                }
            }
        });
        emptyLL = findViewById(R.id.emptyLL);
        showNormal();
    }

    public void showStatusView(LoadStatus loadStatus) {
        this.loadStatus = loadStatus;
        switch (loadStatus) {
            case NORMAL:
                showNormal();
                break;
            case LOADING:
                showLoading();
                break;
            case END:
                showEnd();
                break;
            case NO_DATA:
                showNoData();
                break;
            case NO_NET:
                showNoNet();
                break;
        }
    }

    private void showNormal() {
        loadingLL.setVisibility(GONE);
        endLL.setVisibility(GONE);
        errorLL.setVisibility(GONE);
        emptyLL.setVisibility(GONE);
        if (progressBar != null) {
            progressBar.stopAnimation();
        }
    }

    private void showLoading() {
        loadingLL.setVisibility(VISIBLE);
        endLL.setVisibility(GONE);
        errorLL.setVisibility(GONE);
        emptyLL.setVisibility(GONE);
        if (progressBar != null) {
            progressBar.setProgressBarBackgroundColor(Color.parseColor("#EFEFEF"));
            progressBar.startAnimation();
        }
    }

    private void showEnd() {
        loadingLL.setVisibility(GONE);
        endLL.setVisibility(VISIBLE);
        errorLL.setVisibility(GONE);
        emptyLL.setVisibility(GONE);
        if (progressBar != null) {
            progressBar.stopAnimation();
        }
    }

    private void showNoNet() {
        loadingLL.setVisibility(GONE);
        endLL.setVisibility(GONE);
        errorLL.setVisibility(VISIBLE);
        emptyLL.setVisibility(GONE);
        if (progressBar != null) {
            progressBar.stopAnimation();
        }
    }

    private void showNoData() {
        loadingLL.setVisibility(GONE);
        endLL.setVisibility(GONE);
        errorLL.setVisibility(GONE);
        emptyLL.setVisibility(VISIBLE);
        if (progressBar != null) {
            progressBar.stopAnimation();
        }
    }

    public View setFooterSuccess(int layoutId) {
        View view = View.inflate(getContext(), layoutId, null);
        loadingLL.removeAllViews();
        loadingLL.addView(view);
        return view;
    }

    public View setFooterEnd(int layoutId) {
        View view = View.inflate(getContext(), layoutId, null);
        endLL.removeAllViews();
        endLL.addView(view);
        return view;
    }

    public View setFooterEmpty(int layoutId) {
        View view = View.inflate(getContext(), layoutId, null);
        emptyLL.removeAllViews();
        emptyLL.addView(view);
        return view;
    }

    public View setFooterError(int layoutId) {
        View view = View.inflate(getContext(), layoutId, null);
        errorLL.removeAllViews();
        errorLL.addView(view);
        return view;
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    public interface OnReloadListener {
        void onReload();
    }
}
