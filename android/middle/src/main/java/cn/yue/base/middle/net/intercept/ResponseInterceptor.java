package cn.yue.base.middle.net.intercept;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description : 将服务器的异常code 转成200 以便于在gson解析时自动处理，根据flag 判断请求成功失败
 * Created by yue on 2019/3/6
 */
public class ResponseInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        int code = proceed.code();
        if (code != 200 && code != 404) {
            Response adapterResponse = proceed.newBuilder()
                    .code(200)
                    .build();
            return adapterResponse;
        }
        return proceed;
    }
}
