package com.healthy.library.net;

import android.content.Context;
import android.text.TextUtils;

import com.ayvytr.okhttploginterceptor.LoggingInterceptor;
import com.ayvytr.okhttploginterceptor.LoggingLevel;
import com.healthy.library.BuildConfig;
import com.healthy.library.constant.SpKey;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.utils.SpUtils;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author Li
 * @date 2019/03/27 10:16
 * @des 请求
 */

public class RetrofitHelper {

    private static final long CONNECT_TIMEOUT = 20;
    private static final long READ_TIMEOUT = 20;
    private static final long WRITE_TIMEOUT = 20;

    private static final long CONNECT_TIMEOUT2 = 20;
    private static final long READ_TIMEOUT2 = 20;
    private static final long WRITE_TIMEOUT2 = 20;

    private static Retrofit retrofit;
    private static Retrofit retrofitMall;
    private static Retrofit fileRetrofit;
    private static OkHttpClient normalOkHttpClient;
    private static OkHttpClient fileOkhttpClient;

    public static void clear(){
        retrofit=null;
        retrofitMall=null;
        fileRetrofit=null;
        normalOkHttpClient=null;
        fileOkhttpClient=null;
    }

    private static OkHttpClient getOkHttpClient(Context context) {
        if (normalOkHttpClient == null) {
            LoggingInterceptor loginterceptor = new LoggingInterceptor(LoggingLevel.ALL);
            if(!ChannelUtil.isRealRelease()){
                normalOkHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new HMMInterceptor(context))
                        .addInterceptor(loginterceptor)
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(false)
                        .build();
            }else {
                normalOkHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new HMMInterceptor(context))
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .proxy(Proxy.NO_PROXY)
                        .retryOnConnectionFailure(false)
                        .build();
            }

        }
        return normalOkHttpClient;
    }

    private static OkHttpClient getOkHttpClientUpload(Context context) {
        if (normalOkHttpClient == null) {
            normalOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HMMInterceptor(context))
                    .connectTimeout(CONNECT_TIMEOUT2, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT2, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT2, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();
        }
        return normalOkHttpClient;
    }

    private static OkHttpClient fileClient(Context context,
                                           final ProgressListener progressListener) {
        if (fileOkhttpClient == null) {
            fileOkhttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
                            return response.newBuilder()
                                    .body(new ProgressResponseBody(response.body(), progressListener))
                                    .build();
                        }
                    })
                    .addInterceptor(new HMMInterceptor(context))
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();
        }
        return fileOkhttpClient;
    }
    public static String getIp(Context context){
        if(!TextUtils.isEmpty(SpUtils.getValue(context, SpKey.TEST_IP))&&!ChannelUtil.isRealRelease()){
            return SpUtils.getValue(context, SpKey.TEST_IP);
        }
        return BuildConfig.BASE_URL;
    }

    public static HttpService createFileService(Context context, ProgressListener progressListener) {
        if (fileRetrofit == null) {
            fileRetrofit = new Retrofit.Builder()
                    .client(fileClient(context, progressListener))
                    .baseUrl(getIp(context))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return fileRetrofit.create(HttpService.class);
    }


    public static HttpService createServiceUpload(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClientUpload(context))
                    .baseUrl(getIp(context))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(HttpService.class);
    }

    public static HttpService createService(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient(context))
                    .baseUrl(getIp(context))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(HttpService.class);
    }
}
