package cn.yue.base.common.activity;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public interface PermissionCallBack {
    void requestSuccess(String permission);
    void requestFailed(String permission);
}
