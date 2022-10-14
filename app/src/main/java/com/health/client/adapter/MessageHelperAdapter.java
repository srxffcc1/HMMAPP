package com.health.client.adapter;

import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.client.R;
import com.health.client.model.MonMessageHelper;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class MessageHelperAdapter extends BaseQuickAdapter<MonMessageHelper, BaseViewHolder> {


    String type;

    public MessageHelperAdapter() {
        this(R.layout.home_item_activity_messagehelper);
    }

    private MessageHelperAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MonMessageHelper item) {

         ConstraintLayout hasContent;
         TextView helpTime;
         ImageView helpIcon;
         LinearLayout helpIconRightLL;
         ImageView helpIconLeft;
         TextView helpIconContent;
        hasContent = (ConstraintLayout) helper.getView(R.id.hasContent);
        helpTime = (TextView) helper.getView(R.id.helpTime);
        helpIcon = (ImageView) helper.getView(R.id.helpIcon);
        helpIconRightLL = (LinearLayout) helper.getView(R.id.helpIconRightLL);
        helpIconLeft = (ImageView) helper.getView(R.id.helpIconLeft);
        helpIconContent = (TextView) helper.getView(R.id.helpIconContent);

        final Drawable likeNormal = mContext.getResources().getDrawable(R.drawable.icon_message_more);

        helpIconContent.setText(item.content+" >>");
        if("同城圈小助手".equals(type)){
            helpIcon.setImageResource(R.drawable.message_type4);
            if(item.content.contains("已屏蔽")||item.content.contains("举报评论")){
//                helpIconContent.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
//                helpIconContent.setCompoundDrawablePadding(5);

                helpIconContent.setText(item.content+"");
            }else{
//                helpIconContent.setCompoundDrawablesWithIntrinsicBounds(null,null,likeNormal,null);
//                helpIconContent.setCompoundDrawablePadding(5);
            }
        }
        if("母婴服务小助手".equals(type)){
            helpIcon.setImageResource(R.drawable.message_type5);
        }
        if("问答小助手".equals(type)){
            helpIcon.setImageResource(R.drawable.message_type6);
        }
        helpTime.setText(DateUtils.getClassDate(item.createTime));
        if("同城圈小助手".equals(type)){
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.content.contains("关注")){
                        ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                                .withString("type","1")
                                .navigation();
                    }else {
                        if(TextUtils.isEmpty(item.resourceId)){
                            Toast.makeText(mContext,"帖子已删除",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(item.content.contains("已屏蔽")||item.content.contains("举报评论")){

                            return;
                        }
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_POSTDETAIL)
                                .withString("id",item.resourceId+"")
                                .withBoolean("isshowDiscuss",false)
                                .navigation();
                    }

                }
            });
        }
        if("母婴服务小助手".equals(type)){
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.content.contains("优惠")){
                        ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST)
                                .withString("tabIndex", "1")
                                .navigation();
                    }else {
                        if(TextUtils.isEmpty(item.resourceId)){
                            Toast.makeText(mContext,"订单已删除",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(item.orderRace)){
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                    .withString("orderId", item.resourceId + "")
                                    .withString("function", "25006")
                                    .navigation();
                        }else {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                    .withString("orderId", item.resourceId + "")
                                    .withString("function", "25006")
                                    .navigation();
                        }

                    }

                }
            });
        }
        if("问答小助手".equals(type)){
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(item.content.contains("无人回复")) {
                        ARouter.getInstance().build(FaqRoutes.FAQ_QUESTION_ANSWERS)
                                .navigation();
                    }else {
                        if(TextUtils.isEmpty(item.resourceId)){
                            Toast.makeText(mContext,"问答已删除",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(item.content.contains("已采纳")){
                            ARouter.getInstance().build(FaqRoutes.DETEILEDACTIVITY)
                                    .withInt("type", 1)
                                    .navigation();
                        }
                        else if(item.content.contains("专家")){
                            ARouter.getInstance()
                                    .build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                                    .withString("questionId", item.resourceId+"")
                                    .navigation();
                        }else {
                            ARouter.getInstance()
                                    .build(FaqRoutes.FAQ_QUESTION_DETAIL)
                                    .withString("questionId", item.resourceId + "")
                                    .withInt("pos", 0)
                                    .withBoolean("host", true)
                                    .navigation();
                        }
                    }
                }
            });
        }



    }

    private void initView() {

    }

    public void setType(String type) {
        this.type=type;
    }
}
