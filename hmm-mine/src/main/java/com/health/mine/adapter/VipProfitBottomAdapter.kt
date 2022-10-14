package com.health.mine.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.mine.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.VipProfitModel

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/8/4
 * @version V1.0
 */
class VipProfitBottomAdapter :
    BaseAdapter<VipProfitModel>(R.layout.item_vip_profit_adapter_bottom) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val indicator: View by lazy { holder.getView<View>(R.id.indicator) }
        val resultName: TextView by lazy { holder.getView<TextView>(R.id.resultName) }
        val money: TextView by lazy { holder.getView<TextView>(R.id.money) }
        val createTime: TextView by lazy { holder.getView<TextView>(R.id.createTime) }
        val contentName: TextView by lazy { holder.getView<TextView>(R.id.contentName) }
        val mark: ShapeTextView by lazy { holder.getView<ShapeTextView>(R.id.mark) }
        val logsTxt: TextView by lazy { holder.getView<TextView>(R.id.logsTxt) }

        resultName.text = datas[position].sourceResult
        when (datas[position].status) {
            "1" -> {
                money.text = "未到帐"
                holder.setTextColor(R.id.resultName,Color.parseColor("#333333"))
                if("0".equals(datas[position].shareEnterType)){
                    createTime.text = "到账时间:${datas[position].expectReceivingTime?.split(" ")[0]}"
                }else{
                    createTime.text = "到账时间:待对方核销"
                }
            }
            "2" -> {
                money.text = "已到账"
                holder.setTextColor(R.id.resultName,Color.parseColor("#FF256C"))
                createTime.text = "到账时间:${datas[position].expectReceivingTime?.split(" ")[0]}"
            }
            "3" -> {
                money.text = "被撤销"
                holder.setTextColor(R.id.resultName,Color.parseColor("#333333"))
                createTime.text = "撤销时间:${datas[position].updateTime?.split(" ")[0]}"
            }
            "4" -> {
                money.text = "提现"
                holder.setTextColor(R.id.resultName,Color.parseColor("#333333"))
                createTime.text = "到账时间:${datas[position].expectReceivingTime?.split(" ")[0]}"
            }
        }
        if ("0".equals(datas[position].sourceType)) {
            if(!TextUtils.isEmpty(datas[position].actName)){

                mark.text = "分享赚-"+datas[position].actName
            }else{
                mark.text = "分享赚"
            }
        } else {
            mark.text = "其他"
        }
        if (!TextUtils.isEmpty(datas[position].comment)){
            logsTxt.text = datas[position].comment
        }
        if (!TextUtils.isEmpty(datas[position].sourceMemberPhone)){
            var phone= datas[position].sourceMemberPhone.substring(0, 3) + "****" + datas[position].sourceMemberPhone.substring(7, datas[position].sourceMemberPhone.length)
            contentName.text = "来源:${phone}"
        }else if(!TextUtils.isEmpty(datas[position].sourceMemberName)){

            contentName.text = "来源:${datas[position].sourceMemberName}"
        }

    }
}