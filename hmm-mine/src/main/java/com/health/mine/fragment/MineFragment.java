package com.health.mine.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.mine.R;
import com.health.mine.contract.InviteContract;
import com.health.mine.contract.ZxingScanContract;
import com.health.mine.presenter.InvitePresenter;
import com.health.mine.presenter.ZxingScanPresenter;
import com.health.mine.widget.MyAdviserDialog;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.ElcCardDialogView;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.MineContract;
import com.healthy.library.contract.PlusContract;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.dialog.TongLianPhoneBindDialog;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.message.UpdateMessageInfo;
import com.healthy.library.message.UpdateUserBindInfoMsg;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.model.CouponInfo;
import com.healthy.library.model.IndexMenu;
import com.healthy.library.model.InviteAct;
import com.healthy.library.model.InviteJoinInfo;
import com.healthy.library.model.MineFans;
import com.healthy.library.model.OrderInfo;
import com.healthy.library.model.OrderNum;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.model.VipCard;
import com.healthy.library.model.VipInfo;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.MinePresenter;
import com.healthy.library.presenter.PlusPresenter;
import com.healthy.library.presenter.QiYeWeiXinPresenter;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * @author Li
 * @date 2019/03/26 10:16
 * @des "我的"
 */
@Route(path = MineRoutes.MINE_MODULE)
public class MineFragment extends BaseFragment implements AdContract.View, MineContract.View, ZxingScanContract.View,
        SwipeRefreshLayout.OnRefreshListener, PlusContract.View, InviteContract.View, QiYeWeiXinContract.View {
    private Badge noDaiFu;
    private Badge noDaiYong;
    private Badge noDaiPing;
    private Badge norefunds;
    private MinePresenter mPresenter;
    private SwipeRefreshLayout layoutRefresh;
    private ImageView vipBgImg;
    private ImageView passSetting;
    private ImageView passMessage;
    private ImageView passZxing;
    private ConstraintLayout ivAvatarLL;
    private CornerImageView ivAvatar;
    private TextView tvNickname;
    private TextView tvStatus;
    private LinearLayout postLL;
    private LinearLayout lly1;
    private TextView gz;
    private LinearLayout lly2;
    private TextView fs;
    private LinearLayout lly3;
    private TextView tz;
    private LinearLayout tmpBlock;
    private LinearLayout moneyLL;
    private LinearLayout llh2;
    private TextView hdye;
    private LinearLayout llh3;
    private TextView tvYhq;
    private LinearLayout llh1;
    private ConstraintLayout conOrder;
    private ConstraintLayout conOrderAll;
    private TextView orderTitle;
    private LinearLayout llOrderType;
    private LinearLayout passtodaifukuanLL;
    private ImageView passtodaifukuan;
    private LinearLayout passtodaishiyongLL;
    private ImageView passtodaishiyong;
    private LinearLayout passtodaidianpingLL;
    private ImageView passtodaidianping;
    private LinearLayout passtodaituihuoLL;
    private ImageView passtodaituihuo;
    private LinearLayout mallBannerLL;
    private Banner mallBanner;
    private GridLayout vipFucLL;
    private LinearLayout llh4;
    private TextView tvJf;
    private TextView tvChuzhi;
    private ImageTextView tvChuzhiCount;
    private boolean vipCardSize = false;
    private ZxingScanPresenter zxingScanPresenter;
    private MyAdviserDialog myAdviserDialog;
    private boolean isBinding = false;
    private ImageView passElcCard;
    private ConstraintLayout plusRight;
    private ImageView plusCrown;
    private TextView plusText;
    private TextView plusBtn;
    InviteAct inviteAct;
    InvitePresenter invitePresenter;
    private ImageView mUserBadge;
    private ShapeTextView mUserBadgeName;
    QiYeWeiXinPresenter qiYeWeiXinPresenter;

    private void setStatusColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //setStatusColor();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment_mine_vip;
    }

    @Override
    protected void findViews() {
        initView();
        llh1.setOnClickListener(this);
        llh2.setOnClickListener(this);
        llh3.setOnClickListener(this);
        llh4.setOnClickListener(this);
        conOrderAll.setOnClickListener(this);
        passtodaifukuanLL.setOnClickListener(this);
        passtodaishiyongLL.setOnClickListener(this);
        passtodaidianpingLL.setOnClickListener(this);
        passtodaituihuoLL.setOnClickListener(this);
        passSetting.setOnClickListener(this);
        passMessage.setOnClickListener(this);
        passZxing.setOnClickListener(this);
        lly1.setOnClickListener(this);
        lly2.setOnClickListener(this);
        lly3.setOnClickListener(this);
        ivAvatarLL.setOnClickListener(this);
        qiYeWeiXinPresenter = new QiYeWeiXinPresenter(mContext, this);
        zxingScanPresenter = new ZxingScanPresenter(mContext, this);
        invitePresenter = new InvitePresenter(mContext, this);
        adPresenter = new AdPresenter(mContext, this);
        myAdviserDialog = MyAdviserDialog.newInstance();
        zxingScanPresenter.getTokerWorkerInfo();
        buildFucs();
        passElcCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElcCardDialogView.newInstance().show(getChildFragmentManager(), "电子卡");
            }
        });
        plusRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MineRoutes.MINE_VIPPLUSRIGHT)
                        .navigation();
            }
        });
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "11").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        getData();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (isFirstLoad) {
            getData();
        }
    }

    AdPresenter adPresenter;

    private void buildHeadView(final List<AdModel> bannerimgs) {
        List<ColorInfo> colorList = new ArrayList<>();

        ImageNetAdapter imageLoader;
        int count;
        if (bannerimgs != null && bannerimgs.size() > 0) {
            colorList.clear();
            count = bannerimgs.size();
            for (int j = 0; j < bannerimgs.size(); j++) {
                ColorInfo info = new ColorInfo();
                info.setImgUrl(bannerimgs.get(j).photoUrls);
                colorList.add(info);

            }
            for (int j = 0; j < colorList.size(); j++) {
                try {
                    colorList.get(j).setPerfectColor(Color.parseColor(bannerimgs.get(j).colorValue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imageLoader = new ImageNetAdapter(new SimpleArrayListBuilder<String>().putList(bannerimgs, new ObjectIteraor<AdModel>() {
                @Override
                public Object getDesObj(AdModel adModel) {
                    return adModel.photoUrls;
                }
            }), TransformUtil.dp2px(mContext, 10f), colorList);
            mallBanner.setAdapter(imageLoader)
                    .setIndicator(new RectangleIndicator(mContext))
                    .setIndicatorGravity(IndicatorConfig.Direction.CENTER);//设置指示器位置（左，中，右）
            mallBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    AdModel adModel = bannerimgs.get(position);
                    MARouterUtils.passToTarget(mContext, adModel);
                }

                @Override
                public void onBannerChanged(int position) {

                }
            });
            //System.out.println("修改背景版333");
//            mallBanner.setIndicator(new CircleIndicator(mContext));
            mallBanner.stop();
            mallBanner.start();

        }
    }

    private int mMargin;
    private int mCornerRadius;
    private List<IndexMenu> list = new ArrayList<>();

    private void buildFucs() {
        vipFucLL.removeAllViews();
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 5);
        }
        list.clear();
        list.add(new IndexMenu("我的寄存", "R.drawable.vip_jc"));
        list.add(new IndexMenu("门店消费", "R.drawable.vip_mdxf"));
        list.add(new IndexMenu("我的预约", "R.drawable.vip_yy"));
        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {

        } else {
            list.add(new IndexMenu("我的问答", "R.drawable.vip_wd"));
        }
        list.add(new IndexMenu("收货地址", "R.drawable.vip_sh"));
        list.add(new IndexMenu("我的拼团", "R.drawable.vip_pt"));
        list.add(new IndexMenu("我的砍价", "R.drawable.vip_kj"));
        list.add(new IndexMenu("收藏夹", "R.drawable.vip_scj"));
        list.add(new IndexMenu("意见反馈", "R.drawable.vip_yjfk"));
        list.add(new IndexMenu("我的投票", "R.drawable.vip_vote"));
        list.add(new IndexMenu("母婴顾问", "R.drawable.vip_yrgw_on"));
        list.add(new IndexMenu("活动报名", "R.drawable.vip_enlist"));
        list.add(new IndexMenu("领奖中心", "R.drawable.vip_award_center"));
        if (inviteAct != null) {
            list.add(new IndexMenu("邀请有礼", "R.drawable.vip_invite"));
        }
        if (tokerWorkerInfoModel != null) {
            if (tokerWorkerInfoModel.tokerWorker != null) {
                list.add(new IndexMenu("企业助手", "R.drawable.vip_qwx"));
            }
        }
        TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
        if (tongLianMemberData==null||!tongLianMemberData.memberInfo.isPhoneChecked) {//没绑定
            list.add(new IndexMenu("支付认证", "R.drawable.vip_pay_check"));
        }
        list.add(new IndexMenu("我的收益", "R.drawable.vip_get_result"));
        for (int i = 0; i < list.size(); i++) {
            int row = 5;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            vipFucLL.setRowCount(5);
            int w = (int) ((ScreenUtils.getScreenWidth(mContext) - 2 * TransformUtil.dp2px(mContext, 10)) / row);
            params.width = w;
            View imageparent = LayoutInflater.from(mContext).inflate(R.layout.mine_fragment_mine_vip_fuc, vipFucLL, false);
            final IndexMenu indexMenu = list.get(i);

            ImageView ivCategory;
            ImageView ivCategoryOff;
            TextView tvCategory;
            ivCategory = (ImageView) imageparent.findViewById(R.id.iv_category);
            ivCategoryOff = (ImageView) imageparent.findViewById(R.id.iv_category_off);
            tvCategory = (TextView) imageparent.findViewById(R.id.tv_category);

            if (tokerWorkerInfoModel != null) {
                if ("母婴顾问".equals(indexMenu.name)) {
                    //判断是否绑定母婴顾问
                    if (isBinding) {
                        ivCategoryOff.setVisibility(View.GONE);
                    } else {
                        ivCategoryOff.setVisibility(View.VISIBLE);
                    }
                }
            }
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(indexMenu.getImageRes())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(ivCategory);
            tvCategory.setText(indexMenu.name);
            imageparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outClick(indexMenu.name, indexMenu);
                }
            });
            vipFucLL.addView(imageparent, params);
        }

    }

    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("企业助手".equals(function)) {
            try {
                ARouter.getInstance().build(MineRoutes.MINE_QIYEWEIXIN)
                        .withString("tokerWorkerId", tokerWorkerInfoModel.tokerWorker.id)
                        .withObject("tokerWorker", tokerWorkerInfoModel.tokerWorker)
                        .navigation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("邀请有礼".equals(function)) {
            ARouter.getInstance().build(MineRoutes.MINE_INVITE)
                    .navigation();
        }

        if ("积分商城".equals(function)) {

        }

        if ("我的寄存".equals(function)) {
            ARouter.getInstance().build(MineRoutes.MINE_VIPDEPOSIT)
                    .navigation();
        }

        if ("门店消费".equals(function)) {
            ARouter.getInstance().build(MineRoutes.MINE_VIPCONSUME_HISTORY)
                    .navigation();
        }

        if ("我的预约".equals(function)) {
            /*ARouter.getInstance().build(MineRoutes.MINE_ORDER_SUB_HISTORY_LIST)
                    .navigation();*/
            //修改新预约路径
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_APPOINTMENTLIST)
                    .navigation();
            return;
        }
        if ("收货地址".equals(function)) {
            ARouter.getInstance().build(ServiceRoutes.SERVICE_ADDRESS_LIST)
                    .navigation();
        }


        if ("我的问答".equals(function)) {
            ARouter.getInstance().build(FaqRoutes.FAQ_QUESTION_ANSWERS)
                    .navigation();
        }
        if ("收藏夹".equals(function)) {
            ARouter.getInstance().build(MineRoutes.MINE_COLLECTIONS).navigation();
        }
        if ("意见反馈".equals(function)) {
            ARouter.getInstance().build(MineRoutes.MINE_FEEDBACK).navigation();
        }
        if ("我的拼团".equals(function)) {
//           showToast("暂未开放");
            ARouter.getInstance().build(DiscountRoutes.DIS_MYASSEMBLEACTIVITY).navigation();
        }
        if ("我的砍价".equals(function)) {
//            showToast("暂未开放");
            ARouter.getInstance().build(DiscountRoutes.DIS_MYKICKACTIVITY).navigation();
        }

        if ("我的投票".equals(function)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_VOTELIST)
                    .navigation();
            return;
        }

        if ("活动报名".equals(function)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_ENLIST)
                    .navigation();
            return;
        }

        if ("领奖中心".equals(function)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_AWARD_CENTER)
                    .navigation();
            return;
        }
        if ("我的收益".equals(function)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_VIPPROFIT)
                    .navigation();
            return;
        }

        if ("母婴顾问".equals(function)) {
            if (isBinding) {
                String bindqiyeweixin = null;
                try {
                    bindqiyeweixin = tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.workWxUrl;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (qiYeWeXinKey != null && "1".equals(qiYeWeXinKey.workerOnOff) && !TextUtils.isEmpty(bindqiyeweixin)) {
                    ARouter.getInstance().build(MineRoutes.MINE_WORKS).navigation();
                } else {
                    if (myAdviserDialog != null) {
                        myAdviserDialog.show(getChildFragmentManager(), "母婴顾问");
                    }
                }

            } else {
                if (qiYeWeXinKey != null && "1".equals(qiYeWeXinKey.workerOnOff)) {

                    ARouter.getInstance().build(MineRoutes.MINE_RECOMMANDWORKS).navigation();
                } else {

                    ARouter.getInstance().build(MineRoutes.MINE_BINDINGADVISER).navigation();
                }
            }
        }
        if ("支付认证".equals(function)) {
            TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
            if (tongLianMemberData==null||!tongLianMemberData.memberInfo.isPhoneChecked) {//没绑定
                if (tongLianPhoneBindDialog == null) {
                    tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance();
                }
                tongLianPhoneBindDialog.show(getChildFragmentManager(), "手机绑定");
                tongLianPhoneBindDialog.setOnDialogAgreeClickListener(new TongLianPhoneBindDialog.OnDialogAgreeClickListener() {
                    @Override
                    public void onDialogAgree() {

                    }
                });
            } else {
                showToast("您已认证成功~");
            }
        }
//        if ("测试跳转".equals(function)) {
//            ARouter.getInstance().
//                    build(IndexRoutes.INDEX_WEBVIEWSINGLE).withString("scheme","Lottery")
//                    .navigation();
//        }

    }

    @Override
    protected void onCreate() {
        mPresenter = new MinePresenter(mContext, this);
        plusPresenter = new PlusPresenter(mContext, this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    PlusPresenter plusPresenter;

    @Override
    public void getData() {
        super.getData();
        tokerWorkerInfoModel = null;
        zxingScanPresenter.getTokerWorkerInfo();
        mPresenter.getUserInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("type", "0");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        mPresenter.getUserFans(map);
        //mPresenter.getOrderInfo();
        mPresenter.getOrderNum();
        //mPresenter.getCouponInfo();
        invitePresenter.getMerchantInvite(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION, "invite_10002")
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        plusPresenter.checkPlus(new SimpleHashMapBuilder<String, Object>()
                .puts("phoneNum", SpUtils.getValue(mContext, SpKey.PHONE))
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        mPresenter.getVipInfo(new SimpleHashMapBuilder<String, Object>());
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "11").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核就不展示会员权益
            plusRight.setVisibility(View.GONE);
        } else {
            plusRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNetErr() {
    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_avatar_LL) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_PERSONAL_INFO)
                    .navigation();
        }
//        else if (v.getId() == R.id.section_settings) {
//            ARouter.getInstance().build(MineRoutes.MINE_SETTINGS).navigation();
//        }
        else if (v.getId() == R.id.passSetting) {
            ARouter.getInstance().build(MineRoutes.MINE_SETTINGS).navigation();
        } else if (v.getId() == R.id.passMessage) {
            ARouter.getInstance()
                    .build(AppRoutes.APP_MESSAGE)
                    .navigation();
        } else if (v.getId() == R.id.llh1) {
            //跳转账户详情
            ARouter.getInstance().build(FaqRoutes.ACCOUNTCENTER)
                    .navigation();
        } else if (v.getId() == R.id.llh2) {
            ARouter.getInstance().build(MineRoutes.MINE_VIPINTEGRALACTIVITY)
                    .navigation();

        } else if (v.getId() == R.id.llh3) {
            ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST)
                    .navigation();
        } else if (v.getId() == R.id.llh4) {
            if (vipCardSize) {
                ARouter.getInstance().build(MineRoutes.MINE_VIPCARDBALANCEACTIVITY)
                        .navigation();
            } else {
                showToast("您暂无储值卡");
            }


        } else if (v.getId() == R.id.lly1) {
            ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                    .withString("type", "0")
                    .navigation();
        } else if (v.getId() == R.id.lly2) {
            ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                    .withString("type", "1")
                    .navigation();
        } else if (v.getId() == R.id.lly3) {
            ARouter.getInstance()
                    .build(CityRoutes.CITY_FANDETAIL)
                    .withString("type", 0 + "")
                    .withString("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "")
                    .withString("searchMemberType", 1 + "")
                    .navigation();
        } else if (v.getId() == R.id.passtodaituihuoLL) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ORDERBACK)
                    .navigation();
        } else if (v.getId() == R.id.con_order_all) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "“全部”tab点击量");
            MobclickAgent.onEvent(mContext, "event2APPMineAllTabClick", nokmap);
            ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                    .withString("type", Constants.ORDER_TYPE_ALL)
                    .navigation();
        } else if (v.getId() == R.id.passtodaifukuanLL) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "“待付款”tab点击量");
            MobclickAgent.onEvent(mContext, "event2APPMineNoPayTabClick", nokmap);
            ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                    .withString("type", Constants.ORDER_TYPE_TO_BE_PAID)
                    .navigation();
        } else if (v.getId() == R.id.passtodaishiyongLL) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "“待自提/待配送”tab点击量");
            MobclickAgent.onEvent(mContext, "event2APPMineNoServiceTabClick", nokmap);
            ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                    .withString("type", Constants.ORDER_TYPE_USE)
                    .navigation();
        } else if (v.getId() == R.id.passtodaidianpingLL) {
            ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                    .withString("type", Constants.ORDER_TYPE_COMMENT)
                    .navigation();
        } else if (v.getId() == R.id.passZxing) {
            ARouter.getInstance().build(MineRoutes.MINE_ZXING_SCAN)
                    .navigation();
        }
    }


    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {
        //System.out.println("我的性别是啥:" + userInfoModel.getMemberSex());
        if (userInfoModel.getMemberSex() == 2) {
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(userInfoModel.getFaceUrl())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(ivAvatar);
        } else {
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(userInfoModel.getFaceUrl())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(ivAvatar);
        }

        boolean empty = TextUtils.isEmpty(userInfoModel.getBadgeId());
        int visibility = empty ? View.GONE : View.VISIBLE;
        mUserBadge.setVisibility(visibility);
        mUserBadgeName.setVisibility(visibility);
        mUserBadgeName.setText(userInfoModel.getBabyName());
        if (!empty) {
            ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
            if (0 == userInfoModel.getBadgeType()) {
                //达人认证
                mUserBadge.setImageResource(R.drawable.icon_user_certification);
                mUserBadgeName.setTextColor(Color.parseColor("#F91362"));
                shapeDrawableBuilder
                        .setSolidColor(0)
                        .setGradientColors(Color.parseColor("#1AF91362"), Color.parseColor("#1AFC4D53"))
                        .intoBackground();
            }
            if (1 == userInfoModel.getBadgeType()) {
                mUserBadge.setImageResource(R.drawable.icon_user_official_certification);
                mUserBadgeName.setTextColor(Color.parseColor("#3488FF"));
                shapeDrawableBuilder.clearGradientColors();
                shapeDrawableBuilder
                        .setSolidColor(Color.parseColor("#1A4E96FE"))
                        .intoBackground();

            }
        }

        tvNickname.setText(userInfoModel.getNickname());
        tvStatus.setText(userInfoModel.getDateContent());
        //System.out.println("ggggg我自己:" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        if (userInfoModel.getStatus() == -1) {
            //System.out.println("gggg2");
            tvStatus.setText("资料越全面，服务更贴心");
        }
        hdye.setText(userInfoModel.getBalance() + "");


