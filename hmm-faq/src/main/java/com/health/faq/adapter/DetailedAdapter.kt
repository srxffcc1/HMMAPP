package com.health.faq.adapter

import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.text.Html
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.faq.R
import com.health.faq.model.DetatiledBean
import java.text.DecimalFormat

class DetailedAdapter(type: Int, layoutResId: Int, data: MutableList<DetatiledBean>?) : BaseQuickAdapter<DetatiledBean, BaseViewHolder>(layoutResId, data) {
    var type: Int = 0

    /**
     *
     * 1:收益 2：憨豆豆消费
     * */
    init {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: DetatiledBean) {
        when (type) {
            1 -> {
                helper.setText(R.id.tvDate, String.format("%02d", (item.monthAndDay!!.split("/")[0]).toInt())+"/"+String.format("%02d", (item.monthAndDay!!.split("/")[1]).toInt()))
                        .setText(R.id.tvTitle, "${item.sourceDescription}")
                        .setText(R.id.tvDateName, item.dayOfWeek)
                        .setText(R.id.tvAdoptTime, item.hourAndMinAsecond)
                        .setText(R.id.tvMoney, "+￥${DecimalFormat("0.##").format(item.incomeMoney)}")
                        .setText(R.id.tvHdd, "")
            }
            2 -> {
                helper.setText(R.id.tvDate, String.format("%02d", (item.monthAndDay!!.split("/")[0]).toInt())+"/"+String.format("%02d", (item.monthAndDay!!.split("/")[1]).toInt()))
                        .setText(R.id.tvTitle, "${item.virtualSourceDescription}")
                        .setText(R.id.tvDateName, item.dayOfWeek)
                        .setText(R.id.tvAdoptTime, item.hourAndMinAsecond)

                var tvMoney: TextView = helper.getView(R.id.tvMoney)
                if (item.virtualSource == 3 || item.virtualSource == 4) {
                    tvMoney.text = Html.fromHtml("<font color='#444444'>" +
                            item.virtualMoneyDescription + "</font>")
                } else {
                    //+ 憨豆豆
                    tvMoney.text = item.virtualMoneyDescription
                }
            }
        }

    }
}