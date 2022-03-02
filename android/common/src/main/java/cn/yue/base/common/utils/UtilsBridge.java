package cn.yue.base.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import cn.yue.base.common.utils.app.ActivityUtils;
import cn.yue.base.common.utils.app.AppUtils;
import cn.yue.base.common.utils.app.BarUtils;
import cn.yue.base.common.utils.app.IntentUtils;
import cn.yue.base.common.utils.app.PermissionUtils;
import cn.yue.base.common.utils.app.ServiceUtils;
import cn.yue.base.common.utils.app.UtilsActivityLifecycleImpl;
import cn.yue.base.common.utils.code.GsonUtils;
import cn.yue.base.common.utils.code.JsonUtils;
import cn.yue.base.common.utils.code.ShellUtils;
import cn.yue.base.common.utils.code.ThreadUtils;
import cn.yue.base.common.utils.constant.ConvertUtils;
import cn.yue.base.common.utils.constant.EncodeUtils;
import cn.yue.base.common.utils.constant.EncryptUtils;
import cn.yue.base.common.utils.debug.ThrowableUtils;
import cn.yue.base.common.utils.device.KeyboardUtils;
import cn.yue.base.common.utils.device.LanguageUtils;
import cn.yue.base.common.utils.device.ProcessUtils;
import cn.yue.base.common.utils.device.SDCardUtils;
import cn.yue.base.common.utils.display.AdaptScreenUtils;
import cn.yue.base.common.utils.code.SPUtils;
import cn.yue.base.common.utils.display.SizeUtils;
import cn.yue.base.common.utils.file.FileIOUtils;
import cn.yue.base.common.utils.file.FileUtils;
import cn.yue.base.common.utils.file.ImageUtils;
import cn.yue.base.common.utils.variable.StringUtils;
import cn.yue.base.common.utils.variable.TimeUtils;
import cn.yue.base.common.utils.variable.UriUtils;
import cn.yue.base.common.utils.view.ToastUtils;

import static android.Manifest.permission.CALL_PHONE;


/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2020/03/19
 *     desc  :
 * </pre>
 */
public class UtilsBridge {

    public static void init(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.init(app);
    }

    public static void unInit(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.unInit(app);
    }

    public static void preLoad() {
        preLoad(AdaptScreenUtils.getPreLoadRunnable());
    }

    ///////////////////////////////////////////////////////////////////////////
    // UtilsActivityLifecycleImpl
    ///////////////////////////////////////////////////////////////////////////
    public static Activity getTopActivity() {
        return UtilsActivityLifecycleImpl.INSTANCE.getTopActivity();
    }

    public static void addOnAppStatusChangedListener(final Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.addOnAppStatusChangedListener(listener);
    }

    public static void removeOnAppStatusChangedListener(final Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.removeOnAppStatusChangedListener(listener);
    }

    public static void addActivityLifecycleCallbacks(final Activity activity,
                                              final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(activity, callbacks);
    }

