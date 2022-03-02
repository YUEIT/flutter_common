package cn.yue.base.middle.net.wrapper;


import java.util.List;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class BaseUnityListBean<T> {
    private List<T> list;       // 瀑布式
    private List<T> page_list;  //分页式
    private List<T> dataList;
    private int total;
    private int pageCount;
    private int pageSize;
    private int pageNo;
    private int count;//
    private String nt;	//版本Id，用作下一页版本号，null表示没有下一页
    private String pt;	//版本Id，用作上一页版本号

    protected List<T> getRealList() {
        if (list != null){
            return list;
        }else if (dataList != null){
            return dataList;
        }else if (page_list != null){
            return page_list;
        }
        return null;
    }

    protected int getRealPageSize() {
        List<T> realList = getRealList();
        if (pageSize > 0) {
            return pageSize;
        } else if (realList != null && realList.size() > 0) {
            return realList.size();
        }
        return 0;
    }

    protected int getRealTotal() {
        return count == 0 ? total : count;
    }

    protected int getRealPageNo() {
        return pageNo;
    }

    protected String getRealPageNt() {
        return nt;
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNt() {
        return nt;
    }

    public void setNt(String nt) {
        this.nt = nt;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }
}
