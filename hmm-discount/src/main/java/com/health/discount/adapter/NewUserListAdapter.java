package com.health.discount.adapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.NewUserListModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;


public class NewUserListAdapter extends BaseAdapter<NewUserListModel> {
    private int type = -1;

    public void setResult(int result) {
        this.type = result;
    }

    public NewUserListAdapter() {
        this(R.layout.item_new_user_bottom_layout);
    }

    public NewUserListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView price = holder.getView(R.id.price);
        TextView linePrice = holder.getView(R.id.linePrice);
        TextView goodsTitle = holder.getView(R.id.goodsTitle);
        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
        final NewUserListModel model = getDatas().get(position);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(model.filePath)
                .error(R.drawable.img_1_1_default)
                .into(goodsImg);
        goodsTitle.setText(model.goodsTitle);
        linePrice.setText("￥" + FormatUtils.moneyKeep2Decimals(model.retailPrice));
        linePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        SpannableString goodsprice = new SpannableString("新客价 ￥" + FormatUtils.moneyKeep2Decimals(model.marketingPrice));
        goodsprice.setSpan(new AbsoluteSizeSpan(13, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsprice.setSpan(new AbsoluteSizeSpan(14, true), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsprice.setSpan(new StyleSpan(Typeface.NORMAL), 4, goodsprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        price.setText(goodsprice);
        TextView tagText=holder.getView(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(model.getTagFirst())){
            tagText.setVisibility(View.VISIBLE);
            if(model.getTagFirst().length()>3){
                String org=model.getTagFirst();
                String resultOrg=org.substring(0,2)+"\n"+org.substring(2,org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            }else {
                tagText.setText(model.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "新客专享提示不满足新人专享条件的触发量");
                    MobclickAgent.onEvent(context, "event2APPNewUseFalseClick", nokmap);
                    Toast.makeText(context, "您不符合新客条件，可推荐给亲友购买哦~", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {//不是新客要等toast完成再跳转页面
                        @Override
                        public void run() {

                        }
                    }, 500);
                    if (model.goodsType == 1) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("goodsId", model.goodsId)
                                .withString("isNtReal", type + "")
                                .withString("marketingType", "4")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", model.goodsId)
                                .withString("isNtReal", type + "")
                                .withString("marketingType", "4")
                                .navigation();
                    }
                } else {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "新客专享商品图片/标题点击量");
                    MobclickAgent.onEvent(context, "event2APPNewUserListGoodsClick", nokmap);
                    if (model.goodsType == 1) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("goodsId", model.goodsId)
                                .withString("isNtReal", type + "")
                                .withString("marketingType", "4")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", model.goodsId)
                                .withString("isNtReal", type + "")
                                .withString("marketingType", "4")
                                .navigation();
                    }
                }
            }
        });
    }
}
