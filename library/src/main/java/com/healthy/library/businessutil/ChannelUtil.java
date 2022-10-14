package com.healthy.library.businessutil;

import android.content.Context;
import android.text.TextUtils;

import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.RetrofitHelper;
import com.healthy.library.utils.SpUtilsOld;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;


public class ChannelUtil {
    public static String getChannel(Context context){
        if(context == null){
            return BuildConfig.CHANNEL;
        }
        return WalleChannelReader.getChannel(context,"xiaomi");
    }
    public static String getFullChannel(Context context){
        return getChannel(null)+"-"+getChannel(context);
    }

    public static boolean isRealRelease(){
        if("release".equals(getChannel(null))&&!"test".equals(getChannel(LibApplication.getAppContext()))){
            return true;
        }
        return false;
    }

    public static boolean isIpRealRelease(){
        if(RetrofitHelper.getIp(LibApplication.getAppContext()).contains("capi.hanmama.com")&&!"test".equals(getChannel(LibApplication.getAppContext()))){
            return true;
        }
        return false;
    }

    public static String getAPPFullVersion(){
        String result= BuildConfig.VERSION_NAME;
        if(SpUtilsOld.getValue(LibApplication.getAppContext(), SpKey.YSLOOK,false)){
            try {
                Tinker tinker = Tinker.with(LibApplication.getAppContext());
                String tinkerId=tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.NEW_TINKER_ID);
                if(!TextUtils.isEmpty(tinkerId)){
                    result=tinkerId.replace("-patch","");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
