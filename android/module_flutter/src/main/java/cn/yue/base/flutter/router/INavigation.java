package cn.yue.base.flutter.router;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Description : 路由跳转
 * Created by yue on 2020/4/22
 */
public interface INavigation {

    INavigation bindRouterCard(RouterCard routerCard);

    void navigation(@NonNull Context context);

    void navigation(@NonNull Context context, int requestCode);

    void navigation(@NonNull Context context, int requestCode, String toActivity);
}
