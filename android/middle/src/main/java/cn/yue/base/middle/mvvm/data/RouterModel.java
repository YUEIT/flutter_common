package cn.yue.base.middle.mvvm.data;

import cn.yue.base.middle.router.RouterCard;

public class RouterModel {

    public RouterModel() {
    }

    public RouterModel(RouterCard routerCard, int requestCode, String toActivity) {
        this.routerCard = routerCard;
        this.requestCode = requestCode;
        this.toActivity = toActivity;
    }

    private RouterCard routerCard;
    private int requestCode;
    private String toActivity;

    public RouterCard getRouterCard() {
        return routerCard;
    }

    public void setRouterCard(RouterCard routerCard) {
        this.routerCard = routerCard;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getToActivity() {
        return toActivity;
    }

    public void setToActivity(String toActivity) {
        this.toActivity = toActivity;
    }
}
