package cn.yue.base.flutter.module;

/**
 * Description : 组件类型 , 初始化时根据值的大小排序进行初始化
 * Created by yue on 2020/4/4
 */

public @interface ModuleType {
    int MODULE_BASE = 0;
    int MODULE_APP = 1;
    int MODULE_FLUTTER = 2;
}
