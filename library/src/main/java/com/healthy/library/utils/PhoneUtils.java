package com.healthy.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author: long
 * @date: 2021/4/1
 */
public class PhoneUtils {

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setData(data);
        context.startActivity(intent);
    }

}
