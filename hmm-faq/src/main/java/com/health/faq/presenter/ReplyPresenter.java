package com.health.faq.presenter;

import android.content.Context;

import com.health.faq.contract.ReplyContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author Li
 * @date 2019/07/08 14:22
 * @des
 */
public class ReplyPresenter implements ReplyContract.Presenter {

    private Context mContext;
    private ReplyContract.View mView;

    public ReplyPresenter(Context context, ReplyContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void uploadFile(File file) {
        //System.out.println("文件目录:"+file.getAbsolutePath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part
                .createFormData("files", String.format("audio_%s.amr",
                        UUID.randomUUID().toString()), requestBody);
        ObservableHelper.createUploadObservable(mContext, part)
                .subscribe(new StringObserver(mView, mContext, false,
                        true, true, false, false,
                        true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
                            JSONArray soundArray = JsonUtils.getJsonArray(dataObject, "sound");
                            if (soundArray.length() > 0) {
                                mView.onUploadFileSuccess(JsonUtils.getString(soundArray, 0));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onUploadFileFail();
                    }
                });

    }

    @Override
    public void releaseReply(String questionId, String isHidden, String isWordOrSound,
                             String replyDetail, String voiceUrl, String soundLength) {
        Map<String, Object> map = new HashMap<>(6);
        map.put(Functions.FUNCTION, Functions.FUNCTION_RELEASE_REPLY);
        map.put("questionId", questionId);
        map.put("isHidden", isHidden);
        map.put("replyDetail", replyDetail);
        map.put("soundUrl", voiceUrl);
        map.put("isWordOrSound", isWordOrSound);
        map.put("soundLength", soundLength);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true,
                        true, true, false, false,
                        true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onReleaseSuccess();
                    }
                });
    }
}
