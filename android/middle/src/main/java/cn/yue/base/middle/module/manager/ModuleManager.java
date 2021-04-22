package cn.yue.base.middle.module.manager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : 各Module通信方案
 * Created by yue on 2020/4/4
 */

public class ModuleManager<T extends IModuleService> {

    private static ModuleManager getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static ModuleManager instance = new ModuleManager();
    }

    // 类型  接口  实现
    private Map<Class, ModuleData<T>> serviceMap = new HashMap<>();

    private void registerModule(int priority, Class clazz, T service) {
        try {
            ModuleData<T> moduleData = new ModuleData<T>();
            moduleData.setPriority(priority);
            moduleData.setInterfaceClass(clazz);
            moduleData.setService(service);
            moduleData.setImpClass(service.getClass());
            serviceMap.put(clazz, moduleData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private T getModule(Class key) {
        ModuleData<T> moduleData = serviceMap.get(key);
        if (moduleData == null) {
            return null;
        }
        if (moduleData.getService() == null) {
            try {
                Class<T> clazz = moduleData.getImpClass();
                if (clazz != null) {
                    T service = clazz.newInstance();
                    moduleData.setService(service);
                    return service;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return moduleData.getService();
    }

    private void init(Context context) {
        List<Map.Entry<Class, ModuleData<T>>> list = new ArrayList<>(serviceMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Class, ModuleData<T>>>() {
            @Override
            public int compare(Map.Entry<Class, ModuleData<T>> o1, Map.Entry<Class, ModuleData<T>> o2) {
                int o1Index = o1.getValue().getPriority();
                int o2Index = o2.getValue().getPriority();
                if (o1Index < o2Index) {
                    return 1;
                } else if (o1Index > o2Index) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (Map.Entry<Class, ModuleData<T>> entry : list) {
            ModuleData<T> moduleData = entry.getValue();
            if (moduleData != null && moduleData.getService() != null) {
                moduleData.getService().init(context);
            }
        }
    }

    /**
     * 获取注册的module 接口实现 ，使用该方法必须先通过register注册
     * @param clazz
     * @return
     */
    public static <T extends IModuleService> T getModuleService(Class<T> clazz) {
        IModuleService service = getInstance().getModule(clazz);
        if (service != null) {
            if (clazz.isAssignableFrom(service.getClass())) {
                return (T) service;
            } else {
                throw new IllegalArgumentException("class is not extends IModuleService");
            }
        } else {
            throw new IllegalArgumentException("this ModuleService not register");
        }
    }

    /**
     * 注册
     * @param priority
     * @param service
     */
    public static <T extends IModuleService> void register(int priority, Class clazz, @NonNull T service) {
        getInstance().registerModule(priority, clazz, service);
    }

    /**
     * 初始化
     * @param context
     */
    public static void doInit(Context context) {
        getInstance().init(context);
    }

}
