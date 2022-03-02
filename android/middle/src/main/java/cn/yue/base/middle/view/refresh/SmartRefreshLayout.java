package cn.yue.base.middle.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


/**
 * Description :
 * Created by yue on 2019/6/11
 */

public class SmartRefreshLayout extends com.scwang.smart.refresh.layout.SmartRefreshLayout implements IRefreshLayout{

    public SmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRefreshHeader(new ClassicsHeader(context));
    }

    @Override
    public void setTargetView(View targetView) {

    }

    @Override
    public void setEnabledRefresh(boolean enabled) {
        super.setEnableRefresh(enabled);
    }

    @Override
    public void startRefresh() {
        autoRefresh();
    }

    @Override
    public void finishRefreshing() {
        super.finishRefresh();
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        super.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
            }
        });
    }
}
