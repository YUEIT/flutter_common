package cn.yue.base.flutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.containers.FlutterBoostActivity;

import java.util.HashMap;
import java.util.Map;

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
    public void bindRouterCard(RouterCard routerCard) {
        this.routerCard = routerCard;
    }

    @Override
    public void navigation(Context context) {
        navigation((Activity)context, null, 0);
    }

    @Override
    public void navigation(@NonNull Context context, Class toActivity) {
        navigation((Activity) context, toActivity, 0);
    }

    @Override
    public void navigation(Activity context, int requestCode) {
        navigation(context, null, requestCode);
    }

    @Override
    public void navigation(@NonNull Activity context, Class toActivity, int requestCode) {
        if (routerCard == null) {
            return;
        }
        Intent intent = new FlutterBoostActivity.CachedEngineIntentBuilder(FlutterBoostActivity.class, FlutterBoost.ENGINE_ID)
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