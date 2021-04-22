package cn.yue.base.middle.net.wrapper;

import android.text.TextUtils;

import cn.yue.base.middle.net.NetworkConfig;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class BaseBean<T> {

    protected String msg;
    protected String message;
    protected String flag;//错误码
    protected String status;//错误码
    protected T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRealMessage(){
        if(!TextUtils.isEmpty(msg)){
            return msg;
        }else if(!TextUtils.isEmpty(message)){
            return message;
        }
        return "";
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRealCode() {
        if (NetworkConfig.SUCCESS_FLAG.equals(flag)) {
            return NetworkConfig.SUCCESS_FLAG;
        } else {
            if (NetworkConfig.ERROR_SERVER.equals(flag) && !("0".equals(status) || "1".equals(status))) {
                return status;
            } else {
                return flag;
            }
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "msg='" + msg + '\'' +
                ", message='" + message + '\'' +
                ", flag='" + flag + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
