package com.healthy.library.net;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.healthy.library.LibApplication;
import com.healthy.library.db.CacheDao;
import com.healthy.library.db.HMMHttpCache;
import com.healthy.library.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author Li
 * @date 2019/03/27 10:15
 * @des 重新生成请求体
 */

public class HMMInterceptor implements Interceptor {
    private Context mContext;
    private String mCommonParam = "function";
    private final Charset UTF8 = Charset.forName("UTF-8");
    public static final int CACHE_TIMEOUT=5000;

    HMMInterceptor(Context context) {
        mContext = context;
    }

    public  boolean isNeedCache(String function){//判断方法需要使用缓存
//        switch (function){
//            case "9202":
//                return true;
//        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody body = request.body();
        Response response = null;
        String function = null;
        String jsonJustBody=null;
        try {
            if (!(body instanceof FormBody) &&
                    !(body instanceof MultipartBody)) {
                Buffer buffer = new Buffer();
                if (body != null) {
                    body.writeTo(buffer);
                    String oldParam = buffer.readUtf8();
                    Gson gson = new Gson();
                    Map<String, Object> oldMap =
                            gson.fromJson(oldParam, HashMap.class);
                    if (oldMap.containsKey(mCommonParam)) {
                        function = String.valueOf(oldMap.get(mCommonParam));
                        oldMap.remove(mCommonParam);
                        String json = GenerateHeader.generate(mContext, function, oldMap);
                        jsonJustBody = GenerateHeader.generateJustBody(mContext, function, oldMap);
                        LogUtils.e(json);
                        request = request
                                .newBuilder()
                                .post(RequestBody.create(body.contentType(), json))
                                .build();
                    } else {
                        throw new IllegalArgumentException("param function missing");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HMMHttpCache hmmHttpCache=null;
        if(!TextUtils.isEmpty(function)){
//            hmmHttpCache= CacheDao.getInstance(LibApplication.getAppContext()).queryResponse(jsonJustBody);
        }
//        if(hmmHttpCache!=null&&isNeedCache(function)){
//            System.out.println("有缓存:\n"+hmmHttpCache);
//            System.out.println("能使用吗:"+(System.currentTimeMillis()-Long.parseLong(hmmHttpCache.time)));
//        }
        if(isNeedCache(function)&&hmmHttpCache!=null&&((System.currentTimeMillis()-Long.parseLong(hmmHttpCache.time))<=CACHE_TIMEOUT)){//5秒之内直接走缓存
            int maxStale = 60 * 60 * 24 * 28;
            System.out.println("走缓存");
            response  = new Response.Builder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .body(ResponseBody.create(MediaType.parse("application/json"), hmmHttpCache.response.getBytes()))
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("Cache")
                    .build();
            return response;
        }else {
            response = chain.proceed(request);
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        if (!HttpHeaders.hasBody(response)) { //HttpHeader -> 改成了 HttpHeaders，看版本进行选择
            //END HTTP
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
//                Log.i("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
//                CacheDao.getInstance(LibApplication.getAppContext()).insertResponse(jsonJustBody, new String(result.getBytes(), "UTF-8"),System.currentTimeMillis()+"");

                //获取到response的body的string字符串
                //do something .... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    LogUtils.e("\n\"function\":" + function + "\n" + "\"msg\":" + jsonObject.getString("msg") + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            L.i("<-- END HTTP (" + buffer.size() + "-byte body)");
        }
        return response;
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !"identity".equalsIgnoreCase(contentEncoding);
    }
}
