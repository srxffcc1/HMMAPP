package com.healthy.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;

import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.dialog.SimpleDialog;

/**
 * Author: Li
 * Date: 2018/8/23 0023
 * Description:
 */
public class PermissionUtils {
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
    //首页特殊的不再询问了
    public static boolean hasPermissionsIgnore(Context context, String... permissions) {
        if(ChannelUtil.getChannel(context).contains("huawei")){
            return hasPermissions(context,permissions);
        }
        return true;
    }

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 是否应该显示获取权限界面
     *
     * @param activity    申请权限界面
     * @param permissions 权限
     * @return true：权限有被拒绝的并且勾选了不再显示
     */
    public static boolean shouldShowRationale(Activity activity, String... permissions) {
        return !hasPermissions(activity, permissions) &&
                somePermissionPermanentlyDenied(activity, permissions);
    }

    public static boolean somePermissionPermanentlyDenied(Activity activity, String... permissions) {
        for (String permission : permissions) {
            boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (!result) {
                return true;
            }
        }
        return false;
    }

    public static void showRationale(final Context context, final String content) {
        new SimpleDialog.Builder(context)
                .setTitle("权限提示")
                .setContent(content)
                .setNegativeBtn("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        try {
//                            ((Activity)context).finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                })
                .setPositiveBtn("去授予", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        IntentUtils.toSelfSetting(context);
                        try {
                            ((Activity)context).finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create().show();
    }

    public static void showRationale(final Context context, @StringRes int strId) {
        showRationale(context, context.getResources().getString(strId));
    }

    public static void showRationale(final Context context) {
        showRationale(context, "您已拒绝了权限，可能会导致应用使用异常，是否前往设置界面授予权限");
    }
}
