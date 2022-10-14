package com.healthy.library.net;

/**
 * @author Li
 * @date 2019/07/16 11:10
 * @des
 */
public interface ProgressListener {
    /**
     * 进度监听
     *
     * @param bytesRead 已经下载或上传字节数
     * @param total     总字节数
     * @param done      是否完成
     */
    void onProgressChanged(long bytesRead, long total, boolean done);
}
