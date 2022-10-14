package com.health.faq.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.healthy.library.base.BaseView;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.FaqExport;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HotExportListAdapter extends BaseQuickAdapter<FaqExport, BaseViewHolder> {

    public String cityNo;

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo;
    }

    public HotExportListAdapter() {
        this(R.layout.item_faq_list_left);
    }

    private HotExportListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FaqExport item) {
        TextView tvName;
         TextView tvTitle;
         TextView ivAsk;
         TextView tvBeGoodAt;
         TextView tvAskCount;
         TextView tvAskMoney;
        final CornerImageView ivHeader = (CornerImageView) helper.getView(R.id.ivHeader);
        tvName = (TextView) helper.getView(R.id.tvName);
        tvTitle = (TextView) helper.getView(R.id.tvTitle);
        ivAsk = (TextView) helper.getView(R.id.ivAsk);
        tvBeGoodAt = (TextView) helper.getView(R.id.tvBeGoodAt);
        tvAskCount = (TextView) helper.getView(R.id.tvAskCount);
        tvAskMoney = (TextView) helper.getView(R.id.tvAskMoney);
        try {
            helper.setVisible(R.id.ivTip,cityNo.equals(item.addressCity)?true:false);
        } catch (Exception e) {
            helper.setVisible(R.id.ivTip,false);
            e.printStackTrace();
        }
        if (item.faceUrl != null) {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .asBitmap()
                    .load(item.faceUrl )
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    
                    .into(new BitmapImageViewTarget(ivHeader) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivHeader.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                        .withString("id", item.userId+"")
                        .navigation();
            }
        });
        helper.getView(R.id.ivAsk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getExpertStatus(item.userId+"");
            }
        });
        tvName.setText(item.realName);
        tvTitle.setText(item.rankName);
        try {
            tvBeGoodAt.setText("擅长："+com.healthy.library.utils.JsoupCopy.parse(item.goodSkills).text());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvAskCount.setText(item.counts+"个回答");

        if (StringUtils.getResultPrice(item.consultingFees+"").equals("0")){
            tvAskMoney.setText("限时免费");
        }else {
            tvAskMoney.setText("¥"+ StringUtils.getResultPrice(item.consultingFees+""));
        }





    }
    private void getExpertStatus(final String expertId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION,Functions.FUNCTION_EXPERT_STATUS);
        map.put("userId",expertId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver((BaseView) mContext, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
//                        mView.onGetExpertSuccess(resolveData(obj),resolveRefreshData(obj));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(obj);
                            int status = JsonUtils.getInt(jsonObject, "data");
                            if (status == 1) {

                                Map nokmap = new HashMap<String, String>();
                                nokmap.put("soure","专家答疑首页-知名专家咨询");
                                MobclickAgent.onEvent(mContext, "event2QuestExportFrom",nokmap);

                                ARouter.getInstance().build(FaqRoutes.FAQ_ASK_EXPERT)
                                        .withString("id", expertId)
                                        .navigation();
                            } else {
                                Toast.makeText(mContext,"当前专家不在线",Toast.LENGTH_SHORT).show();
                                ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                                        .withString("id", expertId)
                                        .navigation();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
//                        mView.onRequestFinish();
                    }
                });
    }
}
