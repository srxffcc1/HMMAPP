package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.PointsSignInModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.LotteryInfoModel;
import com.healthy.library.utils.TransformUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author: long
 * @date: 2021/4/15
 * @des
 */
public class PointsSignInHeaderAdapter extends BaseAdapter<PointsSignInModel> {

    private LotteryInfoModel mLotteryInfoModel;

    public void setLotteryInfoModel(LotteryInfoModel lotteryInfoModel) {
        this.mLotteryInfoModel = lotteryInfoModel;
    }

    public PointsSignInHeaderAdapter() {
        super(R.layout.item_points_signin_header_layout);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        //总积分展示优化
        String totalScore = getModel().totalScore;
        if (!TextUtils.isEmpty(totalScore)) {
            if (totalScore.contains(".")) {
                String[] split = totalScore.split("\\.");
                int lastIntegral = Integer.parseInt(split[split.length - 1]);
                if (lastIntegral != 0) {
                    DecimalFormat df = new DecimalFormat(",###,##0.00");
                    BigDecimal bd = new BigDecimal(totalScore);
                    totalScore = df.format(bd);
                } else {
                    totalScore = split[0];
                }
            }
        } else {
            totalScore = "0";
        }
        String body = "已累计签到<font color=\"#ff0000\"> " + getModel().seriesSignNum + "</font> 天";
        holder.setText(R.id.item_pointsSigNin_top_number, totalScore)
                .setText(R.id.tv_title, HtmlCompat.fromHtml(body, HtmlCompat.FROM_HTML_MODE_LEGACY))

                .setVisible(R.id.tv_LotteryTip, mLotteryInfoModel != null);
        holder.setOnClickListener(R.id.textViewEnd, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("查看签到记录", "");
                }
            }
        });

        ConstraintLayout mTitleLayout = holder.getView(R.id.title_layout);
        if (mLotteryInfoModel != null) {
            mTitleLayout.setPadding(mTitleLayout.getPaddingLeft(), mTitleLayout.getPaddingLeft(),
                    mTitleLayout.getPaddingRight(), mTitleLayout.getPaddingLeft());
            int memberSignType = mLotteryInfoModel.getMemberSignType();
            String mLotteryMessage = "";
            if (memberSignType == 0) {
                mLotteryMessage = "连续签到" + mLotteryInfoModel.getMemberSignCount() + "次";
            } else {
                mLotteryMessage = "累计签到" + mLotteryInfoModel.getMemberSignCount() + "次";
            }
            holder.setText(R.id.tv_LotteryTip, "签到抽奖！" + mLotteryMessage + "，参与抽奖哦～");
        } else {
            mTitleLayout.setPadding(mTitleLayout.getPaddingLeft(), (int) TransformUtil.dp2px(context, 20f),
                    mTitleLayout.getPaddingRight(), (int) TransformUtil.dp2px(context, 20f));
        }
    }
}
