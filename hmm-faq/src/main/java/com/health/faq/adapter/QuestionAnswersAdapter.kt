package com.health.faq.adapter

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.faq.R
import com.health.faq.model.MyQuestion
import com.healthy.library.utils.DateUtils

class QuestionAnswersAdapter : BaseMultiItemQuickAdapter<MyQuestion, BaseViewHolder> {
    private val TYPE_LEVEL_0 = 1
    private val TYPE_LEVEL_1 = 2

    constructor(data: MutableList<MyQuestion>?) : super(data) {

        addItemType(TYPE_LEVEL_0, R.layout.adapter_my_question)
        addItemType(TYPE_LEVEL_1, R.layout.adapter_my_answer)
    }


    override fun convert(helper: BaseViewHolder?, item: MyQuestion?) {
        when (helper?.itemViewType) {
            1 -> {
                var content = if (item?.type == 1) "${item.replyCount}回答"
                else "${item?.readCount}人查看"
                helper.setText(R.id.tv_question, if (item?.type == 1) item?.introduction else item?.detail)
                        .setText(R.id.tv_money, "￥ ${item?.rewardMoney}")
                        .setText(R.id.sb_type, if (item?.type == 1) "悬赏求助" else "问专家")
                        .setText(R.id.tv_answer_num, content)
                        .setText(R.id.tv_answer_time, DateUtils.dateToStr(item?.createDate))

            }
            2 -> {
                helper.setText(R.id.tv_question, item?.introduction)
                        .setText(R.id.tv_answer_time, item?.replyDateIllustration)
                        .setText(R.id.sb_type, if (item?.type == 1) "悬赏求助" else "问专家")
                        helper.getView<View>(R.id.tv_answer_type).setVisibility(if(item?.isBest == 2){View.VISIBLE}else{View.GONE})
            }
        }
    }
}