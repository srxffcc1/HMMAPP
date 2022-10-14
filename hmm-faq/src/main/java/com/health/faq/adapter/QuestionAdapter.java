package com.health.faq.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.health.faq.model.QuestionModel;

import java.util.Locale;

/**
 * @author Li
 * @date 2019/06/28 13:44
 * @des 问题列表
 */

public class QuestionAdapter extends BaseQuickAdapter<QuestionModel, BaseViewHolder> {
    private boolean mShowMoney;

    public QuestionAdapter(boolean showMoney) {
        this(R.layout.item_faq);
        mShowMoney = showMoney;
    }

    private QuestionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionModel item) {
        helper.setGone(R.id.tv_price, mShowMoney);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.getQuestionerAvatar())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.ic_questioner_default_avatar)
                
                .into((ImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, item.getQuestionerNickname());
        helper.setText(R.id.tv_period, item.getQuestionerPeriodDes());
        helper.setText(R.id.tv_question, item.getQuestionTitle());
        helper.setText(R.id.tv_answer_num, String.format(Locale.CHINA,
                "%d 回答", item.getAnswerNum()));
        helper.setText(R.id.tv_read_count, String.format(Locale.CHINA, "%d人查看",
                item.getReadCount()));
        helper.setBackgroundRes(R.id.tv_period,
                item.getQuestionerPeriod() == 1 ? R.drawable.shape_period_1 :
                        item.getQuestionerPeriod() == 2 ?
                                R.drawable.shape_period_2 : R.drawable.shape_period_3);
        if (mShowMoney) {
            helper.setText(R.id.tv_price, String.format("¥ %s", item.getQuestionCost()));
        }
    }
}
