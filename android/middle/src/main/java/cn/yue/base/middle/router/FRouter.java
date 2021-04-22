package cn.yue.base.middle.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.exception.NoRouteFoundException;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.middle.activity.CommonActivity;

/**
 * Description : 路由
 * Created by yue on 2019/3/11
 */

public class FRouter implements INavigation, Parcelable {

    public static final String TAG = "FRouter";

    protected FRouter(Parcel in) {
        routerCard = in.readParcelable(RouterCard.class.getClassLoader());
    }

    public static final Creator<FRouter> CREATOR = new Creator<FRouter>() {
        @Override
        public FRouter createFromParcel(Parcel in) {
            return new FRouter(in);
        }

        @Override
        public FRouter[] newArray(int size) {
            return new FRouter[size];
        }
    };

    public static void init(Application application) {
        debug();
        ARouter.init(application);
    }

    //必须写在init之前，否则这些配置在init过程中将无效
    public static void debug() {
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(routerCard, flags);
    }

    private static class FRouterHolder {
        private static FRouter instance = new FRouter();
    }

    public static FRouter getInstance() {
        FRouter fRouter = FRouterHolder.instance;
        fRouter.routerCard.clear();
        return fRouter;
    }

    private RouterCard routerCard;
    public FRouter() {
        routerCard = new RouterCard(this);
    }

    public RouterCard getRouterCard() {
        return routerCard;
    }

    public RouterCard build(String path) {
        routerCard.setPath(path);
        return routerCard;
    }

    private Class targetActivity;

    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }

    public RouteType getRouteType() {
        if (TextUtils.isEmpty(routerCard.getPath())) {
            throw new NullPointerException("path is null");
        }
        Postcard postcard = ARouter.getInstance().build(routerCard.getPath());
        try {
            LogisticsCenter.completion(postcard);
        } catch (NoRouteFoundException e) {
            return RouteType.UNKNOWN;
        }
        return postcard.getType();
    }

    @Override
    public void bindRouterCard(RouterCard routerCard) {
        this.routerCard = routerCard;
    }

    @Override
    public void navigation(Context context) {
        this.navigation(context, null);
    }

    @Override
    public void navigation(@NonNull Context context, Class toActivity) {
        if (routerCard.isInterceptLogin() && interceptLogin(context)) {
            return;
        }
        if (getRouteType() == RouteType.ACTIVITY) {
            jumpToActivity(context);
        } else if (getRouteType() == RouteType.FRAGMENT) {
            jumpToFragment(context, toActivity);
        } else {
            ToastUtils.showShortToast("找不到页面~");
        }
    }

    @Override
    public void navigation(Activity context, int requestCode) {
        this.navigation(context, null, requestCode);
    }

    @Override
    public void navigation(@NonNull Activity context, Class toActivity, int requestCode) {
        if (routerCard.isInterceptLogin() && interceptLogin(context)) {
            return;
        }
        if (getRouteType() == RouteType.ACTIVITY) {
            jumpToActivity(context, requestCode);
        } else if (getRouteType() == RouteType.FRAGMENT) {
            jumpToFragment(context, toActivity, requestCode);
        } else {
            ToastUtils.showShortToast("找不到页面~");
        }
    }


    private void jumpToActivity(Context context) {
        jumpToActivity(context, 0);
    }

    private void jumpToActivity(Context context, int requestCode) {
        Postcard postcard = ARouter.getInstance()
                .build(routerCard.getPath())
                .withFlags(routerCard.getFlags())
                .with(routerCard.getExtras())
                .withTransition(routerCard.getRealEnterAnim(), routerCard.getRealExitAnim())
                .setTimeout(routerCard.getTimeout());
        if (requestCode == 0 || !(context instanceof Activity)) {
            postcard.navigation(context);
        } else {
            postcard.navigation((Activity) context, requestCode);
        }
    }

    private void jumpToFragment(Context context) {
        jumpToFragment(context, null);
    }

    private void jumpToFragment(Context context, Class toActivity) {
        jumpToFragment(context, toActivity, 0);
    }

    private void jumpToFragment(Context context, Class toActivity, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(TAG, this);
        intent.putExtras(routerCard.getExtras());
        intent.setFlags(routerCard.getFlags());
        if (toActivity == null) {
            if (targetActivity == null) {
                intent.setClass(context, CommonActivity.class);
            } else {
                intent.setClass(context, targetActivity);
            }
        } else {
            intent.setClass(context, toActivity);
        }
        if (requestCode == 0) {
            context.startActivity(intent);
        } else {
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(routerCard.getRealEnterAnim(), routerCard.getRealExitAnim());
        }
    }

    public DialogFragment navigationDialogFragment(BaseFragmentActivity context) {
        DialogFragment dialogFragment = (DialogFragment) ARouter.getInstance()
                .build(routerCard.getPath())
                .with(routerCard.getExtras())
                .navigation(context);
        dialogFragment.show(context.getSupportFragmentManager(), null);
        return dialogFragment;
    }


    public interface OnInterceptLoginListener {
        boolean interceptLogin(Context context);
    }

    private OnInterceptLoginListener onInterceptLoginListener;

    public void setOnInterceptLoginListener(OnInterceptLoginListener onInterceptLoginListener) {
        this.onInterceptLoginListener = onInterceptLoginListener;
    }

    private boolean interceptLogin(Context context) {
        return onInterceptLoginListener.interceptLogin(context);
    }

}

