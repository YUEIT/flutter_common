package cn.yue.base.middle.init;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public class BaseUrlAddress {

    private static String UP_LOAD_URL;

    private static void debugModel() {
        UP_LOAD_URL = "http://imgsvc.imcoming.com";
    }

    private static void releaseModel() {
        UP_LOAD_URL = "https://imgsvc.anlaiye.com.cn";
    }

    /**************************************************************************************/

    public static String getUpLoadUrl() {
        return UP_LOAD_URL;
    }

    /**************************************************************************************/

    public static void setDebug(boolean debug) {
        if (debug) {
            debugModel();
        } else {
            releaseModel();
        }
    }

    //开发环境，着急调试的话加上，一般不用
    private static void developModel() {
        UP_LOAD_URL = "https://imgsvc.anlaiye.com.cn";
    }

}
