package com.health.mine.adapter

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.mine.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.BankCardModel
import com.healthy.library.routes.MineRoutes
import com.healthy.library.utils.LogUtils

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/8/4
 * @version V1.0
 */
class BankcardManageAdapter :
    BaseAdapter<BankCardModel>(R.layout.item_vip_bank_card_adapter) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        if (position == itemCount - 1) {
            holder.setVisibility(R.id.addBankcard, View.VISIBLE)
            holder.setVisibility(R.id.cardLayout, View.GONE)
        } else {
            holder.setText(R.id.cardName, datas[position].bankName)
            holder.setText(
                R.id.changeBankCard,
                "**** ${
                    datas[position].bankCardNo.toString().substring(
                        datas[position].bankCardNo.length - 4,
                        datas[position].bankCardNo.length
                    )
                }"
            )
            holder.setVisibility(R.id.addBankcard, View.GONE)
            holder.setVisibility(R.id.cardLayout, View.VISIBLE)
        }
        holder.setOnClickListener(R.id.addBankcard, View.OnClickListener {
            ARouter.getInstance()
                .build(MineRoutes.MINE_VIPADDBANK)
                .navigation()

        })
        holder.setOnClickListener(R.id.cardLayout, View.OnClickListener {
            ARouter.getInstance()
                .build(MineRoutes.MINE_VIPBANKCARDDEL)
                .withString("cardNum", datas[position].bankCardNo)
                .navigation()
        })
    }
}