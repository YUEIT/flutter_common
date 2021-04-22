package cn.yue.base.middle.view.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

public class OverRefreshLayout extends TwinklingRefreshLayout implements IRefreshLayout{

    public OverRefreshLayout(Context context) {
        super(context);
    }

    public OverRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        setEnableLoadmore(false);
        setHeaderView(new BezierLayout(getContext()));
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
            }
        });
    }
}
