package cn.yue.base.middle.view.refresh;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

public class SwipeRefreshLayout extends androidx.swiperefreshlayout.widget.SwipeRefreshLayout implements IRefreshLayout{

    public int[] COLORS = {
            cn.yue.base.common.R.color.progress_color_1,
            cn.yue.base.common.R.color.progress_color_2,
            cn.yue.base.common.R.color.progress_color_3,
            cn.yue.base.common.R.color.progress_color_4
    };

    public SwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(COLORS);
    }

    @Override
    public void setTargetView(View targetView) {

    }

    @Override
    public void setEnabledRefresh(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void startRefresh() {
        setRefreshing(true);
    }

    @Override
    public void finishRefreshing() {
        if (isRefreshing()) {
            setRefreshing(false);
        }
    }

    @Override
    public void setOnRefreshListener(IRefreshLayout.OnRefreshListener onRefreshListener) {
        setOnRefreshListener(new androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
            }
        });
    }
}
