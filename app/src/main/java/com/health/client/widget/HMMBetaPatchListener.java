package com.health.client.widget;

import android.util.Log;

import com.healthy.library.LibApplication;
import com.healthy.library.message.UpdateKillMsg;
import com.healthy.library.utils.SpUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

public class HMMBetaPatchListener implements BetaPatchListener {

    @Override
    public void onPatchReceived(String patchFile) {
        Log.i("CrashReport", "补丁下载地址:" + patchFile);
//        Beta.downloadPatch();
    }

    @Override
    public void onDownloadReceived(long savedLength, long totalLength) {
        Log.i("CrashReport", "补丁下载进度:" + String.format(Locale.getDefault(), "%s %d%%",
                Beta.strNotificationDownloading,
                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));

    }

    @Override
    public void onDownloadSuccess(String msg) {
        Log.i("CrashReport", "补丁下载成功:"+msg);
        Beta.applyDownloadedPatch();
    }

    @Override
    public void onDownloadFailure(String msg) {
        Log.i("CrashReport", "补丁下载失败");
    }

    @Override
    public void onApplySuccess(String msg) {
        Log.i("CrashReport", "补丁应用成功");
        SpUtils.store(LibApplication.getAppContext(),"showKillDialog",true);
        EventBus.getDefault().post(new UpdateKillMsg());
    }

    @Override
    public void onApplyFailure(String msg) {
        Log.i("CrashReport", "补丁应用失败"+msg);
    }

    @Override
    public void onPatchRollback() {
        Log.i("CrashReport", "补丁回滚");
    }
}
