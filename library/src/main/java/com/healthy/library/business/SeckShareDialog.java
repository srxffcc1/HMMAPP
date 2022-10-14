package com.healthy.library.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.SeckShareDialogContract;
import com.healthy.library.interfaces.OnLabelItemWidthListener;
import com.healthy.library.model.ActivityModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.EnlistActivityModel;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.Goods2ShopModelKick;
import com.healthy.library.model.Goods2ShopModelPin;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsModel;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.ShopModel;
import com.healthy.library.presenter.SeckShareDialogPresenter;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StackLabel;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

public class SeckShareDialog extends BaseDialogFragment implements SeckShareDialogContract.View {

    private static final String TAG = SeckShareDialog.class.getSimpleName();
    private CardView dialogCardView;
    private LinearLayout shareViewLiner;
    private LinearLayout share_weixinLiner;
    private LinearLayout share_friendLiner;
    private LinearLayout share_downLiner;
    private static final int RC_PERMISSION = 45;
    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 1服务分享 2标品分享（2021/10/13 异业公用） 3活动分享（2021/10/13 异业公用） 4投票 -> 拉票分享
     * 5 报名 6邀请有礼 7服务预约分享 8直播分享 9签到分享
     */
    private int type = 0;
    /**
     * 行业类型 0 默认普通 1 异业行业
     */
    private int industyType = 0;
    /**
     * 0普通 1砍价 2拼团 3秒杀 4新人专享 5 积分商品 (type == 3 marketingType == 6为领劵内容)(type==8 marketingType==7是直播预告分享)
     */
    private int marketingType = 0;
    private ShopDetailModel detailModel;
    private Bitmap bitmap;//活动分享的bitmap
    private Bitmap bitmapToker;//拓客分享的bitmap
    private GoodsDetail goodsDetail;//标品的goodsDetail
    private GoodsModel goodsModel;//服务的goodsDetail
    private ShopModel shopModel;//服务的ShopModel
    private Goods2DetailKick goods2DetailKick;//标品砍价详情
    private Goods2DetailPin goods2DetailPin;//标品拼团详情
    private Goods2ShopModelKick goods2ShopModelKick;//服务砍价详情
    private Goods2ShopModelPin goods2ShopModelPin;//服务拼团详情
    private SeckShareDialogPresenter presenter;
    private ImageView appletsImg;
    String assembleId = "";
    String bargainId = "";
    public String visitShopId;
    public String groupId;

    public SeckShareDialog setShopDetailModel(ShopDetailModel shopDetailModel) {
        this.shopDetailModel = shopDetailModel;
        return this;
    }

    public ShopDetailModel shopDetailModel;
    public SeckShareDialog setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public SeckShareDialog setMerchantShopId(String merchantId, String visitShopId) {
        this.merchantId = merchantId;
        this.visitShopId = visitShopId;
        return this;
    }

    private String mPostImgUrl;

    public void setmPostImgUrl(String mPostImgUrl) {
        this.mPostImgUrl = mPostImgUrl;
    }

