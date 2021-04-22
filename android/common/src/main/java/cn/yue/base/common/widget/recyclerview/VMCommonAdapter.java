package cn.yue.base.common.widget.recyclerview;

import android.content.Context;

import java.util.List;

/**
 * Description : 通用Adapter
 * Created by yue on 2016/12/16
 */

public class VMCommonAdapter<T> extends CommonAdapter<T> {

    public VMCommonAdapter(Context context) {
        super(context);
    }

    public VMCommonAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public int getLayoutIdByType(int viewType) {
        return 0;
    }

    @Override
    public void bindData(CommonViewHolder<T> holder, int position, T t) {

    }

}