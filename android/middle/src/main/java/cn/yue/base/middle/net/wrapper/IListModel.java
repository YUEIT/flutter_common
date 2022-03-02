package cn.yue.base.middle.net.wrapper;

import java.util.List;

/**
 * Description :
 * Created by yue on 2022/1/26
 */

public interface IListModel<T> {

    List<T> getList();

    int getTotal();

    int getPageNo();

    int getPageSize();

    String getPageNt();

    int getCurrentPageTotal();
}