    String merchantId;
    public String mShopLogo; //异业情况为异业商家logo 反之品牌logo
    private String mMerchantLogo;//异业情况下当前为商家合伙人logo（品牌logo）
    private Object mActivityObject;
    private Map<String, Object> mMap = new HashMap();
    private SimpleHashMapBuilder<String, String> mExtraMap = new SimpleHashMapBuilder();
    private StringBuilder mStringBuilder = new StringBuilder();
    private Bitmap mBase64Bitmap;
    //小程序地址
    private String mPath;
    private String mBase64Data;
    private String mGoodsId;
    private String mShareBackgroundUrl;
    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(LibApplication.getAppContext(), "分享成功！", Toast.LENGTH_LONG).show();
            getDialog().dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(LibApplication.getAppContext(), "分享失败！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(LibApplication.getAppContext(), "取消分享！", Toast.LENGTH_LONG).show();
        }
    };
    private int mHeightPixels;
    private int mResourcesUmImg;
    private int mWidthPixels;
    private ImageView topView;
    /*--------------- 公用类型字段 START --------------*/
    private String mShareTitle;
    private String mActivityId;
    private String mActivityTitle;
    private String personId;
    /*--------------- 公用类型字段 END --------------*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(getActivity(), mPermissions)) {
                Toast.makeText(getActivity(), "需要同意存储权限才能保存图片", Toast.LENGTH_LONG).show();
                PermissionUtils.requestPermissions(getActivity(), RC_PERMISSION, mPermissions);
            }
        }
    }

    public static SeckShareDialog newInstance() {
        Bundle args = new Bundle();
        SeckShareDialog fragment = new SeckShareDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public SeckShareDialog setBitmap(int type, Bitmap bitmap) {
        this.type = type;
        this.bitmap = bitmap;
        return this;
    }

    public SeckShareDialog setExtraMap(@NonNull SimpleHashMapBuilder<String, String> map) {
        this.mExtraMap = map;
        if(mExtraMap!=null){//参数补全
            if(mExtraMap.get("merchantId")!=null){
                merchantId=mExtraMap.get("merchantId").toString();
            }
            if(mExtraMap.get("shopId")!=null){
                visitShopId=mExtraMap.get("shopId").toString();
            }
        }
        return this;
    }

    public SeckShareDialog setGoodsKickPin(Goods2DetailKick goods2DetailKick, Goods2DetailPin goods2DetailPin) {
        this.goods2DetailKick = goods2DetailKick;
        this.goods2DetailPin = goods2DetailPin;
        if (goods2DetailKick != null) {
            bargainId = goods2DetailKick.bargainInfoDTO.id + "";
        }
        if (goods2DetailPin != null) {
            assembleId = goods2DetailPin.assembleDTO.id + "";
        }
        return this;
    }


    public SeckShareDialog setGoodsDetail(int type, int marketingType, GoodsDetail goodsDetail, String visitShopId, String merchantId) {
        this.type = type;
        this.visitShopId = visitShopId;
        this.merchantId = merchantId;
        this.marketingType = marketingType;
        this.goodsDetail = goodsDetail;
        return this;
    }

    /**
     * 限时活动 (秒杀、拼团、砍价、领劵）3活动分享
     * 4投票 -> 拉票分享 5 报名  6邀请有礼 7服务预约分享 8直播分享 9异业商品详情分享
     *
     * @param type
     * @param marketingType
     * @param object
     * @return
     */
    public SeckShareDialog setActivityDialog(int type, int marketingType, Object object) {
        this.type = type;
        this.marketingType = marketingType;
        this.mActivityObject = object;
        return this;
    }

    /**
     * 设置行业类型
     *
     * @param industyType 0 默认普通 1 异业行业
     * @return
     */
    public SeckShareDialog setIndustyType(int industyType) {
        this.industyType = industyType;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (inflater == null) {
            dismiss();
        }
        final View view = inflater.inflate(R.layout.dis_seckill_share_dialog_layout, null);
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(getActivity(), SpKey.CHOSE_MC);
        }
        initView(view);
        presenter = new SeckShareDialogPresenter(getActivity(), this);
        // 活动列表
        if (type == 3) {
            if (industyType == 1) {
                mExtraMap.puts("scheme", "SecondActivityList").puts("type", String.valueOf(marketingType));
                mPath = getNormalSurlWithScheme("SecondActivityList", mExtraMap).split("\\?")[0];
            } else {
                mExtraMap.puts("scheme", "ActivityList").puts("type", String.valueOf(marketingType));
                mPath = getNormalSurlWithScheme("ActivityList", mExtraMap).split("\\?")[0];
            }
        } else if (type == 4) {
            //进入小程序投票页面
//            mPath = "packageB/pages/voteDetail/index";
            mExtraMap.put("scheme", "VoteDetail");
            mPath = getNormalSurlWithScheme("VoteDetail", mExtraMap).split("\\?")[0];
        } else if (type == 5) {
            //进入小程序报名页面 packageA/pages/enroll/index?enlistActivityId=
            mPath = "packageA/pages/enroll/index";
        } else if (type == 6) {
            /** 邀请有礼 */
            mExtraMap.put("scheme", "GiftsForSharing");
            mPath = getNormalSurlWithSchemeNoRel("GiftsForSharing", mExtraMap).split("\\?")[0];
        } else if (type == 7) {
            /** 服务预约 */
            mExtraMap.put("scheme", "AppointmentDetail");
            mPath = getNormalSurlWithScheme("AppointmentDetail", mExtraMap).split("\\?")[0];
        } else if (type == 8) {
            /** 直播分享 */
            mExtraMap.put("scheme", "LiveStream");
            mPath = getNormalSurlWithScheme("LiveStream", mExtraMap).split("\\?")[0];
        } else if (type == 2 && industyType == 1) {
            /** 异业分享 */
            mExtraMap.puts("scheme", "SecondGoodsDetail");
            mPath = getNormalSurlWithScheme("SecondGoodsDetail", mExtraMap).split("\\?")[0];
        } else if (type == 9) {
            /** 签到分享 */
            mExtraMap.puts("scheme", "SignInPage");
            mPath = getNormalSurlWithScheme("SignInPage", mExtraMap).split("\\?")[0];
        } else {
            //mPath = "pages/home/goodsDetail/index/index";
            mExtraMap.puts("scheme", "GoodsDetail");
            mPath = getNormalSurlWithScheme("GoodsDetail", mExtraMap).split("\\?")[0];
        }

        //获取屏幕高度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mWidthPixels = outMetrics.widthPixels;
        mHeightPixels = outMetrics.heightPixels;
        Log.e("SeckShare", "当前屏幕宽度 = " + mWidthPixels + ";当前屏幕高度 = " + mHeightPixels);

        builder.setView(view);
        Dialog result = builder.create();
        if (type == 1 || type == 2) {
            boolean isError = false;
            if (goodsDetail == null) {
                isError = true;
            } else {
                mGoodsId = goodsDetail.id;
                if (TextUtils.isEmpty(mGoodsId)) {
                    isError = true;
                }
            }
            if (isError) {
                result.dismiss();
                return result;
            }
        }
        getData();
        result.setCancelable(true);
        result.setCanceledOnTouchOutside(true);
        Window window = result.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
        initOnClick();
        return result;
    }

    private void initView(View view) {
        dialogCardView = view.findViewById(R.id.dialogCardView);
        shareViewLiner = view.findViewById(R.id.shareViewLiner);
        share_weixinLiner = view.findViewById(R.id.share_weixinLiner);
        share_friendLiner = view.findViewById(R.id.share_friendLiner);
        share_downLiner = view.findViewById(R.id.share_downLiner);
        topView = view.findViewById(R.id.topView);

        //适配宽度小于1080手机 海报太窄小问题
        /*if (mWidthPixels <= 1080) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dialogCardView.getLayoutParams();
            layoutParams.leftMargin = (int) TransformUtil.dp2px(getActivity(), 20);
            layoutParams.rightMargin = (int) TransformUtil.dp2px(getActivity(), 20);
            dialogCardView.setLayoutParams(layoutParams);
        }*/
    }

    public void initOnClick() {

        //分享微信
        share_weixinLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buildWeixin(type != 3 ? viewConversionBitmap(dialogCardView) : null, null, null);
                //积分商品单独做处理
                /*if (5 == marketingType || 6 == type || 4 == type || 7 == type || 8 == type || 2 >= type) {
                    buildWeixin(viewConversionBitmap(dialogCardView));
                    return;
                }*/
                buildWeixin(viewConversionBitmap(dialogCardView));
                /**
                 * 2021-06-19 海报需求重新定义
                 * 1）商品详情链接分享的图片，商品的主图即可；
                 * 2）拼团、砍价、秒杀、领券界面的链接分享，图片已设计完成；如下：
                 */
                //buildWeixin(null, null, null);
            }
        });

        //分享朋友圈
        share_friendLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildWeixinCircel(viewConversionBitmap(dialogCardView));
            }
        });

        //保存图片
        share_downLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "开始存至相册！", Toast.LENGTH_LONG).show();
                if (PermissionUtils.hasPermissions(getActivity(), mPermissions)) {//读写存储权限
                    if (!bitmapHasPic) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                viewSaveToImage(dialogCardView);
                            }
                        }).start();
                    } else {
                        Toast.makeText(getActivity(), "已保存至相册！", Toast.LENGTH_LONG).show();
                    }

                } else {
                    PermissionUtils.requestPermissions(getActivity(), RC_PERMISSION, mPermissions);
                }
            }
        });
    }

    /**
     * 公共设置商品信息
     *
     * @param seckillText
     * @param goodsDate
     * @param goodsPrice
     * @param mPinMoneySingle
     * @param saveMoney
     */
    private void setGoodsData(TextView seckillText, TextView goodsDate, TextView goodsPrice, TextView mPinMoneySingle, TextView saveMoney) {
        if (marketingType == 0) {//普通商品

            System.out.println("普通商品");
            //seckillIcon.setVisibility(View.GONE);
            SpannableString goods_price = new SpannableString("¥" + StringUtils.getResultPrice(goodsDetail.platformPrice + ""));
            goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goods_price);

            BigDecimal savePrice = new BigDecimal(goodsDetail.retailPrice/*platformPrice*/).subtract(new BigDecimal(goodsDetail.platformPrice));
            double v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 0) {
                saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                saveMoney.setVisibility(View.VISIBLE);
            }

            mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.retailPrice + ""));
            mPinMoneySingle.setVisibility(View.VISIBLE);
            if (goodsDetail.getPlusPriceShow() > 0 && industyType != 1) {
                saveMoney.setVisibility(View.GONE);
                seckillText.setVisibility(View.VISIBLE);
                seckillText.setText("plus专享");
                seckillText.setVisibility(View.VISIBLE);
                //seckillIcon.setVisibility(View.VISIBLE);
                double plusPrice = goodsDetail.getPlusPriceShow();
            /*if(plusPrice <= 0 ){
                plusPrice = goodsDetail.platformPrice;
            } else {
                plusPrice = goodsDetail.getPlusPriceShow();
            }*/
                goods_price = new SpannableString("¥" + StringUtils.getResultPrice(plusPrice + ""));
                goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                goodsPrice.setText(goods_price);
                savePrice = new BigDecimal(goodsDetail.goodsChildren.get(0).retailPrice/*platformPrice*/).subtract(new BigDecimal(plusPrice));
                v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (v > 0) {
                    saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                    saveMoney.setVisibility(View.VISIBLE);
                }
                mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.goodsChildren.get(0).retailPrice + ""));
                mPinMoneySingle.setVisibility(View.VISIBLE);
            }
        } else if (marketingType == 1) {//砍价
            //seckillIcon.setVisibility(View.VISIBLE);
            seckillText.setText("疯狂砍价");
            seckillText.setVisibility(View.VISIBLE);
            SpannableString goods_price = new SpannableString("¥" + StringUtils.getResultPrice(goods2DetailKick.bargainInfoDTO.floorPrice + ""));
            goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goods_price);
            BigDecimal savePrice = new BigDecimal(goodsDetail.goodsChildren.get(0).platformPrice).subtract(new BigDecimal(goods2DetailKick.bargainInfoDTO.floorPrice));
            double v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 0) {
                saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                saveMoney.setVisibility(View.VISIBLE);
            }
            mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.goodsChildren.get(0).platformPrice + ""));
            mPinMoneySingle.setVisibility(View.VISIBLE);
            if (goods2DetailKick.bargainInfoDTO.endTime != null && !TextUtils.isEmpty(goods2DetailKick.bargainInfoDTO.endTime)) {
                String[] splited = goods2DetailKick.bargainInfoDTO.endTime.split("\\s+");//2020-09-20 11:10:00
                String[] date = splited[0].split("-");
                String[] time = splited[1].split(":");
                if (date.length >= 2 && time.length >= 2) {
                    String datatime = date[0] + "年" + date[1] + "月" + date[2] + "日" + time[0] + ":" + time[1] + "\r活动结束";
                    goodsDate.setText(datatime);//20
                    goodsDate.setVisibility(View.VISIBLE);// 20年08月20日 23:59活动结束
                }
            }
        } else if (marketingType == 2) {//拼团
            //seckillIcon.setVisibility(View.VISIBLE);
            seckillText.setText("超值拼团");
            seckillText.setVisibility(View.VISIBLE);
            if (goods2DetailPin==null||goods2DetailPin.assembleDTO==null){
                return;
            }
            SpannableString goods_price = new SpannableString("¥" + StringUtils.getResultPrice(goods2DetailPin.assembleDTO.assemblePrice + ""));
            goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goods_price);
            BigDecimal savePrice = new BigDecimal(goodsDetail.goodsChildren.get(0).platformPrice).subtract(new BigDecimal(goods2DetailPin.assembleDTO.assemblePrice));
            double v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 0) {
                saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                saveMoney.setVisibility(View.VISIBLE);
            }
            mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.goodsChildren.get(0).platformPrice + ""));
            mPinMoneySingle.setVisibility(View.VISIBLE);
            if (goods2DetailPin.assembleDTO.endTime != null && !TextUtils.isEmpty(goods2DetailPin.assembleDTO.endTime)) {
                String[] splited = goods2DetailPin.assembleDTO.endTime.split("\\s+");//2020-09-20 11:10:00
                String[] date = splited[0].split("-");
                String[] time = splited[1].split(":");
                if (date.length >= 2 && time.length >= 2) {
                    String datatime = date[0] + "年" + date[1] + "月" + date[2] + "日" + time[0] + ":" + time[1] + "\n活动结束";
                    goodsDate.setText(datatime);//202
                    goodsDate.setVisibility(View.VISIBLE);// 0年08月20日 23:59活动结束
                }
            }
        } else if (marketingType == 3) {//秒杀
            //seckillIcon.setVisibility(View.VISIBLE);
            seckillText.setText("限时秒杀");
            seckillText.setVisibility(View.VISIBLE);
            double actPrice = goodsDetail.marketingGoodsChildren.get(0).marketingPrice;
            SpannableString goods_price = new SpannableString("¥" + StringUtils.getResultPrice(actPrice + ""));
            goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goods_price);
            BigDecimal savePrice = new BigDecimal(goodsDetail.marketingGoodsChildren.get(0).retailPrice).subtract(new BigDecimal(actPrice));
            double v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 0) {
                saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                saveMoney.setVisibility(View.VISIBLE);
            }
            //设置划线价
            mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.marketingGoodsChildren.get(0).retailPrice + ""));
            mPinMoneySingle.setVisibility(View.VISIBLE);
            if (goodsDetail.getMarketingEndTime() != null && !TextUtils.isEmpty(goodsDetail.getMarketingEndTime())) {
                String[] splited = goodsDetail.getMarketingEndTime().split("\\s+");//2020-09-20 11:10:00
                String[] date = splited[0].split("-");
                String[] time = splited[1].split(":");
                if (date.length >= 2 && time.length >= 2) {
                    String datatime = date[0] + "年" + date[1] + "月" + date[2] + "日" + time[0] + ":" + time[1] + "\n活动结束";
                    goodsDate.setText(datatime);//202
                    goodsDate.setVisibility(View.VISIBLE);// 0年08月20日 23:59活动结束
                }
            }
        /*com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(CodeUtils.createImage(getMiniKillSurl(), 650, 650, null))
                .placeholder(R.drawable.hmhg_zxing)
                .error(R.drawable.hmhg_zxing)
                .into(appletsImg);*/
        } else if (marketingType == 4) {//新客
            //seckillIcon.setVisibility(View.VISIBLE);
            seckillText.setText("新客专享");
            seckillText.setVisibility(View.VISIBLE);
            SpannableString goods_price = new SpannableString("¥" + StringUtils.getResultPrice(goodsDetail.marketingGoodsChildren.get(0).marketingPrice + ""));
            goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goods_price);
            double actPrice = goodsDetail.marketingGoodsChildren.get(0).marketingPrice;
            BigDecimal savePrice = new BigDecimal(goodsDetail.marketingGoodsChildren.get(0).retailPrice).subtract(new BigDecimal(actPrice));
            double v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 0) {
                saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                saveMoney.setVisibility(View.VISIBLE);
            }
            //设置划线价
            mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.marketingGoodsChildren.get(0).retailPrice + ""));
            mPinMoneySingle.setVisibility(View.VISIBLE);
            //新客加入活动结束时间
            if (goodsDetail.getMarketingEndTime() != null && !TextUtils.isEmpty(goodsDetail.getMarketingEndTime())) {
                String[] splited = goodsDetail.getMarketingEndTime().split("\\s+");//2020-09-20 11:10:00
                String[] date = splited[0].split("-");
                String[] time = splited[1].split(":");
                if (date.length >= 2 && time.length >= 2) {
                    String datatime = date[0] + "年" + date[1] + "月" + date[2] + "日" + time[0] + ":" + time[1] + "\n活动结束";
                    goodsDate.setText(datatime);//2020年08月
                    goodsDate.setVisibility(View.VISIBLE);// 20日 23:59活动结束
                }
            }
        } else if (marketingType == 5) {//积分商品
            //seckillIcon.setVisibility(View.VISIBLE);
            seckillText.setText("积分商品");
            seckillText.setVisibility(View.VISIBLE);
            String goodsPoint = "";
            if (goodsDetail.marketingGoodsChildren != null && goodsDetail.marketingGoodsChildren.size() > 0) {
                if (goodsDetail.marketingGoodsChildren.get(0).marketingPrice > 0) {
                    goodsPoint = FormatUtils.moneyKeep2Decimals(goodsDetail.marketingGoodsChildren.get(0).pointsPrice) + "积分+¥" + FormatUtils.moneyKeep2Decimals(goodsDetail.marketingGoodsChildren.get(0).marketingPrice);
                } else {
                    goodsPoint = FormatUtils.moneyKeep2Decimals(goodsDetail.marketingGoodsChildren.get(0).pointsPrice) + "积分";
                }
            }
            SpannableString goods_price = new SpannableString(goodsPoint);
            goods_price.setSpan(new AbsoluteSizeSpan(14, true), 0, goodsPoint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goodsPoint);
            //新客加入活动结束时间
            if (goodsDetail.getMarketingEndTime() != null && !TextUtils.isEmpty(goodsDetail.getMarketingEndTime())) {
                String[] splited = goodsDetail.getMarketingEndTime().split("\\s+");//2020-09-20 11:10:00
                String[] date = splited[0].split("-");
                String[] time = splited[1].split(":");
                if (date.length >= 2 && time.length >= 2) {
                    String datatime = date[0] + "年" + date[1] + "月" + date[2] + "日" + time[0] + ":" + time[1] + "\n活动结束";
                    goodsDate.setText(datatime);//2020年08月20日
                    goodsDate.setVisibility(View.VISIBLE);// 23:59活动结束
                }
            }
        } else if (marketingType == 8) {//
            //seckillIcon.setVisibility(View.VISIBLE);

            seckillText.setText("plus专享");
            seckillText.setVisibility(View.VISIBLE);
            double plusPrice = goodsDetail.getPlusPriceShow();
        /*if(plusPrice <= 0){
            plusPrice = goodsDetail.marketingGoodsChildren.get(0).platformPrice;
        } else {
            plusPrice = goodsDetail.getPlusPriceShow();
        }*/
            SpannableString goods_price = new SpannableString("¥" + StringUtils.getResultPrice(plusPrice + ""));
            goods_price.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsPrice.setText(goods_price);
            BigDecimal savePrice = new BigDecimal(goodsDetail.marketingGoodsChildren.get(0).retailPrice/*platformPrice*/).subtract(new BigDecimal(plusPrice));
            double v = savePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 0) {
                saveMoney.setText("立减" + FormatUtils.moneyKeep2Decimals(v) + "元");//BigDecimal转double 四舍五入保留两位小数再去0
                saveMoney.setVisibility(View.VISIBLE);
            }
            //设置划线价
            mPinMoneySingle.setText("¥" + StringUtils.getResultPrice(goodsDetail.marketingGoodsChildren.get(0).retailPrice + ""));
            mPinMoneySingle.setVisibility(View.VISIBLE);
        }
        if(industyType==1){//异业和UI同步去掉立减
            saveMoney.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 商品分享布局样式
     *
     * @param faceUrl
     * @param nickName
     */
    @SuppressLint("StringFormatMatches")
    private void buildGoods(String faceUrl, String nickName) {
        shareViewLiner.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        if (inflater == null) {
            return;
        }
        final View view = inflater.inflate(R.layout.item_share_goods_layout, shareViewLiner, false);
        appletsImg = view.findViewById(R.id.appletsImg);
        //ImageView seckillIcon = view.findViewById(R.id.seckillIcon);
        TextView seckillText = view.findViewById(R.id.seckillText);
        final CornerImageView goodsImg = view.findViewById(R.id.goodsImg);
        TextView mVoteTitle = view.findViewById(R.id.voteTitle);
        CornerImageView userImg = view.findViewById(R.id.userImg);
        TextView goodsTitle = view.findViewById(R.id.goodsTitle);
        final TextView goodsDate = view.findViewById(R.id.goodsDate);
        goodsDate.setVisibility(View.GONE);
        final StackLabel mStackLabel = view.findViewById(R.id.stackLabelView);
        mStackLabel.setGetItemWidth(true);
        TextView goodsPrice = view.findViewById(R.id.goodsPrice);
        /**
         * 划线价 有活动划平台 没活动 划零售
         */
        TextView mPinMoneySingle = view.findViewById(R.id.pinMoneySingle);
        mPinMoneySingle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView saveMoney = view.findViewById(R.id.saveMoney);
        TextView userNickName = view.findViewById(R.id.userNickName);
        ImageView mShopLogoImg = view.findViewById(R.id.shareShopLogoImg);

        final ConstraintLayout mStackLayout = view.findViewById(R.id.share_stack_layout);
        mStackLayout.setVisibility(View.GONE);
        final ImageView mMoreSurpriseTip = view.findViewById(R.id.share_more_surprise_tip);

        ConstraintLayout mEnlistInfoLayout = view.findViewById(R.id.cl_enlist_info);

        /**
         * 设置门店Logo
         */
        mShopLogoImg.setVisibility((TextUtils.isEmpty(mShopLogo) || mShopLogo.equals("null")) ? View.GONE : View.VISIBLE);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mShopLogoImg);
        seckillText.setVisibility(View.GONE);

        //适配大屏幕手机展示样式
        if (mHeightPixels > 1920) {
            goodsImg.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) goodsImg.getLayoutParams();
                linearParams.height = goodsImg.getWidth()/*((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics()))*/;
                goodsImg.setLayoutParams(linearParams);
            });
        }

        if (4 == type) {
            goodsTitle.setVisibility(View.GONE);
            mVoteTitle.setVisibility(View.VISIBLE);
            ActivityModel mActivityModel = (ActivityModel) mActivityObject;
            mShareTitle = String.format(getResources().getString(R.string.share_vote_title), nickName, mActivityModel.getVoteNo(),mActivityModel.getName());
            mVoteTitle.setText(mShareTitle);
            //goodsImg.setImageResource(R.drawable.index_no_fuc2);
            mShareBackgroundUrl = mActivityModel.getBackgroundUrl();
            if (!TextUtils.isEmpty(mActivityModel.getBackgroundUrl())) {
                if (mActivityModel.getBackgroundUrl().contains(",")) {
                    String[] split = mActivityModel.getBackgroundUrl().split(",");
                    mShareBackgroundUrl = split[0];
                } else {
                    mShareBackgroundUrl = mActivityModel.getBackgroundUrl();
                }
            }
            mActivityId = mActivityModel.getId();
            mActivityTitle = mActivityModel.getName();
        } else if (5 == type) {
            mEnlistInfoLayout.setVisibility(View.VISIBLE);
            goodsPrice.setVisibility(View.GONE);
            TextView mTvEnlistTime = view.findViewById(R.id.item_enlist_Time);
            TextView mTvEnlistAddress = view.findViewById(R.id.item_enlist_Address);
            EnlistActivityModel enlistActivityModel = (EnlistActivityModel) mActivityObject;
            mShareTitle = String.format("我是“%s”,我正在参与“%s”活动。", nickName, enlistActivityModel.getName());
            mActivityId = enlistActivityModel.getId();
            mShareBackgroundUrl = enlistActivityModel.getPhotoUrl();
            String mTime = DateUtils.getDateDay(enlistActivityModel.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss") + " - " +
                    DateUtils.getDateDay(enlistActivityModel.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss");
            goodsTitle.setText(enlistActivityModel.getName());
            goodsTitle.setTextSize(20f);
            mTvEnlistTime.setText(mTime);
            mTvEnlistAddress.setText(enlistActivityModel.activityAddress());
        } else if (7 == type) {
            /** 服务预约 */
            TextView mBottomShape = view.findViewById(R.id.tv_bottom_shape);
            view.findViewById(R.id.item_enlist_time_view).setVisibility(View.GONE);
            view.findViewById(R.id.item_enlist_address_view).setVisibility(View.GONE);
            AppointmentMainItemModel model = (AppointmentMainItemModel) mActivityObject;
            mActivityId = String.valueOf(model.getId());
            if (!ListUtil.isEmpty(model.getImgList())) {
                mShareBackgroundUrl = model.getImgList().get(0);
            }

            goodsTitle.setText(model.getName());
            goodsTitle.setTextSize(TransformUtil.sp2px(getContext(), 10f));
            String goodsMoney = "";
            if ("1".equals(model.getPriceType())) {
                goodsMoney = FormatUtils.moneyKeep2Decimals(model.getAttribute().getPrice());
            } else {
                List<AppointmentMainItemModel.AttributeList> attributeList = model.getAttributeList();
                if (!ListUtil.isEmpty(model.getAttributeList())) {
                    double mDefaultPrice = attributeList.get(0).getPrice();
                    goodsMoney = FormatUtils.moneyKeep2Decimals(mDefaultPrice);
                }
            }
            goodsPrice.setVisibility(View.VISIBLE);
            goodsPrice.setText("¥" + goodsMoney);
            int supportRefund = model.getSupportRefund();
            if (supportRefund == 1) {
                saveMoney.setText("免费取消");
                saveMoney.setVisibility(View.VISIBLE);
            } else {
                saveMoney.setVisibility(View.GONE);
                //不支持取消预约 需更换展示位置
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mBottomShape.getLayoutParams();
                layoutParams.topToBottom = -1;
                layoutParams.leftToLeft = -1;
                layoutParams.topToTop = R.id.seckillText;
                layoutParams.leftToRight = R.id.seckillText;
                layoutParams.bottomToBottom = R.id.seckillText;
                layoutParams.leftMargin = 20;
                mBottomShape.setLayoutParams(layoutParams);
            }
            mBottomShape.setText("服务时长 " + model.getDuration() + "分钟");
            mBottomShape.setVisibility(View.VISIBLE);
        } else {
            if (goodsDetail != null) {
                setGoodsData(seckillText, goodsDate, goodsPrice, mPinMoneySingle, saveMoney);
                //mStackLayout.setVisibility(View.VISIBLE);
                //mStackLabel.setLabels("满300减30,买送,钢铁侠,美国队长,蜘蛛侠,蝙蝠侠,雷神,暴风雨,金刚狼,雷神,暴风雨,金刚狼".split(","));//
                mStackLabel.setOnLabelItemWidthListener(new OnLabelItemWidthListener() {
                    @Override
                    public void onChangeWidth(final int width) {
                        mStackLabel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                mStackLabel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                final int mMoreSurpriseWidth = mMoreSurpriseTip.getWidth();
                                Log.e("SeckShare", "\n堆标签的宽 = " + width + "\n提示的宽度 = " + mMoreSurpriseWidth +
                                        "\n包裹布局的宽度 = " + mStackLayout.getWidth());
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mStackLabel.getLayoutParams();
                                if (width + mMoreSurpriseWidth >= mStackLayout.getWidth()) {
                                    layoutParams.topToTop = -1;
                                    layoutParams.topToBottom = R.id.share_more_surprise_tip;
                                    layoutParams.leftToLeft = R.id.share_more_surprise_tip;
                                    layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                                    layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                                    layoutParams.topMargin = 20;
                                    mStackLabel.setLabelBackground(R.drawable.shape_label_radius8_solid_bg);
                                    mStackLabel.setPaddingHorizontal((int) TransformUtil.dp2px(getActivity(), 8));
                                } else {
                                    layoutParams.leftToLeft = -1;
                                    layoutParams.leftToRight = R.id.share_more_surprise_tip;
                                    layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                                    layoutParams.topToTop = R.id.share_more_surprise_tip;
                                    layoutParams.bottomToBottom = R.id.share_more_surprise_tip;
                                    layoutParams.leftMargin = 10;
                                    mStackLabel.setLabelBackground(R.drawable.shape_share_color_ffeaea_radius4);
                                }
                                mStackLabel.setLayoutParams(layoutParams);
                            }
                        });
                    }
                });
                /** 促销活动标签，只要有就就进行显示 */
                if (goodsDetail.actVip != null) {
                    if (goodsDetail.actVip.PopInfo != null && goodsDetail.actVip.PopInfo.size() > 0) {
                        mStackLayout.setVisibility(View.VISIBLE);
                        List<String> strings = new ArrayList<>();
                        for (int i = 0; i < goodsDetail.actVip.PopInfo.size(); i++) {
                            if (goodsDetail.actVip.PopInfo.get(i).PopLabelName != null
                                    && !strings.contains(goodsDetail.actVip.PopInfo.get(i).PopLabelName)) {
                                strings.add(goodsDetail.actVip.PopInfo.get(i).PopLabelName);
                            }
                        }
                        mStackLabel.setLabels(strings);
                    } else {
                        mStackLayout.setVisibility(View.GONE);
                    }
                    //saveMoney.setVisibility(View.GONE);
                    //seckillText.setVisibility(View.GONE);
                }

                //积分详情分享二维码大小需调整
                if (5 == marketingType) {
                    ViewGroup.LayoutParams layoutParams = appletsImg.getLayoutParams();
                    layoutParams.width = (int) TransformUtil.dp2px(getActivity(), 70);
                    layoutParams.height = (int) TransformUtil.dp2px(getActivity(), 70);
                    appletsImg.setLayoutParams(layoutParams);
                }

                com.healthy.library.businessutil.GlideCopy.with(getActivity())
                        .load(mBase64Bitmap)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(appletsImg);

                goodsTitle.setText(goodsDetail.goodsTitle);
                mShareBackgroundUrl = goodsDetail.headImage;
            }
        }
        GlideCopy.with(goodsImg.getContext())
                .load(mShareBackgroundUrl)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into(goodsImg);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);

        if (TextUtils.isEmpty(personId)) {
            if (nickName.length() > 6) {
                nickName = nickName.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + nickName + "\"的推荐");
        } else {
            Date today=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString=new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            nickName = "<strong>" + personId + "母婴顾问</strong>";
            String timeP="</p><strong>" + "有效期至 "+tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + nickName + "”的推荐"+timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        shareViewLiner.addView(view);
    }

    /**
     * 限时活动（秒杀、领劵、砍价、拼团）
     *
     * @param faceUrl
     * @param nickName
     */
    private void buildList(String faceUrl, String nickName) {
        shareViewLiner.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        if (inflater == null) {
            return;
        }
        final View view = inflater.inflate(R.layout.item_share_list_layout, shareViewLiner, false);
        appletsImg = view.findViewById(R.id.appletsImg);
        ImageView listImg = view.findViewById(R.id.listImg);
        CornerImageView userImg = view.findViewById(R.id.userImg);
        TextView userNickName = view.findViewById(R.id.userNickName);

        ImageView mThisLogo = view.findViewById(R.id.shareThisLogo);

        /**
         * 设置门店Logo
         */
        ImageView mShopLogoImg = view.findViewById(R.id.shareShopLogoImg);
        mShopLogoImg.setVisibility((TextUtils.isEmpty(mShopLogo) || mShopLogo.equals("null")) ? View.GONE : View.VISIBLE);
        /** 设置异业logo */
        ImageView mSecondLogoImg = view.findViewById(R.id.shareSecondLogoImg);
        if (industyType == 1) {
            mSecondLogoImg.setVisibility((TextUtils.isEmpty(mShopLogo) || mShopLogo.equals("null")) ? View.GONE : View.VISIBLE);

            view.findViewById(R.id.share_list_content).setVisibility(View.VISIBLE);
            TextView mSecondShopName = view.findViewById(R.id.secondShopName);
            TextView mSecondShopAddress = view.findViewById(R.id.secondShopAddress);
            ConstraintLayout mShareUser = view.findViewById(R.id.cl_share_user);

            ViewGroup mHeaderView = view.findViewById(R.id.share_header_view);
            ViewGroup.LayoutParams mHeaderViewLayoutParams = mHeaderView.getLayoutParams();
            mHeaderViewLayoutParams.width = 0;
            mHeaderView.setLayoutParams(mHeaderViewLayoutParams);

            ConstraintLayout mShareListLayout = view.findViewById(R.id.item_share_list_linearLayout);
            ConstraintLayout.LayoutParams mShareLayoutParams = (ConstraintLayout.LayoutParams) mShareListLayout.getLayoutParams();
            mShareLayoutParams.topMargin = (int) TransformUtil.dp2px(requireContext(), 4f);
            mShareListLayout.setLayoutParams(mShareLayoutParams);

            ConstraintLayout.LayoutParams mThisLogoLayoutParams = (ConstraintLayout.LayoutParams) mThisLogo.getLayoutParams();
            mThisLogoLayoutParams.leftMargin = (int) TransformUtil.dp2px(requireContext(), 38f);
            mThisLogoLayoutParams.leftToRight = -1;
            mThisLogoLayoutParams.rightToRight = -1;
            mThisLogoLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            mThisLogo.setLayoutParams(mThisLogoLayoutParams);

            ConstraintLayout.LayoutParams mShopLogoImgLayoutParams = (ConstraintLayout.LayoutParams) mShopLogoImg.getLayoutParams();
            mShopLogoImgLayoutParams.leftToLeft = -1;
            mShopLogoImgLayoutParams.rightToLeft = R.id.shareSecondLogoImg;
            mShopLogoImgLayoutParams.rightMargin = (int) TransformUtil.dp2px(requireContext(), 10f);
            mShopLogoImg.setLayoutParams(mShopLogoImgLayoutParams);

            mSecondLogoImg.post(() -> {
                mShopLogoImgLayoutParams.width = mSecondLogoImg.getWidth();
                mShopLogoImg.setLayoutParams(mShopLogoImgLayoutParams);
            });

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mShareUser.getLayoutParams();
            layoutParams.topToTop = -1;
            layoutParams.bottomToBottom = -1;
            layoutParams.topToBottom = R.id.share_list_content;
            mShareUser.setLayoutParams(layoutParams);
            mShareUser.setBackground(new ColorDrawable(requireContext().getResources().getColor(R.color.transparent)));

            ConstraintLayout.LayoutParams userImgLayoutParams = (ConstraintLayout.LayoutParams) userImg.getLayoutParams();
            userImgLayoutParams.leftMargin = 0;
            userImg.setLayoutParams(userImgLayoutParams);

            /** 设置数据 */
            if (mActivityObject != null) {

            }
            if (presenter.detailModel != null) {
                ShopDetailModel detailModel = presenter.detailModel;
                String shopName = "";
                if (!TextUtils.isEmpty(detailModel.chainShopName)) {
                    shopName = detailModel.shopName + "(" + detailModel.chainShopName + ")";
                } else {
                    shopName = detailModel.shopName;
                }
                mSecondShopName.setText(shopName);
                mSecondShopAddress.setText(detailModel.provinceName + detailModel.cityName +
                        detailModel.addressAreaName + detailModel.addressDetails);

            }
        }

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(appletsImg);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mShopLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mSecondLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);


        TextView goodsDate = view.findViewById(R.id.goodsDate);
        goodsDate.setVisibility(View.GONE);

        String mActivityEndTime = "";
        int mDrawable = R.drawable.share_assemble_img;
        switch (marketingType) {
            case 1:
                mDrawable = R.drawable.share_kick_img;
               /* Kick mKick = (Kick) mActivityObject;
                if (mKick != null && !TextUtils.isEmpty(mKick.endTime)) {
                    goodsDate.setVisibility(View.VISIBLE);
                    mActivityEndTime = getEndTime(mKick.endTime);
                }*/
                break;
            case 2:
                mDrawable = R.drawable.share_assemble_img;
               /* KKGroup mObject = (KKGroup) mActivityObject;
                if (mObject != null && !TextUtils.isEmpty(mObject.endTime)) {
                    goodsDate.setVisibility(View.VISIBLE);
                    mActivityEndTime = getEndTime(mObject.endTime);
                }*/
                break;
            case 3:
                mDrawable = R.drawable.share_seckill_img;
                /*NewUserListModel mNewUserListModel = (NewUserListModel) mActivityObject;
                if (mNewUserListModel != null && !TextUtils.isEmpty(mNewUserListModel.endTime)) {
                    goodsDate.setVisibility(View.VISIBLE);
                    mActivityEndTime = DateUtils.getDateDay(mNewUserListModel.endTime,
                            "yyyy-MM-dd HH:mm:ss",
                            "yyyy年MM月dd日 HH:mm") + " 活动结束";
                }*/
                break;
            case 6:
                mDrawable = R.drawable.share_coupon_img;
                break;
        }

        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(mDrawable)
                .into(listImg);

        //goodsDate.setText(mActivityEndTime);// 2020年08月20日 23:59活动结束
        if (TextUtils.isEmpty(personId)) {
            if (nickName.length() > 6) {
                nickName = nickName.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + nickName + "\"的推荐");
        } else {
            Date today=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString=new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            nickName = "<strong>" + personId + "母婴顾问</strong>";
            String timeP="</p><strong>" + "有效期至 "+tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + nickName + "”的推荐"+timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        shareViewLiner.addView(view);

    }

    /**
     * 邀请有礼相关布局
     *
     * @param faceUrl
     * @param nickName
     */
    private void buildInvite(String faceUrl, String nickName) {
        shareViewLiner.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (inflater == null) {
            return;
        }
        final View view = inflater.inflate(R.layout.item_share_invite_layout, shareViewLiner, false);
        appletsImg = view.findViewById(R.id.appletsImg);
        ImageView listImg = view.findViewById(R.id.listImg);
        CornerImageView userImg = view.findViewById(R.id.userImg);
        TextView userNickName = view.findViewById(R.id.userNickName);
        //listImg.setImageBitmap(bitmap);

        //适配大屏幕手机展示样式
        if (mHeightPixels > 1920) {
            /*LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) listImg.getLayoutParams();
            linearParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics()));
            listImg.setLayoutParams(linearParams);

            mShareListLayout.setPadding(mShareListLayout.getPaddingLeft(),
                    mShareListLayout.getPaddingTop(),
                    mShareListLayout.getPaddingRight(),
                    mShareListLayout.getPaddingBottom() + 60);*/
        }


        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(appletsImg);

        /**
         * 设置门店Logo
         */
        ImageView mShopLogoImg = view.findViewById(R.id.shareShopLogoImg);
        mShopLogoImg.setVisibility(TextUtils.isEmpty(mShopLogo) ? View.GONE : View.VISIBLE);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mShopLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);


        TextView goodsDate = view.findViewById(R.id.goodsDate);
        goodsDate.setVisibility(View.GONE);


        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(R.drawable.share_invite_img)
                .into(listImg);
        if (mPostImgUrl != null) {
            com.healthy.library.businessutil.GlideCopy.with(getActivity()).load(mPostImgUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(listImg);
        }

        //goodsDate.setText(mActivityEndTime);// 2020年08月20日 23:59活动结束
        if (TextUtils.isEmpty(personId)) {
            if (nickName.length() > 6) {
                nickName = nickName.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + nickName + "\"的推荐");
        } else {
            Date today=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString=new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            nickName = "<strong>" + personId + "母婴顾问</strong>";
            String timeP="</p><strong>" + "有效期至 "+tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + nickName + "”的推荐"+timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        shareViewLiner.addView(view);
    }

    /**
     * 直播分享相关布局
     *
     * @param faceUrl
     * @param nickName
     */
    private void buildLive(String faceUrl, String nickName) {
        shareViewLiner.removeAllViews();
        shareViewLiner.setPadding(0, 0, 0, 0);
        topView.setVisibility(View.GONE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (inflater == null) {
            return;
        }
        final View view = inflater.inflate(R.layout.item_share_live_layout, shareViewLiner, false);
        appletsImg = view.findViewById(R.id.appletsImg);
        final ImageView goodsImg = view.findViewById(R.id.goodsImg);
        CornerImageView userImg = view.findViewById(R.id.userImg);
        TextView userNickName = view.findViewById(R.id.userNickName);
        TextView mLiveTitle = view.findViewById(R.id.tvLiveTitle);
        TextView mLiveTime = view.findViewById(R.id.tvLiveTime);
        TextView mLiveDesc = view.findViewById(R.id.tvLiveDesc);
        if (mActivityObject != null) {
            if (marketingType == 0) {
                LiveRoomInfo liveRoomInfo = (LiveRoomInfo) mActivityObject;
                mShareTitle = liveRoomInfo.courseTitle + "开播啦";
                mActivityId = liveRoomInfo.courseId;
                mActivityTitle = liveRoomInfo.courseIntroduce;
                mShareBackgroundUrl = liveRoomInfo.picUrl;
                mLiveTitle.setText(liveRoomInfo.courseTitle);
                if (!TextUtils.isEmpty(liveRoomInfo.actualBeginTime)) {
                    String dateDay = DateUtils.getDateDay(liveRoomInfo.actualBeginTime,
                            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
                    mLiveTime.setText(dateDay);
                }
                mLiveDesc.setText(mActivityTitle);
            } else {
                LiveVideoMain liveVideoMain = (LiveVideoMain) mActivityObject;
                mShareTitle = liveVideoMain.courseTitle + "开播啦";
                mActivityId = liveVideoMain.id;
                mActivityTitle = liveVideoMain.courseIntroduce;
                mShareBackgroundUrl = liveVideoMain.picUrl;
                mLiveTitle.setText(liveVideoMain.courseTitle);
                if (!TextUtils.isEmpty(liveVideoMain.actualBeginTime)) {
                    String dateDay = DateUtils.getDateDay(liveVideoMain.actualBeginTime,
                            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
                    mLiveTime.setText(dateDay);
                } else {
                    String dateDay = DateUtils.getDateDay(liveVideoMain.beginTime,
                            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
                    mLiveTime.setText(dateDay);
                }
                mLiveDesc.setText(mActivityTitle);
            }

        }
        //适配大屏幕手机展示样式
        if (mHeightPixels > 1920) {
            goodsImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) goodsImg.getLayoutParams();
                    layoutParams.height = goodsImg.getWidth();
                    goodsImg.setLayoutParams(layoutParams);
                }
            });
        }

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(appletsImg);

        /**
         * 设置门店Logo
         */
        final ImageView mShopLogoImg = view.findViewById(R.id.shareShopLogoImg);
        mShopLogoImg.setVisibility(TextUtils.isEmpty(mShopLogo) ? View.GONE : View.VISIBLE);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mShopLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);

        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(mShareBackgroundUrl)
                .error(R.drawable.img_1_1_default)
                .into(goodsImg);

        if (TextUtils.isEmpty(personId)) {
            if (nickName.length() > 6) {
                nickName = nickName.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + nickName + "\"的推荐");
        } else {
            Date today=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString=new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            nickName = "<strong>" + personId + "母婴顾问</strong>";
            String timeP="</p><strong>" + "有效期至 "+tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + nickName + "”的推荐"+timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        shareViewLiner.addView(view);
    }

    /**
     * 异业商品分享相关布局
     *
     * @param faceUrl
     * @param nickName
     */
    private void buildSecond(String faceUrl, String nickName) {
        shareViewLiner.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        if (inflater == null) {
            return;
        }

        final View view = inflater.inflate(R.layout.item_share_second_goods_layout, shareViewLiner, false);
        appletsImg = view.findViewById(R.id.appletsImg);
        final ConstraintLayout mStackLayout = view.findViewById(R.id.share_stack_layout);
        final ImageView mMoreSurpriseTip = view.findViewById(R.id.share_more_surprise_tip);
        final StackLabel mStackLabel = view.findViewById(R.id.stackLabelView);
        mStackLabel.setGetItemWidth(true);
        TextView goodsDate = view.findViewById(R.id.goodsDate);
        TextView goodsTitle = view.findViewById(R.id.goodsTitle);
        final ImageView goodsImg = view.findViewById(R.id.goodsImg);
        TextView goodsPrice = view.findViewById(R.id.goodsPrice);
        TextView seckillText = view.findViewById(R.id.seckillText);
        /**
         * 划线价 有活动划平台 没活动 划零售
         */
        TextView mPinMoneySingle = view.findViewById(R.id.pinMoneySingle);
        mPinMoneySingle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView saveMoney = view.findViewById(R.id.saveMoney);
        TextView secondShopName = view.findViewById(R.id.secondShopName);
        TextView secondShopAddress = view.findViewById(R.id.secondShopAddress);
        CornerImageView userImg = view.findViewById(R.id.userImg);
        TextView userNickName = view.findViewById(R.id.userNickName);

        if (goodsDetail != null) {
            mShareBackgroundUrl = goodsDetail.headImage;
            goodsTitle.setText(goodsDetail.goodsTitle);
            setGoodsData(seckillText, goodsDate, goodsPrice, mPinMoneySingle, saveMoney);
            if (detailModel != null) {
                String shopName = "";
                if (!TextUtils.isEmpty(detailModel.chainShopName)) {
                    shopName = detailModel.shopName + "(" + detailModel.chainShopName + ")";
                } else {
                    shopName = detailModel.shopName;
                }
                secondShopName.setText(shopName);
                secondShopAddress.setText(detailModel.cityName +
                        detailModel.addressAreaName + detailModel.addressDetails);
                if (!TextUtils.isEmpty(shopName)) {
                    secondShopName.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(secondShopAddress.getText().toString().trim())) {
                    secondShopAddress.setVisibility(View.VISIBLE);
                }
            }
        }

        //适配大屏幕手机展示样式
        if (mHeightPixels > 1920) {
            goodsImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    goodsImg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams linearParams = goodsImg.getLayoutParams();
                    linearParams.height = goodsImg.getWidth()/*((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics()))*/;
                    goodsImg.setLayoutParams(linearParams);
                }
            });
        }

        /**
         * 设置门店Logo
         */
        final ImageView mShopLogoImg = view.findViewById(R.id.shareShopLogoImg);
        mShopLogoImg.setVisibility(TextUtils.isEmpty(mMerchantLogo) ? View.GONE : View.VISIBLE);
        final ImageView mSecondLogoImg = view.findViewById(R.id.shareSecondLogoImg);
        mSecondLogoImg.setVisibility(TextUtils.isEmpty(mShopLogo) ? View.GONE : View.VISIBLE);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(appletsImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mMerchantLogo)
                .into(mShopLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mSecondLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);
        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(mShareBackgroundUrl)
                .error(R.drawable.img_1_1_default)
                .into(goodsImg);

        if (TextUtils.isEmpty(personId)) {
            if (nickName.length() > 6) {
                nickName = nickName.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + nickName + "\"的推荐");
        } else {
            Date today=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString=new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            nickName = "<strong>" + personId + "母婴顾问</strong>";
            String timeP="</p><strong>" + "有效期至 "+tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + nickName + "”的推荐"+timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        shareViewLiner.addView(view);

    }

    /**
     * 签到分享相关布局
     *
     * @param faceUrl
     * @param nickName
     */
    private void buildPointsSignIn(String faceUrl, String nickName) {
        shareViewLiner.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        if (inflater == null) {
            return;
        }
        final View view = inflater.inflate(R.layout.item_share_points_signin_layout, shareViewLiner, false);
        appletsImg = view.findViewById(R.id.appletsImg);
        View mPointsSignInBg = view.findViewById(R.id.share_points_signin_bg);
        CornerImageView userImg = view.findViewById(R.id.userImg);
        TextView userNickName = view.findViewById(R.id.userNickName);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(appletsImg);

        /**
         * 设置门店Logo
         */
        final ImageView mShopLogoImg = view.findViewById(R.id.shareShopLogoImg);
        mShopLogoImg.setVisibility(TextUtils.isEmpty(mShopLogo) ? View.GONE : View.VISIBLE);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mShopLogo)
                .into(mShopLogoImg);

        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);

        if (TextUtils.isEmpty(personId)) {
            if (nickName.length() > 6) {
                nickName = nickName.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + nickName + "\"的推荐");
        } else {
            Date today=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString=new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            nickName = "<strong>" + personId + "母婴顾问</strong>";
            String timeP="</p><strong>" + "有效期至 "+tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + nickName + "”的推荐"+timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        shareViewLiner.addView(view);
    }

    //    /**