    public static void removeActivityLifecycleCallbacks(final Activity activity) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity);
    }

    public static void removeActivityLifecycleCallbacks(final Activity activity,
                                                 final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    public static List<Activity> getActivityList() {
        return UtilsActivityLifecycleImpl.INSTANCE.getActivityList();
    }

    public static Application getApplicationByReflect() {
        return UtilsActivityLifecycleImpl.INSTANCE.getApplicationByReflect();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ActivityUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isActivityAlive(final Activity activity) {
        return ActivityUtils.isActivityAlive(activity);
    }

    public static String getLauncherActivity() {
        return ActivityUtils.getLauncherActivity();
    }

    public static String getLauncherActivity(final String pkg) {
        return ActivityUtils.getLauncherActivity(pkg);
    }

    public static Activity getActivityByContext(Context context) {
        return ActivityUtils.getActivityByContext(context);
    }

    public static void startHomeActivity() {
        ActivityUtils.startHomeActivity();
    }

    public static void finishAllActivities() {
        ActivityUtils.finishAllActivities();
    }

    ///////////////////////////////////////////////////////////////////////////
    // AppUtils
    ///////////////////////////////////////////////////////////////////////////
    public static Context getTopActivityOrApp() {
        if (AppUtils.isAppForeground()) {
            Activity topActivity = getTopActivity();
            return topActivity == null ? Utils.getApp() : topActivity;
        } else {
            return Utils.getApp();
        }
    }

    public static boolean isAppRunning(@NonNull final String pkgName) {
        return AppUtils.isAppRunning(pkgName);
    }

    public static boolean isAppInstalled(final String pkgName) {
        return AppUtils.isAppInstalled(pkgName);
    }

    public static String getAppVersionName() {
        return AppUtils.getAppVersionName();
    }

    public static int getAppVersionCode() {
        return AppUtils.getAppVersionCode();
    }

    public static boolean isAppDebug() {
        return AppUtils.isAppDebug();
    }

    ///////////////////////////////////////////////////////////////////////////
    // BarUtils
    ///////////////////////////////////////////////////////////////////////////
    public static int getStatusBarHeight() {
        return BarUtils.getStatusBarHeight();
    }

    public static int getNavBarHeight() {
        return BarUtils.getNavBarHeight();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ConvertUtils
    ///////////////////////////////////////////////////////////////////////////
    public static String bytes2HexString(final byte[] bytes) {
        return ConvertUtils.bytes2HexString(bytes);
    }

    public static byte[] hexString2Bytes(String hexString) {
        return ConvertUtils.hexString2Bytes(hexString);
    }

    public static byte[] string2Bytes(final String string) {
        return ConvertUtils.string2Bytes(string);
    }

    public static String bytes2String(final byte[] bytes) {
        return ConvertUtils.bytes2String(bytes);
    }

    public static byte[] jsonObject2Bytes(final JSONObject jsonObject) {
        return ConvertUtils.jsonObject2Bytes(jsonObject);
    }

    public static JSONObject bytes2JSONObject(final byte[] bytes) {
        return ConvertUtils.bytes2JSONObject(bytes);
    }

    public static byte[] jsonArray2Bytes(final JSONArray jsonArray) {
        return ConvertUtils.jsonArray2Bytes(jsonArray);
    }

    public static JSONArray bytes2JSONArray(final byte[] bytes) {
        return ConvertUtils.bytes2JSONArray(bytes);
    }

    public static byte[] parcelable2Bytes(final Parcelable parcelable) {
        return ConvertUtils.parcelable2Bytes(parcelable);
    }

    public static <T> T bytes2Parcelable(final byte[] bytes,
                                  final Parcelable.Creator<T> creator) {
        return ConvertUtils.bytes2Parcelable(bytes, creator);
    }

    public static byte[] serializable2Bytes(final Serializable serializable) {
        return ConvertUtils.serializable2Bytes(serializable);
    }

    public static Object bytes2Object(final byte[] bytes) {
        return ConvertUtils.bytes2Object(bytes);
    }

    public static String byte2FitMemorySize(final long byteSize) {
        return ConvertUtils.byte2FitMemorySize(byteSize);
    }

    public static byte[] inputStream2Bytes(final InputStream is) {
        return ConvertUtils.inputStream2Bytes(is);
    }

    public static ByteArrayOutputStream input2OutputStream(final InputStream is) {
        return ConvertUtils.input2OutputStream(is);
    }

    public static List<String> inputStream2Lines(final InputStream is, final String charsetName) {
        return ConvertUtils.inputStream2Lines(is, charsetName);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EncodeUtils
    ///////////////////////////////////////////////////////////////////////////
    public static byte[] base64Encode(final byte[] input) {
        return EncodeUtils.base64Encode(input);
    }

    public static byte[] base64Decode(final byte[] input) {
        return EncodeUtils.base64Decode(input);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EncryptUtils
    ///////////////////////////////////////////////////////////////////////////
    public static byte[] hashTemplate(final byte[] data, final String algorithm) {
        return EncryptUtils.hashTemplate(data, algorithm);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FileIOUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean writeFileFromBytes(final File file,
                                      final byte[] bytes) {
        return FileIOUtils.writeFileFromBytesByChannel(file, bytes, true);
    }

    public static byte[] readFile2Bytes(final File file) {
        return FileIOUtils.readFile2BytesByChannel(file);
    }

    public static boolean writeFileFromString(final String filePath, final String content, final boolean append) {
        return FileIOUtils.writeFileFromString(filePath, content, append);
    }

    public static boolean writeFileFromIS(final String filePath, final InputStream is) {
        return FileIOUtils.writeFileFromIS(filePath, is);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FileUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isFileExists(final File file) {
        return FileUtils.isFileExists(file);
    }

    public static File getFileByPath(final String filePath) {
        return FileUtils.getFileByPath(filePath);
    }

    public static boolean deleteAllInDir(final File dir) {
        return FileUtils.deleteAllInDir(dir);
    }

    public static boolean createOrExistsFile(final File file) {
        return FileUtils.createOrExistsFile(file);
    }

    public static boolean createOrExistsDir(final File file) {
        return FileUtils.createOrExistsDir(file);
    }

    public static boolean createFileByDeleteOldFile(final File file) {
        return FileUtils.createFileByDeleteOldFile(file);
    }

    public static long getFsTotalSize(String path) {
        return FileUtils.getFsTotalSize(path);
    }

    public static long getFsAvailableSize(String path) {
        return FileUtils.getFsAvailableSize(path);
    }

    public static void notifySystemToScan(File file) {
        FileUtils.notifySystemToScan(file);
    }

    ///////////////////////////////////////////////////////////////////////////
    // GsonUtils
    ///////////////////////////////////////////////////////////////////////////
    public static String toJson(final Object object) {
        return GsonUtils.toJson(object);
    }

    public static <T> T fromJson(final String json, final Type type) {
        return GsonUtils.fromJson(json, type);
    }

    public static Gson getGson4LogUtils() {
        return GsonUtils.getGson4LogUtils();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ImageUtils
    ///////////////////////////////////////////////////////////////////////////
    public static byte[] bitmap2Bytes(final Bitmap bitmap) {
        return ImageUtils.bitmap2Bytes(bitmap);
    }

    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format, int quality) {
        return ImageUtils.bitmap2Bytes(bitmap, format, quality);
    }

    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return ImageUtils.bytes2Bitmap(bytes);
    }

    public static byte[] drawable2Bytes(final Drawable drawable) {
        return ImageUtils.drawable2Bytes(drawable);
    }

    public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format, int quality) {
        return ImageUtils.drawable2Bytes(drawable, format, quality);
    }

    public static Drawable bytes2Drawable(final byte[] bytes) {
        return ImageUtils.bytes2Drawable(bytes);
    }

    public static Bitmap view2Bitmap(final View view) {
        return ImageUtils.view2Bitmap(view);
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        return ImageUtils.drawable2Bitmap(drawable);
    }

    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return ImageUtils.bitmap2Drawable(bitmap);
    }

    ///////////////////////////////////////////////////////////////////////////
    // IntentUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isIntentAvailable(final Intent intent) {
        return IntentUtils.isIntentAvailable(intent);
    }

    public static Intent getLaunchAppIntent(final String pkgName) {
        return IntentUtils.getLaunchAppIntent(pkgName);
    }

    public static Intent getInstallAppIntent(final File file) {
        return IntentUtils.getInstallAppIntent(file);
    }

    public static Intent getInstallAppIntent(final Uri uri) {
        return IntentUtils.getInstallAppIntent(uri);
    }

    public static Intent getUninstallAppIntent(final String pkgName) {
        return IntentUtils.getUninstallAppIntent(pkgName);
    }

    public static Intent getDialIntent(final String phoneNumber) {
        return IntentUtils.getDialIntent(phoneNumber);
    }

    @RequiresPermission(CALL_PHONE)
    public static Intent getCallIntent(final String phoneNumber) {
        return IntentUtils.getCallIntent(phoneNumber);
    }

    public static Intent getSendSmsIntent(final String phoneNumber, final String content) {
        return IntentUtils.getSendSmsIntent(phoneNumber, content);
    }

    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName, final boolean isNewTask) {
        return IntentUtils.getLaunchAppDetailsSettingsIntent(pkgName, isNewTask);
    }


    ///////////////////////////////////////////////////////////////////////////
    // JsonUtils
    ///////////////////////////////////////////////////////////////////////////
    public static String formatJson(String json) {
        return JsonUtils.formatJson(json);
    }

    ///////////////////////////////////////////////////////////////////////////
    // KeyboardUtils
    ///////////////////////////////////////////////////////////////////////////
    public static void fixSoftInputLeaks(final Activity activity) {
        KeyboardUtils.fixSoftInputLeaks(activity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // LanguageUtils
    ///////////////////////////////////////////////////////////////////////////
    public static void applyLanguage(final Activity activity) {
        LanguageUtils.applyLanguage(activity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // PermissionUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isGranted(final String... permissions) {
        return PermissionUtils.isGranted(permissions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedDrawOverlays() {
        return PermissionUtils.isGrantedDrawOverlays();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ProcessUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isMainProcess() {
        return ProcessUtils.isMainProcess();
    }

    public static String getForegroundProcessName() {
        return ProcessUtils.getForegroundProcessName();
    }

    public static String getCurrentProcessName() {
        return ProcessUtils.getCurrentProcessName();
    }

    ///////////////////////////////////////////////////////////////////////////
    // SDCardUtils
    ///////////////////////////////////////////////////////////////////////////
    public static String getSDCardPathByEnvironment() {
        return SDCardUtils.getSDCardPathByEnvironment();
    }

    public static boolean isSDCardEnableByEnvironment() {
        return SDCardUtils.isSDCardEnableByEnvironment();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ServiceUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isServiceRunning(final String className) {
        return ServiceUtils.isServiceRunning(className);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ShellUtils
    ///////////////////////////////////////////////////////////////////////////
    public static ShellUtils.CommandResult execCmd(final String command, final boolean isRooted) {
        return ShellUtils.execCmd(command, isRooted);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SizeUtils
    ///////////////////////////////////////////////////////////////////////////
    public static int dp2px(final float dpValue) {
        return SizeUtils.dp2px(dpValue);
    }

    public static int px2dp(final float pxValue) {
        return SizeUtils.px2dp(pxValue);
    }

    public static int sp2px(final float spValue) {
        return SizeUtils.sp2px(spValue);
    }

    public static int px2sp(final float pxValue) {
        return SizeUtils.px2sp(pxValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SpUtils
    ///////////////////////////////////////////////////////////////////////////
    public static SPUtils getSpUtils4Utils() {
        return SPUtils.getInstance("Utils");
    }

    ///////////////////////////////////////////////////////////////////////////
    // StringUtils
    ///////////////////////////////////////////////////////////////////////////
    public static boolean isSpace(final String s) {
        return StringUtils.isSpace(s);
    }

    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        return StringUtils.equals(s1, s2);
    }


    ///////////////////////////////////////////////////////////////////////////
    // ThreadUtils
    ///////////////////////////////////////////////////////////////////////////
    public static <T> Utils.Task<T> doAsync(final Utils.Task<T> task) {
        ThreadUtils.getCachedPool().execute(task);
        return task;
    }

    public static void runOnUiThread(final Runnable runnable) {
        ThreadUtils.runOnUiThread(runnable);
    }

    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        ThreadUtils.runOnUiThreadDelayed(runnable, delayMillis);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ThrowableUtils
    ///////////////////////////////////////////////////////////////////////////
    public static String getFullStackTrace(Throwable throwable) {
        return ThrowableUtils.getFullStackTrace(throwable);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TimeUtils
    ///////////////////////////////////////////////////////////////////////////
    public static String millis2FitTimeSpan(long millis, int precision) {
        return TimeUtils.millis2FitTimeSpan(millis, precision);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ToastUtils
    ///////////////////////////////////////////////////////////////////////////
    public static void toastShowShort(final CharSequence text) {
        ToastUtils.showShort(text);
    }

    public static void toastCancel() {
        ToastUtils.cancel();
    }

    public static void preLoad(final Runnable... runs) {
        for (final Runnable r : runs) {
            ThreadUtils.getCachedPool().execute(r);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // UriUtils
    ///////////////////////////////////////////////////////////////////////////
    public static Uri file2Uri(final File file) {
        return UriUtils.file2Uri(file);
    }

    public static File uri2File(final Uri uri) {
        return UriUtils.uri2File(uri);
    }
}
