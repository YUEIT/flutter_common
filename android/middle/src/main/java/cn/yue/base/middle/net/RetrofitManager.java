package cn.yue.base.middle.net;


import java.util.concurrent.TimeUnit;

import cn.yue.base.middle.init.InitConstant;
import cn.yue.base.middle.net.convert.SignGsonConverterFactory;
import cn.yue.base.middle.net.intercept.NoNetInterceptor;
import cn.yue.base.middle.net.intercept.ParamInterceptor;
import cn.yue.base.middle.net.intercept.ResponseInterceptor;
import cn.yue.base.middle.net.intercept.SignInterceptor;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class RetrofitManager {

    private static final int DEFAULT_TIMEOUT = 60;

    public static RetrofitManager getInstance() {
        return RetrofitManagerHolder.instance;
    }

    private static class RetrofitManagerHolder {
        private final static RetrofitManager instance = new RetrofitManager();
    }

    public OkHttpClient.Builder getOkHttpClientBuilder(boolean isSign) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (InitConstant.logMode) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        builder.addInterceptor(new NoNetInterceptor());
        if (isSign) {
            builder.addInterceptor(new SignInterceptor());
        }
        builder.addInterceptor(new ResponseInterceptor());
        builder.retryOnConnectionFailure(true);
        return builder;
    }

    public OkHttpClient.Builder getParamOkHttpClientBuilder() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (InitConstant.logMode) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        builder.addInterceptor(new NoNetInterceptor());
        builder.addInterceptor(new ParamInterceptor());
        builder.addInterceptor(new ResponseInterceptor());
        builder.retryOnConnectionFailure(true);
        return builder;
    }

    /**
     *  加密方式的
     */
    public Retrofit getSignRetrofit(String baseUrl) {
        handlerError();
        OkHttpClient.Builder signBuilder = getOkHttpClientBuilder(true);
        return new Retrofit.Builder()
                .client(signBuilder.build())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //注册自定义的工厂类
                .addConverterFactory(SignGsonConverterFactory.create())
                .build();
    }

    /**
     *  没有参数拦截和解析
     */
    public Retrofit getRetrofit(String baseUrl) {
        handlerError();
        OkHttpClient.Builder builder = getOkHttpClientBuilder(false);
        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     *  没有参数拦截，有返回处理解析
     */
    public Retrofit getConverterRetrofit(String baseUrl) {
        handlerError();
        OkHttpClient.Builder builder = getOkHttpClientBuilder(false);
        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SignGsonConverterFactory.create())
                .build();
    }

    /**
     *  部分参数（版本号之类）拦截，有返回处理解析
     */
    public Retrofit getParamRetrofit(String baseUrl) {
        handlerError();
        OkHttpClient.Builder builder = getParamOkHttpClientBuilder();
        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SignGsonConverterFactory.create())
                .build();
    }
    // 查看CallObservable的subscribeActual方法可知，一般情况下异常会被“observer.onError(t);”中处理
    // 但是如果是onError中抛出的异常，会调用RxJavaPlugins.onError方法，所有这里实现Consumer<Throwable>接口，并让异常在accept中处理
    // 考虑到ResultException是自定义异常，并不能让APP闪退，这里拦截，如果是其他异常直接抛出。
    private void handlerError() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (!(throwable instanceof ResultException)) {
                    throw new Exception(throwable);
                }
            }
        });
    }

}
