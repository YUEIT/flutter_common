package cn.yue.base.flutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.idlefish.flutterboost.containers.FlutterBoostActivity;
import java.util.HashMap;
import cn.yue.base.middle.router.INavigation;
import cn.yue.base.middle.router.RouterCard;
import io.flutter.embedding.android.FlutterActivityLaunchConfigs;

public class FlutterRouter implements INavigation {

    private static class FlutterRouterHolder {
        private static FlutterRouter instance = new FlutterRouter();
    }

    public static FlutterRouter getInstance() {
        return FlutterRouterHolder.instance;
    }

    private RouterCard routerCard;

    @Override
    public FlutterRouter bindRouterCard(RouterCard routerCard) {
        this.routerCard = routerCard;
        return this;
    }

    @Override
    public void navigation(Context context) {
        navigation((Activity)context,  0, null);
    }

    @Override
    public void navigation(@NonNull Context context, int requestCode) {
        navigation(context, requestCode, null);
    }

    @Override
    public void navigation(@NonNull Context context, int requestCode, String toActivity) {
        if (routerCard == null) {
            return;
        }
        Intent intent = new FlutterBoostActivity.CachedEngineIntentBuilder(FlutterBoostActivity.class)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque)
                .destroyEngineWithActivity(false)
                .uniqueId(getUniqueId())
                .url(routerCard.getPactUrl())
                .urlParams(getParams())
                .build(context);
        context.startActivity(intent);
    }

    private HashMap<String, Object> getParams() {
        HashMap<String, Object> map = new HashMap<>();
        for (String key : routerCard.getExtras().keySet()) {
            Object object = routerCard.getExtras().get(key);
            map.put(key, object.toString());
        }
        return map;
    }
    
    private String getUniqueId() {
        return routerCard.getExtras().getString("uniqueId");
    }
}