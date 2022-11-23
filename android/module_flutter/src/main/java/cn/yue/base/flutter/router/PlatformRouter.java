package cn.yue.base.flutter.router;

import android.content.Context;

import androidx.annotation.NonNull;

import cn.yue.base.flutter.module.IFlutterModule;
import cn.yue.base.flutter.module.manager.ModuleManager;

/**
 * Description : 跨平台路由 支持Flutter页面跳转
 * Created by yue on 2020/4/22
 */
public class PlatformRouter implements INavigation {

    private static class PlatformRouterHolder {
        private static PlatformRouter instance = new PlatformRouter();
    }

    public static PlatformRouter getInstance() {
        return PlatformRouterHolder.instance;
    }

    private INavigation navigation;
    private RouterCard routerCard;

    public synchronized RouterCard build(String pactUrl) {
        if (pactUrl.startsWith(IRouterPath.FLUTTER)) {
            navigation = ModuleManager.getModuleService(IFlutterModule.class).getFlutterRouter();
        } else if (pactUrl.startsWith(IRouterPath.NATIVE)) {
            navigation = FRouter.getInstance();
        }
        routerCard = new RouterCard(navigation);
        routerCard.setPactUrl(pactUrl);
        navigation.bindRouterCard(routerCard);
        return routerCard;
    }

    @Override
    public INavigation bindRouterCard(RouterCard routerCard) {
        this.routerCard = routerCard;
        this.routerCard.setNavigationImpl(this);
        return this;
    }

    @Override
    public void navigation(@NonNull Context context) {
        if (navigation != null) {
            navigation.navigation(context);
        }
    }

    @Override
    public void navigation(@NonNull Context context, int requestCode) {
        if (navigation != null) {
            navigation.navigation(context, requestCode);
        }
    }

    @Override
    public void navigation(@NonNull Context context, int requestCode, String toActivity) {
        if (navigation != null) {
            navigation.navigation(context, requestCode, toActivity);
        }
    }

}
