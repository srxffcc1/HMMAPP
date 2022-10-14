package com.healthy.library.net;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author Li
 * @date 2019/03/27 10:00
 * @des
 */

public interface HttpService {
    /**
     * 获取参数
     *
     * @param map 请求参数（必须包含function）
     * @return 报文
     */
    @POST("actionHandler")
    Observable<String> getData(@Body Map<String, Object> map);

    /**
     * 上传文件
     *
     * @param part 文件part
     * @return 报文
     */
    @Multipart
    @POST("saveSoundFiles")
    Observable<String> uploadFile(@Part MultipartBody.Part part);
}
