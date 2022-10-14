package com.healthy.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.model.LotteryModel;
import com.healthy.library.model.ServiceTabChangeModel;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.RetrofitHelper;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.AdPresenterCopy;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.SoundRoutes;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MMiniPass {//跳转小程序 综合的来一个
    public static void passMini(String miniName,String path){
        String appId = Ids.WX_APP_ID;
        IWXAPI api = WXAPIFactory.createWXAPI(LibApplication.getAppContext(), appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = miniName;
        req.path = path;
        System.out.println("小程序跳转:"+req.path);
        if (ChannelUtil.isIpRealRelease()) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        }
        api.sendReq(req);
    }

}
