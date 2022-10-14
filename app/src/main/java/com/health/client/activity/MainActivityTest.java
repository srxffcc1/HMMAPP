package com.health.client.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.health.client.R;
import com.health.client.presenter.MainDialogPresenter;
import com.health.discount.contract.MainDialogContract;
import com.health.discount.widget.GiftDialog;
import com.health.mine.contract.ZxingScanContract;
import com.health.mine.presenter.ZxingScanPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.DownLoadFragment;
import com.healthy.library.business.GetCouponDialog;
import com.healthy.library.business.GetGiftDialog;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GuideViewHelp;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Events;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.ActH5Contract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedVideo;
import com.healthy.library.message.CanUpdateTab;
import com.healthy.library.message.UpdateGiftInfo;
import com.healthy.library.message.UpdateGuideInfo;
import com.healthy.library.message.UpdateKillMsg;
import com.healthy.library.model.Coupon;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShareEntity;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.UpdatePatchVersion;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.net.download.DownloadInfo;
import com.healthy.library.net.download.HmmDownloadManager;
import com.healthy.library.presenter.ActH5Presenter;
import com.healthy.library.presenter.RefCodePresenterCopy;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceInstallListener;

/**
 * @author Li
 * @date 2019/03/08 10:36
 * @des 壳
 */

@Route(path = AppRoutes.APP_MAIN_TEST)
public class MainActivityTest extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
