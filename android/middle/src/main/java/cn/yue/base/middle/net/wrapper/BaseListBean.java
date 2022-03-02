package cn.yue.base.middle.net.wrapper;

import java.util.List;

/**
 * 分页类型
 * Created by yue on 2018/7/11.
 */

public class BaseListBean<T> extends BaseUnityListBean<T> implements IListModel<T> {

    @Override
    public List<T> getList() {
        return super.getRealList();
    }

    //并不一定返回
    @Override
    public int getTotal() {
        return super.getRealTotal();
    }

    @Override
    public int getPageNo() {
        return super.getRealPageNo();
    }

    @Override
    public int getPageSize() {
        return super.getRealPageSize();
    }

    @Override
    public String getPageNt() {
        return super.getRealPageNt();
    }

    @Override
    public int getCurrentPageTotal() {
        return getList()==null?0:getList().size();
    }

}

