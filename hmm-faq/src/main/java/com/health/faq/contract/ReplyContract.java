package com.health.faq.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.io.File;

/**
 * @author Li
 * @date 2019/07/08 11:22
 * @des 回复
 */

public interface ReplyContract {
    interface View extends BaseView {

        /**
         * 上传文件成功
         *
         * @param fileUrl 文件url地址
         */
        void onUploadFileSuccess(String fileUrl);

        /**
         * 上传文件失败
         */
        void onUploadFileFail();

        /**
         * 发布问答成功
         */
        void onReleaseSuccess();
    }

    interface Presenter extends BasePresenter {

        /**
         * 上传文件
         *
         * @param file 文件
         */
        void uploadFile(File file);

        /**
         * 发布回答
         *
         * @param questionId    问题id
         * @param isHidden      是否匿名
         * @param isWordOrSound 文字回答/语音回答
         * @param replyDetail   文字回答内容
         * @param voiceUrl      语音回答语音url
         * @param soundLength   语音时长
         */
        void releaseReply(String questionId, String isHidden, String isWordOrSound,
                          String replyDetail, String voiceUrl, String soundLength);

    }

}
