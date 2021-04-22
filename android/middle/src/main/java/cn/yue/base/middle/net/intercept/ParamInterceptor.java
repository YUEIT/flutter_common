package cn.yue.base.middle.net.intercept;

import java.io.IOException;

import cn.yue.base.middle.init.InitConstant;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class ParamInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl url = original.url().newBuilder()
                .addQueryParameter("appplt", "aph")
                .addQueryParameter("appver", InitConstant.getVersionName())
                .addQueryParameter("appid", "1")
                .build();

        Request request = original.newBuilder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }
}
