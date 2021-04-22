package cn.yue.base.common.utils.code;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description : 类型转换工具
 * Created by yue on 2019/3/11
 */
public class TypeUtils {

    private TypeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Type $List(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, List.class, type);
    }

    public static Type $Set(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Set.class, type);
    }

    public static Type $HashMap(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, HashMap.class, type, type2);
    }

    public static Type $Map(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, type, type2);
    }

    public static Type $Parameterized(Type ownerType, Type rawType, Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);
    }

    public static Type $Array(Type type) {
        return $Gson$Types.arrayOf(type);
    }

    public static Type $SubtypeOf(Type type) {
        return $Gson$Types.subtypeOf(type);
    }

    public static Type $SupertypeOf(Type type) {
        return $Gson$Types.supertypeOf(type);
    }
}

