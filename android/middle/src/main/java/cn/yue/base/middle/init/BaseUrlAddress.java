package cn.yue.base.middle.init;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public class BaseUrlAddress {

    private static String baseUrl;

    private static void debugModel() {
        baseUrl = "http://cn.yue.test";
    }

    private static void releaseModel() {
        baseUrl = "http://cn.yue.release";
    }

    /**************************************************************************************/

    public static String getBaseUrl() {
        return baseUrl;
    }

    /**************************************************************************************/

    public static void setDebug(boolean debug) {
        if (debug) {
            debugModel();
        } else {
            releaseModel();
        }
    }

}
