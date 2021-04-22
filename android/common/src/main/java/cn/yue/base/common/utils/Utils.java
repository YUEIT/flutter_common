package cn.yue.base.common.utils;

import android.content.Context;


/**
 * Utils 包括以下几个部分
 *  app: 应用相关
 *       ActivityUtils
 *       AppUtils
 *       BarUtils
 *       FragmentUtils
 *       IntentUtils
 *
 *  code: 代码相关
 *       HandlerUtils
 *       ProcessUtils 进程工具
 *       ServiceUtils   服务工具
 *       ShellUtils  root工具
 *       SPUtils    sharedPreference工具
 *       ThreadPoolUtils    线程池工具
 *
 *  Constant:
 *       ConstantUtils  常量工具
 *       ConvertUtils   基本类型转换工具
 *       EncodeUtils    编码解码工具
 *       EncryptUtils   加密解密工具
 *       ImageUtils     图片类型转换
 *       LunarUtils     阴历相关
 *       PinyinUtils    拼音
 *       RegexUtils     正则
 *       SpannableStringUtils   SpannableString
 *       StringUtils
 *       TimeUtils  时间格式
 *
 *  debug
 *      CloseUtils
 *      CrashUtils
 *      EmptyUtils
 *      LogUtils
 *      ToastUtils
 *
 *  device:
 *      *CameraUtils
 *      ClipboardUtils  粘贴板
 *      DeviceUtils
 *      KeyboardUtils   软键盘
 *      LocationUtils
 *      PhoneUtils
 *      ScreenUtils
 *      *VibrationUtils
 *
 *  file
 *     CleanUtils   清除内存缓存
 *     FileUtils
 *     SDCardUtils
 *     ZipUtils
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

}