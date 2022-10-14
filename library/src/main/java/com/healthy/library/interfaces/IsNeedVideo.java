package com.healthy.library.interfaces;

import android.os.Handler;

/**
 * @author Li
 * @date 2019/07/04 15:32
 * @des 是否需要分享
 */
public interface IsNeedVideo {

    String getToken();

    String getCourseId();

    String getLiveStatus();

    Handler getVideoHandler();
}
