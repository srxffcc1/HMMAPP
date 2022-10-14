package com.healthy.library.interfaces;

/**
 * @author long
 * @description
 * @date 2021/7/3
 */
public interface OnRxFFmpegListener {

    /**
     * 压缩完毕
     */
    void onFinish();

    /**
     * 压缩进度
     */
    void onProgress(int progress, long progressTime);

    /**
     * 压缩出现异常
     * @param message 错误文本
     */
    void onError(String message);

}
