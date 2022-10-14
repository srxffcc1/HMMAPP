package com.health.faq.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.health.faq.model.ExpertHomepageModel;

import org.jsoup.Jsoup;

import java.util.List;

/**
 * @author Li
 * @date 2019/07/18 14:18
 * @des
 */
public class ExpertHomepageAdapter extends BaseMultiItemQuickAdapter<ExpertHomepageModel,
        BaseViewHolder> {

    public ExpertHomepageAdapter(List<ExpertHomepageModel> data) {
        super(data);
        addItemType(ExpertHomepageModel.TYPE_HEADER, R.layout.layout_expert_homepage_header);
        addItemType(ExpertHomepageModel.TYPE_ANSWER, R.layout.item_expert_answer);
        addItemType(ExpertHomepageModel.TYPE_NO_ANSWER, R.layout.layout_expert_no_answer);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExpertHomepageModel item) {
        switch (item.getItemType()) {
            case ExpertHomepageModel.TYPE_HEADER:
                helper.setText(R.id.consultingFees,"¥"+item.getHeaderInfo().consultingFees);
                helper.setText(R.id.tv_real_name, item.getHeaderInfo().realName);
                helper.setText(R.id.tv_titles, item.getHeaderInfo().rank);
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(item.getHeaderInfo().faceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into((ImageView) helper.getView(R.id.iv_avatar));
                helper.setText(R.id.tv_advisory_num, item.getHeaderInfo().askTimes);
                String responseTime = item.getHeaderInfo().avgReplyMinutes;
                SpannableString spannableString = new SpannableString(String.format("%smin", responseTime));
                spannableString.setSpan(new AbsoluteSizeSpan(16, true),
                        responseTime.length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_response_time, spannableString);
                try {
                    helper.setText(R.id.tv_good_at, com.healthy.library.utils.JsoupCopy.parse(com.healthy.library.utils.JsoupCopy.parse(item.getHeaderInfo().goodSkills).text()).text());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    helper.setText(R.id.tv_introduction, com.healthy.library.utils.JsoupCopy.parse(com.healthy.library.utils.JsoupCopy.parse(item.getHeaderInfo().expertIntroduction).text()).text());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                helper.setText(R.id.tv_answer_count, String.format("(%s)", item.getHeaderInfo().answerCount));
                helper.setGone(R.id.group_answer, item.getHeaderInfo().answerCount != 0);
                break;
            case ExpertHomepageModel.TYPE_ANSWER:
                helper.setText(R.id.tv_question, item.getAnswer().questionTitle);
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(item.getAnswer().faceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into((ImageView) helper.getView(R.id.iv_avatar));
                helper.setText(R.id.tv_date, item.getAnswer().date);
                helper.setText(R.id.tv_see_num, String
                        .format("%s人查看", String.valueOf(item.getAnswer().readCount)));
                helper.addOnClickListener(R.id.iv_ask);
                helper.setText(R.id.tv_see_content, "查看问答详情");
                break;
            case ExpertHomepageModel.TYPE_NO_ANSWER:
                helper.setGone(R.id.tv_ask, item.isOnline());
                helper.addOnClickListener(R.id.tv_ask);
                break;
            default:
                break;

        }
    }
}
