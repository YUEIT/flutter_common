package cn.yue.base.middle.mvp;

/**
 * Description :
 * Created by yue on 2018/11/13
 */
public enum PageStatus {
    STATUS_NORMAL,
    STATUS_LOADING_ADD, //分页，添加
    STATUS_LOADING_REFRESH, //刷新
    STATUS_SUCCESS,
    STATUS_END,
    STATUS_ERROR_OPERATION, //操作异常
    STATUS_ERROR_NET,   //网络连接问题
    STATUS_ERROR_NO_DATA,   //空数据
    STATUS_ERROR_SERVER     //服务器访问错误
}