//     * 格式化活动结束时间
//     *
//     * @param endTime 时间参数 格式为 2020-09-20 23:59:59
//     * @return
//     */
//    private String getEndTime(String endTime) {
//        String mEndTime = "";
//        String[] split;
//        String[] date;
//        String[] time;
//        split = endTime.split("\\s+");
//        date = split[0].split("-");
//        time = split[1].split(":");
//        if (date.length >= 2 && time.length >= 2) {
//            mEndTime = date[0] + "年" + date[1] + "月" + date[2] + "日" + time[0] + ":" + time[1] + " 活动结束";
//        }
//        return mEndTime;
//    }
//
//    public void toXiaochengxu() {//国坚要让长按小程序码去小程序
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String appId = Ids.WX_APP_ID; // 本应用微信AppId
//                IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_f9b4fbd9d3b8"; // 小程序原始id
//                req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//                if (ChannelUtil.isRealRelease()) {
//                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                } else {
//                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
//                }
//                api.sendReq(req);
//                dismiss();
//            }
//        }, 500);
//    }
    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    /**
     * 分享小程序
     *
     * @param bitmap
     * @param goodsId
     * @param shopId
     */
    private void buildWeixin(Bitmap bitmap, String goodsId, String shopId) {
        String spath = null;
        String stitle = "我在憨妈妈发现了一个不错的商品，赶快来看看吧。";
        String sdes = "";
        if (type == 3) {
            //砍价
            if (1 == marketingType) {
                stitle = "好货低价搬回家，快来和我一起砍价吧";
                mResourcesUmImg = R.mipmap.share_wechat_kick_img;
            }
            //拼团
            if (2 == marketingType) {
                stitle = "省心更省钱，和我一起拼团惠生活";
                mResourcesUmImg = R.mipmap.share_wechat_assemble_img;
            }
            //秒杀
            if (3 == marketingType) {
                stitle = "限时限量超低价秒杀，好货抢不停";
                mResourcesUmImg = R.mipmap.share_wechat_seckill_img;
            }
            //优惠券
            if (6 == marketingType) {
                stitle = "海量好券免费领，先领券再购物超划算";
                mResourcesUmImg = R.mipmap.share_wechat_coupon_img;
            }
            //活动列表地址 ( 秒杀、砍价、拼团、优惠券）
            spath = String.format(mPath + "?shopId=%s&merchantId=%s&referral_code=%s", visitShopId, merchantId, referral_code);
        } else if (type == 4) {
            stitle = mShareTitle;
            spath = String.format(mPath + "?id=%s", mActivityId);
        } else if (type == 5) {
            stitle = mShareTitle;
            spath = String.format(mPath + "?enlistActivityId=%s", mActivityId);
        } else if (8 == type) {
            //直播分享
            stitle = mShareTitle;
            sdes = mActivityTitle;
            spath = String.format(mPath + "?id=%s&referral_code=%s", mActivityId, referral_code);//直播id,导购码
        } else {
            stitle = "ding~超值推荐来啦：" + goodsDetail.goodsTitle;
            spath = String.format(mPath + "?goodsId=%s&shopId=%s&merchantId=%s&referral_code=%s&mType=%s&assembleId=%s&bargainId=%s", mGoodsId, visitShopId, merchantId, referral_code, marketingType + "", assembleId, bargainId);
        }
        /*switch (type) {
            case 1://                spath = String.format("/packageA/pages/goodsDetail/index?goodsId=%s&shopId=%s&merchantId=%s", goodsId,SpUtils.getValue(getActivity(),SpKey.CHOSE_SHOP),SpUtils.getValue(getActivity(),SpKey.CHOSE_MC));
                break;
            case 2:
                spath = String.format("/pages/home/goodsDetail/index/index?goodsId=%s&shopId=%s&merchantId=%s&referral_code=%s&mType=%s&assembleId=%s&bargainId=%s", goodsId, visitShopId, merchantId, referral_code, marketingType + "", assembleId, bargainId);
//                spath = String.format("/packageA/pages/commondityDetail/index?goodsId=%s&shopId=%s&merchantId=%s", goodsId, SpUtils.getValue(getActivity(),SpKey.CHOSE_SHOP),SpUtils.getValue(getActivity(),SpKey.CHOSE_MC));
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                break;
        }*/
        if (TextUtils.isEmpty(spath)) {
            Toast.makeText(getActivity(), "获取分享地址失败！", Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println(spath);
        shareMin(SHARE_MEDIA.WEIXIN, null, sdes, stitle, spath, bitmap);
    }

    /**
     * 分享朋友圈
     *
     * @param bitmap
     */
    private void buildWeixinCircel(Bitmap bitmap) {
//        String spath = "/packageA/pages/seckillList/inndex";
//        String stitle = "限时秒杀，憨妈精选好货底价轮番秒！";
//        String sdes = "";
        UMImage thumb = new UMImage(getActivity(), bitmap);
        thumb.setThumb(thumb);
        new ShareAction(getActivity())
                .withText("限时秒杀")
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(thumb)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }

    private void buildWeixin(Bitmap bitmap) {
//        String spath = "/packageA/pages/seckillList/inndex";
//        String stitle = "限时秒杀，憨妈精选好货底价轮番秒！";
//        String sdes = "";
        UMImage thumb = new UMImage(getActivity(), bitmap);
        thumb.setThumb(thumb);
        new ShareAction(getActivity())
                .withText("限时秒杀")
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(thumb)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }

    private void share(String url, String des, String title, Bitmap bitmap) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(getContext(), bitmap));
        web.setDescription(des);
        new ShareAction(getActivity())
                .withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }

    String referral_code;

    public String getGoodsSurl() {
        String urlPrefix = SpUtils.getValue(getContext(), UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme=%s&goodsId=%s&shopId=%s&referral_code=%s&assembleId=%s&bargainId=%s&marketingType=%s",
                urlPrefix,
                "NormGoodsDetail",
                goodsDetail.id,
                visitShopId,
                referral_code,
                assembleId,
                bargainId,
                marketingType);
        return url;
    }

    //获取通常形式的分享二维码
    public String getNormalSurl(String urlPrefix, String scheme, Map<String, String> extra) {
        String url = String.format("%s?type=8&scheme=%s&referral_code=%s",
                urlPrefix,
                scheme,
                referral_code);
        mStringBuilder.setLength(0);
        mStringBuilder.append(url);
        for (Map.Entry<String, String> entry : extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //url += "&" + key + "=" + value;
            mStringBuilder.append("&").append(key).append("=").append(value);
        }
        url = mStringBuilder.toString();
        System.out.println("分享的二维码:" + url);
        return url;
    }

    //获取通常形式的分享二维码(不带有导购码)
    public String getNormalSurlNorel(String urlPrefix, String scheme, Map<String, String> extra) {
        String url = String.format("%s?type=8&scheme=%s",
                urlPrefix,
                scheme);
        mStringBuilder.setLength(0);
        mStringBuilder.append(url);
        for (Map.Entry<String, String> entry : extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //url += "&" + key + "=" + value;
            mStringBuilder.append("&").append(key).append("=").append(value);
        }
        url = mStringBuilder.toString();
        System.out.println("分享的二维码:" + url);
        return url;
    }

    //获取通常形式的分享二维码
    public String getNormalSurlWithScheme(String scheme, Map<String, String> extra) {
        String urlPrefix = "hmmm://hmm/corresponding";
        String url = String.format("%s?type=8&scheme=%s&referral_code=%s",
                urlPrefix,
                scheme,
                referral_code);
        mStringBuilder.setLength(0);
        mStringBuilder.append(url);
        for (Map.Entry<String, String> entry : extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //url += "&" + key + "=" + value;
            mStringBuilder.append("&").append(key).append("=").append(value);
        }
        url = mStringBuilder.toString();
        return url;
    }

    //获取通常形式的分享二维码无导购码
    public String getNormalSurlWithSchemeNoRel(String scheme, Map<String, String> extra) {
        String urlPrefix = "hmmm://hmm/corresponding";
        String url = String.format("%s?type=8&scheme=%s",
                urlPrefix,
                scheme);
        mStringBuilder.setLength(0);
        mStringBuilder.append(url);
        for (Map.Entry<String, String> entry : extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //url += "&" + key + "=" + value;
            mStringBuilder.append("&").append(key).append("=").append(value);
        }
        url = mStringBuilder.toString();
        return url;
    }

    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void shareMin(SHARE_MEDIA shareMedia, String url, String des, String title, String path, Bitmap bitmap) {
        UMMin umMin = new UMMin("url");
        umMin.setTitle(title);
        if (type == 3) {
            umMin.setThumb(new UMImage(getActivity(), mResourcesUmImg));
        } else {
            umMin.setThumb(new UMImage(getActivity(), mShareBackgroundUrl));
        }
        umMin.setDescription(des);
        umMin.setPath(path);
        umMin.setUserName("gh_f9b4fbd9d3b8");
        if (ChannelUtil.isRealRelease()) {

        } else {
            com.umeng.socialize.Config.setMiniPreView();
        }
        try {
            new ShareAction(getActivity())
                    .withMedia(umMin)
                    .setPlatform(shareMedia)
                    .setCallback(umShareListener)
                    .share();
        } catch (Exception e) {

        }
        dismiss();
    }

    boolean bitmapHasPic = false;

    public void viewSaveToImage(View view) {
        // 把一个View转换成bitmap
        Bitmap bitmap = viewConversionBitmap(view);
        bitmapHasPic = true;
        //bitmap转图片并保存本地
        saveBmp2GalleryNew(bitmap, "hmm" + new Date().getTime());//用时间戳来命名图片名称
    }

    double scale = 1;

    /**
     * view转bitmap
     */
    public Bitmap viewConversionBitmap(View view) {
        view.clearFocus();
//        Runtime.getRuntime().gc();
        Bitmap bitmap = Bitmap.createBitmap((int) (view.getWidth() * 1), (int) (view.getHeight() * 1), Bitmap.Config.RGB_565);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    /**
     * url转bitmap
     */
    public Bitmap returnBitMap(final String url) {
        URL imageurl = null;
        try {
            imageurl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public void saveBmp2Gallery(Bitmap bmp, String picName) {

        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");

            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
            bmp.recycle();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //通知相册更新
//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
//                    bmp, fileName, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            try {
                getActivity().sendBroadcast(intent);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "已保存至相册！", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Looper.prepare();//子线程中需手动创建Looper才能Toast
//            Looper.loop();
        }
    }

    public void saveBmp2GalleryNew(Bitmap bmp, String picName) {
        String insertImage = null;
        try {
            insertImage = MediaStore.Images.Media.insertImage(LibApplication.getAppContext().getContentResolver(), bmp, picName, picName);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (!TextUtils.isEmpty(insertImage)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.parse(insertImage);
                    intent.setData(uri);
                    LibApplication.getAppContext().sendBroadcast(intent);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                            Toast.makeText(LibApplication.getAppContext(), "已保存至相册！", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getData() {
        if (marketingType == 3) {
            //秒杀不请求导购码
            presenter.getZxingCode();
        } else {
            presenter.getZxingCode();
        }
    }

    @Override
    public void onGetUserInfoSuccess(String faceUrl, String nickName) {
        /*if (type != 0) {
            if (type == 1 || type == 2 || type == 4 || type == 5 || type == 7) {
                buildGoods(faceUrl, nickName);
            } else if (type == 3) {
                buildList(faceUrl, nickName);
            } else if (type == 6) {
                buildInvite(faceUrl, nickName);
            }
        } else {
            shareViewLiner.removeAllViews();
        }*/
    }

    @Override
    public void getZxingCode(String result, String personId) {
        referral_code = result;
        this.personId = personId;
        if (type == 8 && !TextUtils.isEmpty(referral_code)) {
            /***直播分享 且是导购身份 */
            mMap.clear();
            mMap.put("groupId", groupId);
            mMap.put("type", 0);
            mMap.put("referralCode", referral_code);
            presenter.addLookLivePeopleNum(mMap);
        }
        if (!TextUtils.isEmpty(mShopLogo)) {
            //投票直接设置logo从外层传入，没有就没有
            onGetStoreDetialSuccess(mShopLogo, mMerchantLogo);
            return;
        }
        //请求101001 获取门店品牌Logo
        if (TextUtils.isEmpty(visitShopId)) {
            visitShopId = SpUtils.getValue(getActivity(), SpKey.CHOSE_SHOP);
        }
        String nowShopId=visitShopId;
        if(shopDetailModel!=null){
            nowShopId=shopDetailModel.id;
        }
        presenter.getStoreDetial(nowShopId);
    }

    /**
     * 门店详情(获取门店品牌Logo)
     *
     * @param shopLogo 品牌Logo
     */
    @Override
    public void onGetStoreDetialSuccess(String shopLogo, String partnerMerchantLogo) {
        this.mShopLogo = shopLogo;
        this.mMerchantLogo = partnerMerchantLogo;//当前异业情况会多出
        this.detailModel = presenter.detailModel;
        if (getActivity() == null) {//加固，避免显示弹框接口反应慢此时关闭弹框，后续接口成功回调出现问题
            return;
        }
        //获取用户信息
//        presenter.getUserInfo();

        if (type != 0) {
            String mUserIcon = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ICON);
            String mUserNick = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_NICK);
            if (type == 2 && industyType == 1) { //异业分享
                buildSecond(mUserIcon, mUserNick);
            } else if (type == 1 || type == 2 || type == 4 || type == 5 || type == 7) {//服务商品、标配商品、投票、报名、服务预约
                buildGoods(mUserIcon, mUserNick);
            } else if (type == 3) {//营销活动列表
                buildList(mUserIcon, mUserNick);
            } else if (type == 6) {//邀请有礼
                buildInvite(mUserIcon, mUserNick);
            } else if (type == 8) {//直播分享
                buildLive(mUserIcon, mUserNick);
            } else if (type == 9) {//签到分享
                buildPointsSignIn(mUserIcon, mUserNick);
            }
        } else {
            shareViewLiner.removeAllViews();
        }
        //积分商品单独处理
        if (type == 6) {

        } else {
            if (5 == marketingType) {
                mBase64Bitmap = CodeUtils.createImage(getGoodsSurl(), 650, 650, null);
                if (appletsImg != null) {
                    if (getContext() != null) {
                        com.healthy.library.businessutil.GlideCopy.with(getContext())
                                .load(mBase64Bitmap)
                                .placeholder(R.drawable.img_1_1_default)
                                .error(R.drawable.hmhg_zxing)
                                .into(appletsImg);
                    }
                }
                return;
            }
        }
        //局限在活动列表分享缓存此数据
        mMap.clear();
        //	小程序页面路径
        mMap.put("path", mPath);
        if (3 == type) {
            /** 秒杀、砍价、拼团、优惠券走向 */
            mMap.put("params", mExtraMap.puts("shopId", visitShopId)
                    .puts("merchantId", merchantId)
                    .puts("referral_code", referral_code));
        } else if (4 == type) {
            /** 报名投票->拉票分享走向 */
            mMap.put("params", mExtraMap.puts("referral_code", referral_code)
                    .puts("id", mActivityId)
                    .puts("voteTitle", mActivityTitle));
        } else if (5 == type) {
            /** 报名活动分享 */
            mMap.put("params", mExtraMap
                    .puts("enlistActivityId", mActivityId));
        } else if (6 == type) {
            /** 邀请有礼 */
            mMap.put("params", mExtraMap);
        } else if (8 == type) {
            /** 直播分享 */
            mMap.put("params", mExtraMap.puts("referral_code", referral_code));
        } else if (7 == type) {
            /** 服务预约 */
            mMap.put("params", mExtraMap.puts("referral_code", referral_code));
        } else if (9 == type) {
            mExtraMap.puts("merchantId", merchantId).puts("shopId", visitShopId).puts("referral_code", referral_code);
            mMap.put("params", mExtraMap);
        } else {
            /** 普通商品走向 */
            mMap.put("params", mExtraMap
                    .puts("goodsId", mGoodsId)
                    .puts("mType", marketingType + "")
                    .puts("marketingType", marketingType + "")
                    .puts("bargainId", bargainId)
                    .puts("assembleId", assembleId)
                    .puts("shopId", visitShopId)
                    .puts("merchantId", merchantId)
                    .puts("referral_code", referral_code));
        }
        //缓存小程序参数
        //if (type == 6 || type == 4 || type == 7 || type == 8) {
        // 邀请有礼/投票活动/服务预约
        //TODO 2021-09-22 调整分享都不走小程序太阳码
        presenter.setAppProgram(mMap);
        /*} else {
            presenter.setProgram(mMap);
        }*/
    }

    /**
     * 小程序二维码图片的base64编码 回调
     *
     * @param data
     */
    @Override
    public void onGetBase64DataSuccess(String data) {
        if (!TextUtils.isEmpty(data)) {
            String[] split = data.split(",");
            this.mBase64Data = split[1];
            //解码并转换为图片
            byte[] bitmapArray = Base64.decode(mBase64Data, Base64.DEFAULT);
            this.mBase64Bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        }

        if (appletsImg != null) {
            if (getContext() != null) {
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(mBase64Bitmap)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.hmhg_zxing)
                        .into(appletsImg);
            }
        }
    }

    @Override
    public void onGet32DataSuccess(String data) {

        //if (6 == type || 4 == type || 7 == type || 8 == type || type <= 2) {
        if (6 == type) {
            /** 不带导购码的中转页 */
            mBase64Bitmap = CodeUtils.createImage(getNormalSurlNorel(SpUtils.getValue(getContext(), UrlKeys.H5_BargainUrl), "HMMDEFAULT", new SimpleHashMapBuilder<>().puts("keyFrame", data)), 650, 650, null);
        } else if (9 == type) {
            /** 签到专属h5页面 */
            mBase64Bitmap = CodeUtils.createImage(getNormalSurl(SpUtils.getValue(getContext(), UrlKeys.H5_SHARE_SIGN_URL), "HMMDEFAULT",
                    new SimpleHashMapBuilder<>().puts("keyFrame", data).puts("merchantId",merchantId).puts("shopId",visitShopId)), 650, 650, null);
        } else {
            /** 带导购码的中转页 */
            mBase64Bitmap = CodeUtils.createImage(getNormalSurl(SpUtils.getValue(getContext(), UrlKeys.H5_BargainUrl), "HMMDEFAULT", new SimpleHashMapBuilder<>().puts("keyFrame", data)), 650, 650, null);
        }
        //}
        if (appletsImg != null) {
            if (getContext() != null) {
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(mBase64Bitmap)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.hmhg_zxing)
                        .into(appletsImg);
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void showDataErr() {

    }
}

