package cn.yue.base.middle.net.intercept;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yue.base.common.utils.constant.EncryptUtils;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.middle.init.InitConstant;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.convert.RequestConverterBean;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class SignInterceptor implements Interceptor {

    private boolean isDataEncode = false;

    public SignInterceptor(){}

    public SignInterceptor(boolean isDataEncode) {
        this.isDataEncode = isDataEncode;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String language = original.header("language");
        if (original.method().equals("GET")) {
           return chain.proceed(interceptGet(original));
        } else if(original.method().equals("POST")){
            //这里获取的是url中的查询参数，body里的参数暂时没有想到方法获取
           return chain.proceed(interceptPost(original));
        }
        return chain.proceed(original);
    }

    private Request interceptGet(Request original){
        Map<String , Object> map = new HashMap<>();
        //获取传入的全部参数
        if(!original.url().queryParameterNames().isEmpty()) {
            for (String key : original.url().queryParameterNames()) {
                for (Object o : original.url().queryParameterValues(key)) {
                    map.put(key, o);
                }
            }
        }
        LogUtils.i("okhttp", "  origin :  " +map);
        String encodeData = toJsonStr(map);
        HttpUrl url;
        long time = System.currentTimeMillis() / 1000;
        try {
            if(isDataEncode) {
                encodeData = URLEncoder.encode(encodeData.toString(), "utf-8");
                encodeData = encodeData.replaceAll("\\+", "%20");
            }
            String appVersion = InitConstant.getVersionName();
            String deviceId = InitConstant.getDeviceId();
            String sign = EncryptUtils.md5((appVersion + InitConstant.APP_CLIENT_TYPE + encodeData +
                    deviceId + time + InitConstant.APP_SIGN_KEY).getBytes());

            url = original.url().newBuilder().query(null)
                    .addQueryParameter("data", encodeData)
                    .addQueryParameter("app_version", appVersion)
                    .addQueryParameter("client_type", InitConstant.APP_CLIENT_TYPE + "")
                    .addQueryParameter("device_id", deviceId)
                    .addQueryParameter("time", time+"")
                    .addQueryParameter("sign", sign)
                    .build();
            LogUtils.i("okhttp", "  intercept : " + url.toString());
            Request request = original.newBuilder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .build();
            return request;
        } catch (Exception e) {
            e.printStackTrace();
            return original;
        }
    }

    private Request interceptPost(Request original) throws IOException {
        if(original.body() != null && original.body().contentLength() > 0){
            //如果有body的情况，直接用body
            HttpUrl url = original.url().newBuilder().query(null).build();
            return original.newBuilder().url(url).build();
        }else {
            //这里获取的是url中的查询参数，body里的参数暂时没有想到方法获取
            Map<String, Object> map = new HashMap<>();
            //获取传入的全部参数
            if (!original.url().queryParameterNames().isEmpty()) {
                for (String key : original.url().queryParameterNames()) {
                    //URL后面的参数，暂时没想到如何识别list；比较挫的方式参数前加上"LIST_"用来区分；
                    if(key.startsWith("LIST_")){
                        //含有list的情况;
                        List<Object> list = new ArrayList<>();
                        for (String value : original.url().queryParameterValues(key)) {
                            //如果能解析成功，说明是bean类型,转成Object ; 失败：基本类型直接add
                            try {
                                RequestConverterBean bean = gson.fromJson(value, RequestConverterBean.class);
                                list.add(gson.fromJson(bean.getJson(), Class.forName(bean.getClassName())));
                            } catch (Exception e) {
                                list.add(value);
                            }
                        }
                        map.put(key.replace("LIST_", ""), list);
                    }else{
                        if (original.url().queryParameterValues(key).size() > 1) {
                            throw new ResultException(NetworkConfig.ERROR_OPERATION, "请求的数据类型为list，且参数名未以LIST_开始~");
                        }
                        String value = original.url().queryParameterValues(key).get(0);
                        try {
                            RequestConverterBean bean = gson.fromJson(value, RequestConverterBean.class);
                            map.put(key, gson.fromJson(bean.getJson(), Class.forName(bean.getClassName())));
                        } catch (Exception e) {
                            map.put(key, value);
                        }
                    }
                }
            }
            LogUtils.i("okhttp", "  origin :  " + map);
            HttpUrl url = original.url().newBuilder().query(null).build();
            LogUtils.i("okhttp", "  intercept : " + url.toString() + "  --------   body: " + gson.toJson(getBody(map)));
            Request request = original.newBuilder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .method("POST", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(getBody(map))))
                    .build();
            return request;
        }
    }

    private Map<String, Object> getBody(Map<String, Object> map){
        String encodeData = toJsonStr(map);
        Map<String, Object> tmp = new HashMap<>();
        long time = System.currentTimeMillis() / 1000;
        try {
            if(isDataEncode){
                encodeData = URLEncoder.encode(encodeData.toString(), "utf-8");
                encodeData = encodeData.replaceAll("\\+", "%20");

            }
            String appVersion = InitConstant.getVersionName();
            String deviceId = InitConstant.getDeviceId();
            String sign = EncryptUtils.md5((appVersion + InitConstant.APP_CLIENT_TYPE + encodeData +
                    deviceId + time + InitConstant.APP_SIGN_KEY).getBytes());
            tmp.put("app_version", appVersion);
            tmp.put("client_type", InitConstant.APP_CLIENT_TYPE + "");
            tmp.put("data", encodeData);
            tmp.put("device_id", deviceId);
            tmp.put("time", time + "");
            tmp.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }


    private static Gson gson = new Gson();
    private static String toJsonStr(Map<String,Object> map){
        if(map==null){
            map = new HashMap<>();
        }
        return gson.toJson(map);
    }
}
