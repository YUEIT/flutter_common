package cn.yue.base.middle.init;

import cn.yue.base.common.utils.device.DeviceUtils;
import cn.yue.base.common.utils.device.PhoneUtils;

/**
 * Description : 初始化
 * Created by yue on 2018/11/12
 */
public class InitConstant {

    /**------------------------------------------------------------------------------------------**/
    //version.properties文件中修改对应值（正式编译版本无需修改，自动设置false）

    public static boolean isDebug;

    public static boolean logMode;

    /**------------------------------------------------------------------------------------------**/

    public static boolean isLogin;

    public static String loginToken;

    public static String loginTokenSecret;

    public static String loginTokenForWallet; // 钱包的tk

    /**------------------------------------------------------------------------------------------**/

    private static double latitude = 0;     //纬度

    public static void setLatitude(double latitude) {
        InitConstant.latitude = latitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    private static double longitude = 0;    //经度

    public static void setLongitude(double longitude) {
        InitConstant.longitude = longitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    private static String versionName;

    public static void setVersionName(String versionName) {
        InitConstant.versionName = versionName;
    }

    public static String getVersionName() {
        return versionName;
    }

    private static String deviceId;

    public static String getDeviceId() {
        if (InitConstant.deviceId == null) {
            deviceId = PhoneUtils.getAndroidId();
        }
        return deviceId = DeviceUtils.getNullDeviceId(deviceId);
    }

    public final static String APP_CLIENT_TYPE = "2";

    public final static String APP_SIGN_KEY = "nK!op4w9lB.alev0";

    /**------------------------------------------------------------------------------------------**/

    public static final String WX_APP_ID = "wx00915cf45667d83a";
    public static final String WX_APP_SECRET = "97b811646e4ed103675c27448d1f081c";
    public static final String WX_USER_NAME = "gh_b57767e56326";

    private InitConstant() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

}
