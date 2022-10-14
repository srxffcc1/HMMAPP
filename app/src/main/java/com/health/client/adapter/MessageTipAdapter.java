package com.health.client.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.client.R;
import com.health.client.activity.MainActivity;
import com.health.client.model.MonMessageTip;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class MessageTipAdapter extends BaseQuickAdapter<MonMessageTip, BaseViewHolder> {


    public MessageTipAdapter() {
        this(R.layout.home_item_activity_messagetip);
    }

    private MessageTipAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MonMessageTip item) {
        ConstraintLayout hasContent;
        TextView helpTime;
        TextView tipTitle;
        CornerImageView tipImg;
        TextView tipContent;
        LinearLayout tipCanPass;

        hasContent = (ConstraintLayout) helper.getView(R.id.hasContent);
        helpTime = (TextView) helper.getView(R.id.helpTime);
        tipTitle = (TextView) helper.getView(R.id.tipTitle);
        tipImg = (CornerImageView) helper.getView(R.id.tipImg);
        tipContent = (TextView) helper.getView(R.id.tipContent);
        tipCanPass = (LinearLayout) helper.getView(R.id.tipCanPass);


        if (!TextUtils.isEmpty(item.adviseImg)) {
            tipImg.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(tipImg.getContext())
                    .asBitmap()
                    .load(item.adviseImg)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    
                    .into(tipImg);
        } else {

            tipImg.setVisibility(View.GONE);

        }
        tipTitle.setText(item.adviseTitle);
        tipContent.setText(item.adviseContent);
        helpTime.setText(DateUtils.getClassDate(item.createTime));
        tipCanPass.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(item.adviseLink)) {
            tipCanPass.setVisibility(View.VISIBLE);
        }
        String title = "";
        title = "通知";
        if (TextUtils.isEmpty(item.adviseTitle)) {

        } else {
            if (item.adviseTitle.contains("优惠")) {
                tipCanPass.setVisibility(View.VISIBLE);
            }
            if (item.adviseTitle.contains("疫苗")) {
                tipCanPass.setVisibility(View.VISIBLE);
            }
            if (item.adviseTitle.contains("产检")) {
                tipCanPass.setVisibility(View.VISIBLE);
            }
            if (item.adviseTitle.contains("直播")) {
                title = "憨妈直播";
            }
        }
        if(!TextUtils.isEmpty(item.androidLinkName)){//说明有人工期望
            tipCanPass.setVisibility(View.VISIBLE);
        }
        //人工跳转
        if(!TextUtils.isEmpty(item.h5LinkUrl)){//说明有人工期望
            tipCanPass.setVisibility(View.VISIBLE);
        }


        final String finalTitle = title;
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //人工跳转
                if(!TextUtils.isEmpty(item.androidLinkName)){//说明有人工期望
                    MARouterUtils.passToTarget(mContext,item.androidLinkName);
                    return;
                }
                //人工跳转
                if(!TextUtils.isEmpty(item.h5LinkUrl)){//说明有人工期望
                    MARouterUtils.passToTarget(mContext,item.h5LinkUrl);
                    return;
                }
                if (item.adviseTitle.contains("开播")) {
                    ARouter.getInstance().build(TencentLiveRoutes.LiveNotice)
                            .withString("courseId", item.adviseLink)
                            .navigation();
                    return;
                }
                //2021-04-27 解决通知界面跳转预约逻辑
                if (item.adviseContent.contains("预约")) {
                    /** 2021 新预约详情界面 */
                    if (!TextUtils.isEmpty(item.adviseLink)) {
                        ARouter.getInstance().build(ServiceRoutes.SERVICE_APPOINTMENTRESULT)
                                .withLong("id", Long.parseLong(item.adviseLink))
                                .navigation();
                    }
                    return;
                }
                //投票/报名活动通知
                if (item.adviseTitle.contains("活动通知")) {
                    if (item.adviseContent.contains("已中奖")) {
                        //跳转领奖下单页（调整为跳转报名投票列表页面）
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_VOTELIST)
                                .withString("currentItem", "1")
                                .navigation();
                        return;
                    }

                    if (item.adviseContent.contains("您已成功报名")
                            || item.adviseContent.contains("已被签核")) {
                        //跳转详情页面
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_ENLIST_DETAILS)
                                .withString("id", item.adviseLink)
                                .withString("isEnlist", "1")
                                .navigation();
                        return;
                    }
                }

                //抽奖中间通知
                if (item.adviseTitle.contains("中奖提醒")) {
                    if (item.adviseContent.contains("已中奖")) {
                        //跳转
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_AWARD_CENTER)
                                .navigation();
                        return;
                    }
                }

                //满足抽奖资格
                if (item.adviseTitle.contains("抽奖资格提醒")) {
                    if (item.adviseContent.contains("您已满足")) {
                        String url = "http://192.168.10.181:8000/lottery.html";
                        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL))) {
                            url = SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL);
                        }
                        //跳转领奖中心下单页
                        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                                //78434178651668480 -> 大转盘
                                //78448856115200000 -> 九宫格
                                .withString("url", url + "?id=" + item.adviseLink + "&token=" + SpUtils.getValue(mContext, SpKey.TOKEN))
                                .withString("title", "")
                                .withBoolean("isShowTopBar", false)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation();
                        return;
                    }
                }

                if (!TextUtils.isEmpty(item.adviseLink)) {
//                    ARouter.getInstance()
//                            .build(IndexRoutes.INDEX_WEBVIEW)
//                            .withString("title", finalTitle)
//                            .withBoolean("isinhome", false)
//                            .withString("url", item.adviseLink)
//                            .navigation();
                    Intent intent=new Intent(mContext, MainActivity.class);
                    intent.setData(Uri.parse(item.adviseLink));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    return;
                }
                if (TextUtils.isEmpty(item.adviseTitle)) {
                } else {
                    if (item.adviseTitle.contains("疫苗")) {
                        ARouter.getInstance().build(IndexRoutes.INDEX_TOOLS_VACC_CHECK)
                                .navigation();
                        return;
                    }
                    if (item.adviseTitle.contains("产检")) {
                        ARouter.getInstance().build(IndexRoutes.INDEX_TOOLS_DIV_CHECK)
                                .navigation();
                        return;
                    }
                    if (item.adviseTitle.contains("优惠")) {
                        ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST)
                                .withString("tabIndex", "1")
                                .navigation();
                        return;
                    }
                }


            }
        });

    }

    private void initView() {

    }
}