//        mTvIncome.setText(userInfoModel.getIncome());
//        mTvBalance.setText(userInfoModel.getBalance());

    }

    @Override
    public void onGetOrderInfoSuccess(OrderInfo orderInfo) {
        if (noDaiFu == null) {
            noDaiFu = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaifukuan);
        }
        if (noDaiYong == null) {
            noDaiYong = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaishiyong);
        }
        if (noDaiPing == null) {
            noDaiPing = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaidianping);
        }
        if (norefunds == null) {
            norefunds = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaituihuo);
        }
        if (orderInfo.refundsNum == 0) {
            norefunds.hide(false);
        } else {
            norefunds.setBadgeText(orderInfo.refundsNum + "");
        }
        if (orderInfo.noPayNum == 0) {
            noDaiFu.hide(false);
        } else {
            noDaiFu.setBadgeText(orderInfo.noPayNum + "");
        }
        if (orderInfo.noUserNum == 0) {
            noDaiYong.hide(false);
        } else {
            noDaiYong.setBadgeText(orderInfo.noUserNum + "");
        }
        if (orderInfo.noCommentNum == 0) {
            noDaiPing.hide(false);
        } else {
            noDaiPing.setBadgeText(orderInfo.noCommentNum + "");
        }
    }

    @Override
    public void onGetUserFanSucess(MineFans mineFans) {
        if (mineFans != null) {
            gz.setText(mineFans.friendNum + "");
            fs.setText(mineFans.fansNum + "");
            tz.setText(mineFans.postingNum + "");
        }
    }

    CouponInfo couponInfo;

    @Override
    public void onGetCouponSucess(CouponInfo couponInfo) {
        this.couponInfo = couponInfo;
        if (couponInfo != null) {
            tvYhq.setText(couponInfo.couponCount + "");
        } else {
            tvYhq.setText("0");
        }
        mPresenter.getVipInfo(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    public void onGetOrderNumSuccess(OrderNum orderNum) {
        if (noDaiFu == null) {
            noDaiFu = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaifukuan);
        }
        if (noDaiYong == null) {
            noDaiYong = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaishiyong);
        }
        if (noDaiPing == null) {
            noDaiPing = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaidianping);
        }
        if (norefunds == null) {
            norefunds = new QBadgeView(mActivity)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .bindTarget(passtodaituihuo);
        }
        if ("0".equals(orderNum.inRefundCount)) {
            norefunds.hide(false);
        } else {
            norefunds.setBadgeText(orderNum.inRefundCount + "");
        }
        if ("0".equals(orderNum.noPayCount)) {
            noDaiFu.hide(false);
        } else {
            noDaiFu.setBadgeText(orderNum.noPayCount + "");
        }
        if ("0".equals(orderNum.noTakeCount)) {
            noDaiYong.hide(false);
        } else {
            noDaiYong.setBadgeText(orderNum.noTakeCount + "");
        }
        if ("0".equals(orderNum.noCommentCount)) {
            noDaiPing.hide(false);
        } else {
            noDaiPing.setBadgeText(orderNum.noCommentCount + "");
        }
    }

    VipInfo vipInfo;
    List<VipCard> vipCards;

    @Override
    public void onVipInfoSuccess(VipInfo vipInfo) {
        this.vipInfo = vipInfo;
        if (vipInfo != null) {
            tvChuzhi.setText("¥" + (vipInfo.yeTotal + "").split("\\.")[0]);
            tvJf.setText((vipInfo.jfTotal + "").split("\\.")[0]);
            tvYhq.setText(vipInfo.yhCount + "");
            if (couponInfo != null) {
                tvYhq.setText(vipInfo.yhCount + "");
            }
        } else {
            tvChuzhi.setText("¥0");
            tvJf.setText("0");
            tvYhq.setText("0");
        }
        mPresenter.getVipCards(new SimpleHashMapBuilder<String, Object>());

    }

    @Override
    public void onVipCardSuccess(List<VipCard> vipCards) {
        if (vipCards != null && vipCards.size() > 0) {
            vipCardSize = true;
            tvChuzhiCount.setText("储值（" + vipCards.size() + "张)");
        } else {
            vipCardSize = false;
            tvChuzhiCount.setText("储值");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (passMessage != null) {
            CheckUtils.checkMessageCount(mContext, passMessage);
        }
        //System.out.println("重新进入");
//        if (mContentView != null && mContext != null) {
//            mPresenter.getOrderNum();
//            zxingScanPresenter.getTokerWorkerInfo();
//        }
    }

    @Override
    public void onRefresh() {
        if (mContentView != null && mContext != null) {
            getData();
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.setRefreshing(false);
    }

    private void initView() {
        layoutRefresh = (SwipeRefreshLayout) findViewById(R.id.layout_refresh);
        mUserBadge = findViewById(R.id.iv_user_badge);
        mUserBadgeName = findViewById(R.id.stv_user_badgeName);
        layoutRefresh.setOnRefreshListener(this);
        vipBgImg = (ImageView) findViewById(R.id.vipBgImg);
        passSetting = (ImageView) findViewById(R.id.passSetting);
        passMessage = (ImageView) findViewById(R.id.passMessage);
        passZxing = (ImageView) findViewById(R.id.passZxing);
        ivAvatarLL = (ConstraintLayout) findViewById(R.id.iv_avatar_LL);
        ivAvatar = (CornerImageView) findViewById(R.id.iv_avatar);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        postLL = (LinearLayout) findViewById(R.id.postLL);
        lly1 = (LinearLayout) findViewById(R.id.lly1);
        gz = (TextView) findViewById(R.id.gz);
        lly2 = (LinearLayout) findViewById(R.id.lly2);
        fs = (TextView) findViewById(R.id.fs);
        lly3 = (LinearLayout) findViewById(R.id.lly3);
        tz = (TextView) findViewById(R.id.tz);
        tmpBlock = (LinearLayout) findViewById(R.id.tmpBlock);
        moneyLL = (LinearLayout) findViewById(R.id.moneyLL);
        llh2 = (LinearLayout) findViewById(R.id.llh2);
        hdye = (TextView) findViewById(R.id.hdye);
        llh3 = (LinearLayout) findViewById(R.id.llh3);
        tvYhq = (TextView) findViewById(R.id.tv_yhq);
        llh1 = (LinearLayout) findViewById(R.id.llh1);
        conOrder = (ConstraintLayout) findViewById(R.id.con_order);
        conOrderAll = (ConstraintLayout) findViewById(R.id.con_order_all);
        orderTitle = (TextView) findViewById(R.id.order_title);
        llOrderType = (LinearLayout) findViewById(R.id.ll_order_type);
        passtodaifukuanLL = (LinearLayout) findViewById(R.id.passtodaifukuanLL);
        passtodaifukuan = (ImageView) findViewById(R.id.passtodaifukuan);
        passtodaishiyongLL = (LinearLayout) findViewById(R.id.passtodaishiyongLL);
        passtodaishiyong = (ImageView) findViewById(R.id.passtodaishiyong);
        passtodaidianpingLL = (LinearLayout) findViewById(R.id.passtodaidianpingLL);
        passtodaidianping = (ImageView) findViewById(R.id.passtodaidianping);
        passtodaituihuoLL = (LinearLayout) findViewById(R.id.passtodaituihuoLL);
        passtodaituihuo = (ImageView) findViewById(R.id.passtodaituihuo);
        mallBannerLL = (LinearLayout) findViewById(R.id.mall_bannerLL);
        mallBanner = (Banner) findViewById(R.id.mall_banner);
        vipFucLL = (GridLayout) findViewById(R.id.vip_fucLL);
        llh4 = (LinearLayout) findViewById(R.id.llh4);
        tvJf = (TextView) findViewById(R.id.tv_jf);
        tvChuzhi = (TextView) findViewById(R.id.tv_chuzhi);
        tvChuzhiCount = (ImageTextView) findViewById(R.id.tv_chuzhi_count);
        passElcCard = (ImageView) findViewById(R.id.passElcCard);
        plusRight = (ConstraintLayout) findViewById(R.id.plusRight);
        plusCrown = (ImageView) findViewById(R.id.plusCrown);
        plusText = (TextView) findViewById(R.id.plusText);
        plusBtn = (TextView) findViewById(R.id.plusBtn);


        llh1.setOnClickListener(this);
        llh2.setOnClickListener(this);
        llh3.setOnClickListener(this);
        llh4.setOnClickListener(this);
        conOrderAll.setOnClickListener(this);
        passtodaifukuanLL.setOnClickListener(this);
        passtodaishiyongLL.setOnClickListener(this);
        passtodaidianpingLL.setOnClickListener(this);
        passtodaituihuoLL.setOnClickListener(this);
        passSetting.setOnClickListener(this);
        passMessage.setOnClickListener(this);
        passZxing.setOnClickListener(this);
        lly1.setOnClickListener(this);
        lly2.setOnClickListener(this);
        lly3.setOnClickListener(this);
        ivAvatarLL.setOnClickListener(this);
        zxingScanPresenter = new ZxingScanPresenter(mContext, this);
        adPresenter = new AdPresenter(mContext, this);
        myAdviserDialog = MyAdviserDialog.newInstance();
        zxingScanPresenter.getTokerWorkerInfo();
        buildFucs();
        passElcCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElcCardDialogView.newInstance().show(getChildFragmentManager(), "电子卡");
            }
        });
        plusRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MineRoutes.MINE_VIPPLUSRIGHT)
                        .navigation();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserInfoMsg msg) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserBindInfo(UpdateUserBindInfoMsg msg) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTab(UpdateMessageInfo msg) {

        CheckUtils.checkMessageCount(mContext, passMessage);
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        mallBannerLL.setVisibility(View.GONE);
        if (adModels != null && adModels.size() > 0) {
            mallBannerLL.setVisibility(View.VISIBLE);
            buildHeadView(adModels);

        }
    }

    @Override
    public void onGetCodeInfoSuccess(ZxingReferralCodeModel model) {

    }

    @Override
    public void onBindingSuccess(String result) {

    }

    TokerWorkerInfoModel tokerWorkerInfoModel;

    @Override
    public void onGetTokerWorkerInfoSuccess(TokerWorkerInfoModel model) {
        if (model != null) {
            tokerWorkerInfoModel = model;
            if (model.bindingList != null && model.bindingList.size() > 0 && model.bindingList.get(0).bindingTokerWorker != null) {
                isBinding = true;//绑定了
                if (model.bindingList.get(0) != null) {
                    if (model != null) {
                        if (myAdviserDialog != null) {
                            myAdviserDialog.setDate(model);
                        }
                    }
                    //zxingScanPresenter.getCodeInfo(model.bindingList.get(0).bindingTokerWorker.referralCode + "");
                    if (model.tokerWorker != null) {
                        SpUtils.store(mContext, SpKey.GETTOKEN, model.tokerWorker.referralCode);
                        SpUtils.store(mContext, "referralCode", model.tokerWorker.referralCode);
                    }
                }
            } else {// 没绑定
                isBinding = false;
            }
            qiYeWeiXinPresenter.getWeiXinKey(new SimpleHashMapBuilder<String, Object>(), null);
        }
    }

    @Override
    public void ticketCoupon() {

    }

    @Override
    public void onSucessCheckPlus(boolean isplus) {
        SpUtils.store(mContext, SpKey.PLUSSTATUS, isplus);

    }

    @Override
    public void onSuccessSubMerchantInvite(String result) {

    }

    @Override
    public void onSuccessGetMerchantInvite(InviteAct inviteAct) {
        this.inviteAct = inviteAct;
        buildFucs();
    }

    @Override
    public void onSuccessGetMerchantInviteDetailJoinList(List<InviteJoinInfo> inviteJoinInfos) {

    }

    QiYeWeXinKey qiYeWeXinKey;
    TongLianPhoneBindDialog tongLianPhoneBindDialog;

    @Override
    public void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey) {
        this.qiYeWeXinKey = qiYeWeXinKey;
        buildFucs();

    }

    @Override
    public void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins) {

    }

    @Override
    public void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins) {

    }

    @Override
    public void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }

    @Override
    public void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }
}