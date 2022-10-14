package com.health.tencentlive.netutils;

public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
    boolean isCancled();
    void setCancled();
}