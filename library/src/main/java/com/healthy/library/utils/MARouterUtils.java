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

public class MARouterUtils {//广告位整体跳转路由综合器

    public static void passToTarget(Context context, IRouterLink adModel) {
        if (!TextUtils.isEmpty(adModel.getId())) {
            new AdPresenterCopy(context).posAd(new SimpleHashMapBuilder<String, Object>().puts("id", adModel.getId() + ""));
        }
        final String passString = adModel.getLink();
        passToTarget(context, passString);
    }

    public static void passToTarget(Context context, String passString) {
        passString = StringUtils.noEndLnString(passString);
        System.out.println("路由器:" + passString);
        if (TextUtils.isEmpty(passString)) {
            if (!ChannelUtil.isRealRelease()) {
                Toast.makeText(context, "未配置跳转链接", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (passString.startsWith("http")) {//是网页
            Uri uri = Uri.parse(passString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } else {
            if (buildRoute(passString, context)) {
                return;
            }

            ///hmmpassimp://hmm/index/webViewSingle?scheme=Lottery
            /*------------------ 广告位跳转积分抽奖 START -----------------------*/
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
                                    //跳转领奖中心下单页
                                    ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                                            //78434178651668480 -> 大转盘
                                            //78448856115200000 -> 九宫格
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
            /*------------------ 广告位跳转积分抽奖 END -----------------------*/
            if (!passString.startsWith("hmmpassimp://hmm")) {
                String resulypath=Uri.parse("hmmpassimp://hmm" + passString).getPath();
                try {
                    if(checkPath(resulypath)){
                        ARouter.getInstance().build(Uri.parse("hmmpassimp://hmm" + passString))
                                .navigation();
                    }else {
                        Toast.makeText(LibApplication.getAppContext(),"请更新至最新版本体验新功能",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(LibApplication.getAppContext(),"请更新至最新版本体验新功能",Toast.LENGTH_LONG).show();
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
                        String appId = Ids.WX_APP_ID; // 本应用微信AppId
                        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = "gh_93d673cec6a8"; // 小程序原始id
                        req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                        if (ChannelUtil.isRealRelease()) {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                        } else {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
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
        if ("保险服务".equals(passString)) {
            String url = "https://cps.qixin18.com/m/lj1059667/media.html";
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "保险服务")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if ("母婴商品".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(1));
            return true;
        }
        if ("妈妈听听".equals(passString)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN_MON)
                    .withString("audioType", "2")
                    .navigation();
            return true;
        }
        if ("宝宝爱听".equals(passString)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .navigation();
            return true;
        }
        if ("憨妈课堂".equals(passString)) {
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
        if ("育儿资讯".equals(passString)) {
            MobclickAgent.onEvent(context, "event2HomeSeeMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
                    .withString("knowOrInfoStatus", "2")
                    .withString("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .navigation();
            return true;
        }
        if ("新人礼包".equals(passString)) {
            ARouter.getInstance().build(DiscountRoutes.MINE_NEW_USER_GIFT)
                    .navigation();
            return true;
        }
        if ("憨妈直播".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(2));
            return true;
        }
        if ("同城圈".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(1));
            return true;
        }
        if ("技师招募".equals(passString)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_JOB_TYPE)
                    .navigation();
            return true;
        }
        if ("母婴商城".equals(passString)) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "憨妈妈首页导航商城点击量");
            MobclickAgent.onEvent(context, "event2APPHomeNavigationShopClick", nokmap);
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(0));
            return true;
        }
        if ("孕育工具".equals(passString)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TYPE)
                    .navigation();
            return true;
        }
        if ("专家答疑".equals(passString)) {
            ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_FAQ2)
                    .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE))
                    .navigation();
            return true;
        }
        if ("母婴教育".equals(passString)) {
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(2));
            return true;
        }
        if ("憨妈赚".equals(passString)) {
            if (SpUtils.getValue(context, "isHanMomVip", false) == true) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String appId = Ids.WX_APP_ID; // 本应用微信AppId
                        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = "gh_93d673cec6a8"; // 小程序原始id
                        req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                        if (ChannelUtil.isRealRelease()) {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                        } else {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
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
        if (passString.equals("极速问诊")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withBoolean("isNeedRef", true)
                    .withString("title", "极速问诊")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("品质优选")) {
            EventBus.getDefault().post(new TabChangeModel(3));
            return true;
        }
        if (passString.equals("名医在线")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withBoolean("isNeedRef", true)
                    .withString("title", "名医在线")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("专家咨询")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "专家咨询")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("孕育锦囊")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS)
                    .withInt("currentIndex", 0)
                    .navigation();
            return true;
        }
        if (passString.equals("孕育百科")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS)
                    .withInt("currentIndex", 1)
                    .navigation();
            return true;
        }
        if (passString.equals("孕期食谱")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_FOOD)
                    .withString("activityType", "孕期食谱")
                    .navigation();
            return true;
        }
        if (passString.equals("月子餐")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "月子餐")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_MEAL))
                    .navigation();
            return true;
        }
        if (passString.equals("月子餐")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "月子餐")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_MEAL))
                    .navigation();
            return true;
        }
        if (passString.equals("疫苗助手")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VACCINE_LIST)
                    .withInt("id", -1)
                    .navigation();
            return true;
        }
        if (passString.equals("心理咨询")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MentalityList01?dietitianGoodLabelType=7";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "心理咨询")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if (passString.equals("喂养记录")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY)
                    .navigation();
            return true;
        }
        if (passString.equals("童话故事")) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .withString("choseTypeName", "故事")
                    .navigation();
            return true;
        }
        if (passString.equals("胎教音乐")) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .withString("choseTypeName", "胎教")
                    .navigation();
            return true;
        }
        if (passString.equals("胎儿估重")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_BABY_WEIGHT)
                    .navigation();
            return true;
        }
        if (passString.equals("生日分析")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                    .withString("title", "生日分析")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", "http://zx.fengxinz100.cn/baobaojingpi/index?channel=swhmm000")
                    .navigation();
            return true;
        }
        if (passString.equals("生男生女")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_MAIN)
                    .navigation();
            return true;
        }
        if (passString.equals("能不能吃")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "能不能吃")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_CAN_EAT_ALL))
                    .navigation();
            return true;
        }
        if (passString.equals("待产包")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_BIRTH_PACKAGE)
                    .navigation();
            return true;
        }
        if (passString.equals("成长发育")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_GROW)
                    .navigation();
            return true;
        }
        if (passString.equals("产后抑郁")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                    .withString("title", "产后抑郁")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", "http://psychology.tengzhiff.com/detail/chan_hou_yi_yu?channel=swhmm000")
                    .navigation();
            return true;
        }
        if (passString.equals("宝宝起名")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_NAME)
                    .navigation();
            return true;
        }
        if (passString.equals("学育儿")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_INDEXBABY)
                    .navigation();
            return true;
        }
        if (passString.equals("购好物")) {
            EventBus.getDefault().post(new TabChangeModel(3));
            return true;
        }
        if (passString.equals("找服务")) {
            ARouter.getInstance()
                    .build(SecondRoutes.MAIN_MODULE)
                    .navigation();
            return true;
        }
        if (passString.contains("热聊")) {
            EventBus.getDefault().post(new TabChangeModel(1));
            return true;
        }
        if (passString.equals("宝宝解名")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                    .withString("title", "宝宝解名")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", "http://zx.fengxinz100.cn/xingmingxiangpi/index?channel=swhmm000")
                    .navigation();
            return true;
        }
        if (passString.equals("宝宝辅食")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "辅食大全")
                    .withString("url", SpUtils.getValue(context, UrlKeys.H5_FOOD_LIST))
                    .navigation();
            return true;
        }
        if (passString.equals("B超解读")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_ANALYZE_B)
                    .navigation();
            return true;
        }
        if (passString.contains("热聊")) {
            EventBus.getDefault().post(new TabChangeModel(1));
            return true;
        }
        return false;
    }
}
