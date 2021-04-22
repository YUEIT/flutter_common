package cn.yue.base.middle.net.wrapper;

import java.util.List;

/**
 * 分页类型
 * Created by yue on 2018/7/11.
 */

public class BaseListBean<T> extends BaseUnityListBean<T> {

    public List<T> getList() {
        return super.getRealList();
    }

    //并不一定返回
    public int getTotal() {
        return super.getRealTotal();
    }

    public int getPageNo() {
        return super.getRealPageNo();
    }

    public int getPageSize() {
        return super.getRealPageSize();
    }

    public String getPageNt() {
        return super.getNt();
    }

    public int getCurrentPageTotal() {
        return getList()==null?0:getList().size();
    }

}

