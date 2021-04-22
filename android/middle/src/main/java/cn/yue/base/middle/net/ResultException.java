package cn.yue.base.middle.net;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class ResultException extends RuntimeException {

    private String code = "-1";
    private String message;

    public ResultException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}