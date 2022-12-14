package com.healthy.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
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

public class MARouterUtils {//????????????????????????????????????

    public static void passToTarget(Context context, IRouterLink adModel) {
        if (!TextUtils.isEmpty(adModel.getId())) {
            new AdPresenterCopy(context).posAd(new SimpleHashMapBuilder<String, Object>().puts("id", adModel.getId() + ""));
        }
        final String passString = adModel.getLink();
        passToTarget(context, passString);
    }

    public static void passToTarget(Context context, String passString) {
        passString = StringUtils.noEndLnString(passString);
        System.out.println("?????????:" + passString);
        if (TextUtils.isEmpty(passString)) {
            if (!ChannelUtil.isRealRelease()) {
                Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (passString.startsWith("http")) {//?????????
            Uri uri = Uri.parse(passString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } else {
            if (buildRoute(passString, context)) {
                return;
            }

            ///hmmpassimp://hmm/index/webViewSingle?scheme=Lottery
            /*------------------ ??????????????????????????? START -----------------------*/
            if (passString.contains("scheme=Lottery")) {
                Map<String, Object> map = new HashMap();
                map.put(Functions.FUNCTION, "lottery_6004");
                map.put("merchantId", SpUtils.getValue(context, SpKey.CHOSE_MC));
                LifecycleOwner lifecycleOwner;
                if (context instanceof LifecycleOwner) {
                    lifecycleOwner = (LifecycleOwner) context;
                } else {
                    lifecycleOwner = (LifecycleOwner) FrameActivityManager.instance().topActivityLifecycleOwner();
                }
                RetrofitHelper.createService(context)
                        .getData(map)
                        .compose(RxThreadUtils.Obs_io_main())
                        .to(RxLifecycleUtils.bindLifecycle(lifecycleOwner))
                        .subscribe(new NoStringObserver(null, context,
                                false) {

                            @Override
                            protected void onGetResultSuccess(String obj) {
                                super.onGetResultSuccess(obj);
                                try {
                                    JSONObject data = new JSONObject(obj).getJSONObject("data");
                                    String userShopInfoDTOS = data.toString();
                                    GsonBuilder builder = new GsonBuilder();
                                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                            return new Date(json.getAsJsonPrimitive().getAsLong());
                                        }
                                    });
                                    Gson gson = builder.create();
                                    Type type = new TypeToken<LotteryModel>() {
                                    }.getType();
                                    LotteryModel mLotteryModel = gson.fromJson(userShopInfoDTOS, type);

                                    String url = "http://192.168.10.181:8000/lottery.html";
                                    if (!TextUtils.isEmpty(SpUtils.getValue(context, UrlKeys.H5_LOTTERY_URL))) {
                                        url = SpUtils.getValue(context, UrlKeys.H5_LOTTERY_URL);
                                    }
                                    //???????????????????????????
                                    ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                                            //78434178651668480 -> ?????????
                                            //78448856115200000 -> ?????????
                                            .withString("url", url + "?id=" + mLotteryModel.getId() + "&token=" + SpUtils.getValue(context, SpKey.TOKEN))
                                            .withString("title", "")
                                            .withBoolean("isShowTopBar", false)
                                            .navigation();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onFinish() {
                                super.onFinish();
                            }

                            @Override
                            protected void onFailure(String msg) {
                                super.onFailure(msg);
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                return;
            }
            /*------------------ ??????????????????????????? END -----------------------*/
            if (!passString.startsWith("hmmpassimp://hmm")) {
                String resulypath=Uri.parse("hmmpassimp://hmm" + passString).getPath();
                try {
                    if(checkPath(resulypath)){
                        ARouter.getInstance().build(Uri.parse("hmmpassimp://hmm" + passString))
                                .navigation();
                    }else {
                        Toast.makeText(LibApplication.getAppContext(),"???????????????????????????????????????",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (passString.startsWith("/")) {
                    String resulypath=Uri.parse(passString).getPath();
                    try {
                        if(checkPath(resulypath)){
                            ARouter.getInstance().build(Uri.parse(passString))
                                    .navigation();
                        }else {
                            Toast.makeText(LibApplication.getAppContext(),"???????????????????????????????????????",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static boolean checkPath(String path) {
        try {
            String group = path.substring(1, path.indexOf("/", 1));
            LogisticsCenter.completion(new Postcard(path, group));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private static boolean buildRoute(String passString, Context context) {
        if ("/mine/hanMom".equals(passString)) {
            if (SpUtils.getValue(context, "isHanMomVip", false) == true) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String appId = Ids.WX_APP_ID; // ???????????????AppId
                        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = "gh_93d673cec6a8"; // ???????????????id
                        req.path = "";                  //???????????????????????????????????????????????????????????????????????????
                        if (ChannelUtil.isRealRelease()) {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// ???????????? ?????????????????????????????????
                        } else {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// ???????????? ?????????????????????????????????
                        }
                        api.sendReq(req);
                    }
                }, 500);

            } else {
                ARouter.getInstance().build(MineRoutes.MINE_HANMOM)
                        .navigation();
            }
            return true;
        }
        if ("????????????".equals(passString)) {
            String url = "https://cps.qixin18.com/m/lj1059667/media.html";
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(1));
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN_MON)
                    .withString("audioType", "2")
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                    .withString("type", "0")
                    .navigation();
            return true;
        }
        if ("hanVideo".equals(passString)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                    .withString("type", "1")
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            MobclickAgent.onEvent(context, "event2HomeSeeMoreClick", new SimpleHashMapBuilder().puts("soure", "??????"));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
                    .withString("knowOrInfoStatus", "2")
                    .withString("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance().build(DiscountRoutes.MINE_NEW_USER_GIFT)
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(2));
            return true;
        }
        if ("?????????".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(1));
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_JOB_TYPE)
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "????????????????????????????????????");
            MobclickAgent.onEvent(context, "event2APPHomeNavigationShopClick", nokmap);
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(0));
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TYPE)
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_FAQ2)
                    .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE))
                    .navigation();
            return true;
        }
        if ("????????????".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(2));
            return true;
        }
        if ("?????????".equals(passString)) {
            if (SpUtils.getValue(context, "isHanMomVip", false) == true) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String appId = Ids.WX_APP_ID; // ???????????????AppId
                        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = "gh_93d673cec6a8"; // ???????????????id
                        req.path = "";                  //???????????????????????????????????????????????????????????????????????????
                        if (ChannelUtil.isRealRelease()) {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// ???????????? ?????????????????????????????????
                        } else {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// ???????????? ?????????????????????????????????
                        }
                        api.sendReq(req);
                    }
                }, 500);

            } else {
                ARouter.getInstance().build(MineRoutes.MINE_HANMOM)
                        .navigation();
            }
            return true;
        }
        if (passString.equals("????????????")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withBoolean("isNeedRef", true)
                    .withString("title", "????????????")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            EventBus.getDefault().post(new TabChangeModel(3));
            return true;
        }
        if (passString.equals("????????????")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withBoolean("isNeedRef", true)
                    .withString("title", "????????????")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS)
                    .withInt("currentIndex", 0)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS)
                    .withInt("currentIndex", 1)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_FOOD)
                    .withString("activityType", "????????????")
                    .navigation();
            return true;
        }
        if (passString.equals("?????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "?????????")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_MEAL))
                    .navigation();
            return true;
        }
        if (passString.equals("?????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "?????????")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_MEAL))
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VACCINE_LIST)
                    .withInt("id", -1)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MentalityList01?dietitianGoodLabelType=7";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .withString("choseTypeName", "??????")
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .withString("choseTypeName", "??????")
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_BABY_WEIGHT)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                    .withString("title", "????????????")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", "http://zx.fengxinz100.cn/baobaojingpi/index?channel=swhmm000")
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_MAIN)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_CAN_EAT_ALL))
                    .navigation();
            return true;
        }
        if (passString.equals("?????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_BIRTH_PACKAGE)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_GROW)
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                    .withString("title", "????????????")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", "http://psychology.tengzhiff.com/detail/chan_hou_yi_yu?channel=swhmm000")
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_NAME)
                    .navigation();
            return true;
        }
        if (passString.equals("?????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_INDEXBABY)
                    .navigation();
            return true;
        }
        if (passString.equals("?????????")) {
            EventBus.getDefault().post(new TabChangeModel(3));
            return true;
        }
        if (passString.equals("?????????")) {
            ARouter.getInstance()
                    .build(SecondRoutes.MAIN_MODULE)
                    .navigation();
            return true;
        }
        if (passString.contains("??????")) {
            EventBus.getDefault().post(new TabChangeModel(1));
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                    .withString("title", "????????????")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", "http://zx.fengxinz100.cn/xingmingxiangpi/index?channel=swhmm000")
                    .navigation();
            return true;
        }
        if (passString.equals("????????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_FOOD_LIST))
                    .navigation();
            return true;
        }
        if (passString.equals("B?????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_ANALYZE_B)
                    .navigation();
            return true;
        }
        if (passString.contains("??????")) {
            EventBus.getDefault().post(new TabChangeModel(1));
            return true;
        }
        return false;
    }
}
