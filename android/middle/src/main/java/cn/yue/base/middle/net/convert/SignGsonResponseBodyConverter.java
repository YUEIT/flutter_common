package cn.yue.base.middle.net.convert;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.wrapper.BaseBean;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Description : 注册一个自定义的转换类GsonResponseBodyConverter
 * Created by yue on 2018/7/24
 */
class SignGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Object EMPTY_OBJECT = new Object();
    private final Gson gson;
    private final Type type;

    SignGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        LogUtils.i("server response: " + response);
        Type BaseBeanTtype = $Gson$Types.newParameterizedTypeWithOwner(null, BaseBean.class, type);
        BaseBean result = gson.fromJson(response, BaseBeanTtype);
        if (NetworkConfig.SUCCESS_FLAG.equals(result.getRealCode())) {
            //flag 1 表示成功返回，继续用本来的Model类解析
            //剥离无用字段
            T data = (T) result.getData();

            //不在乎服务器的返回值 传入Object ，则此时 data不判空
            if (type != Object.class) {
                //空数据返回异常
                if (data == null) {
                    throw new ResultException(NetworkConfig.ERROR_NO_DATA, "服务器返回数据为空");
                }
                if (data instanceof List && ((List) data).isEmpty()) {
                    throw new ResultException(NetworkConfig.ERROR_NO_DATA, "服务器返回数据为空");
                }
            } else if (null == data) {
                //Rxjava sucess complete 都不能传空数据 所以....
                data = (T) EMPTY_OBJECT;
            }
            return data;
        } else if (null == result.getRealCode()) {
            //不是BaseBean结构
            T json = gson.fromJson(response, type);
            LogUtils.d("no code : " + json);
            return json;
        } else {
            LogUtils.d("error " + result.getRealCode() + " , " + result.getRealMessage());
            throw new ResultException(result.getRealCode(), result.getRealMessage());
        }

    }
}