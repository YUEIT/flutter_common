package cn.yue.base.middle.net.intercept;

import java.io.IOException;

import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description : 将服务器的异常code 拦截
 * Created by yue on 2019/3/6
 */
public class ResponseInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        int code = proceed.code();
        if (code != 200 && code != 404) {
            throw new ResultException(NetworkConfig.ERROR_NO_NET, "网络不给力，请检查您的网络设置~");
        }
        return proceed;
    }
}
