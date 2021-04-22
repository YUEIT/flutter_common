package cn.yue.base.middle.view.refresh;

import android.view.View;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

public interface IRefreshLayout {

    void init();

    void setTargetView(View targetView);

    void setEnabled(boolean enabled);

    void startRefresh();

    void finishRefreshing();

    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    interface OnRefreshListener {
        void onRefresh();
    }
}
