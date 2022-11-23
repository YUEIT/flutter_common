package cn.yue.base.flutter.router;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.exception.NoRouteFoundException;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.yue.base.flutter.activity.CommonActivity;

/**
 * Description : 路由
 * Created by yue on 2019/3/11
 */

public class FRouter implements INavigation, Parcelable {
    
    protected FRouter(Parcel in) {
        mRouterCard = in.readParcelable(RouterCard.class.getClassLoader());
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

    /**
     * 必须写在init之前，否则这些配置在init过程中将无效
     */
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
        dest.writeParcelable(mRouterCard, flags);
    }

    private static class FRouterHolder {
        private static FRouter instance = new FRouter();
    }

    public static FRouter getInstance() {
        return FRouterHolder.instance;
    }

    private RouterCard mRouterCard;
    
    public FRouter() {
        mRouterCard = new RouterCard(this);
    }

    public RouterCard getRouterCard() {
        return mRouterCard;
    }

    public RouterCard build(String path) {
        mRouterCard.setPath(path);
        return mRouterCard;
    }

    private Class targetActivity;

    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }

    public RouteType getRouteType() {
        if (TextUtils.isEmpty(mRouterCard.getPath())) {
            return RouteType.UNKNOWN;
        }
        Postcard postcard = ARouter.getInstance().build(mRouterCard.getPath());
        try {
            LogisticsCenter.completion(postcard);
        } catch (NoRouteFoundException e) {
            return RouteType.UNKNOWN;
        }
        return postcard.getType();
    }

    @Override
    public INavigation bindRouterCard(RouterCard mRouterCard) {
        this.mRouterCard = mRouterCard;
        this.mRouterCard.setNavigationImpl(this);
        return this;
    }

    @Override
    public void navigation(@NonNull Context context) {
        this.navigation(context, 0);
    }

    @Override
    public void navigation(@NonNull Context context, int requestCode) {
        navigation(context, requestCode, null);
    }

    @Override
    public void navigation(@NonNull Context context, int requestCode, String toActivity) {
        if (mRouterCard.isInterceptLogin() && interceptLogin(context)) {
            return;
        }
        if (getRouteType() == RouteType.ACTIVITY) {
            jumpToActivity(context, requestCode);
        } else if (getRouteType() == RouteType.FRAGMENT) {
            jumpToFragment(context, toActivity, requestCode);
        } else {
            Toast.makeText(context, "找不到页面~", Toast.LENGTH_LONG).show();
        }
    }
    
    private void jumpToActivity(Context context, int requestCode) {
        Postcard postcard = ARouter.getInstance()
                .build(mRouterCard.getPath())
                .withFlags(mRouterCard.getFlags())
                .with(mRouterCard.getExtras())
                .withTransition(mRouterCard.getRealEnterAnim(), mRouterCard.getRealExitAnim())
                .setTimeout(mRouterCard.getTimeout());
        if (requestCode <= 0 || !(context instanceof Activity)) {
            postcard.navigation(context);
        } else {
            postcard.navigation((Activity) context, requestCode);
        }
    }

    @SuppressLint("WrongConstant")
    private void jumpToFragment(Context context, String toActivity, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(RouterCard.TAG, mRouterCard);
        intent.putExtras(mRouterCard.getExtras());
        intent.setFlags(mRouterCard.getFlags());
        if (toActivity == null) {
            if (targetActivity == null) {
                intent.setClass(context, CommonActivity.class);
            } else {
                intent.setClass(context, targetActivity);
            }
        } else {
            intent.setClassName(context, toActivity);
        }
        if (requestCode <= 0) {
            context.startActivity(intent);
        } else {
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(mRouterCard.getRealEnterAnim(), mRouterCard.getRealExitAnim());
        }
    }

    public DialogFragment navigationDialogFragment(FragmentActivity context) {
        DialogFragment dialogFragment = (DialogFragment) ARouter.getInstance()
                .build(mRouterCard.getPath())
                .with(mRouterCard.getExtras())
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

