package cn.yue.base.middle.mvvm.components;

import java.util.List;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.mvvm.PageViewModel;

public class BasePageVMFragment<VM extends PageViewModel<S>, S> extends BaseListVMFragment<VM, S> {

    @Override
    public CommonAdapter<S> initAdapter() {
        return null;
    }

    @Override
    void setData(List<S> list) {
        if (getAdapter() != null) {
            getAdapter().setList(list);
        }
    }

}
