package com.health.mine.adapter

import android.graphics.Color
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.mine.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.VipProfitModel
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.FrameActivityManager
import com.healthy.library.utils.SpanUtils
import com.healthy.library.widget.StringDialog

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/8/4
 * @version V1.0
 */
class VipProfitHeaderAdapter :
    BaseAdapter<String>(R.layout.item_vip_profit_adapter_header) {

    var data: VipProfitModel? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    public fun setAdapterData(resultData: VipProfitModel) {
        this.data = resultData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val cardLayout: ConstraintLayout by lazy { holder.getView<ConstraintLayout>(R.id.cardLayout) }
        val moneyTitle: TextView by lazy { holder.getView<TextView>(R.id.moneyTitle) }
        val moneyValue: TextView by lazy { holder.getView<TextView>(R.id.moneyValue) }
        val notReceivedValue: TextView by lazy { holder.getView<TextView>(R.id.notReceivedValue) }
        val notReceivedTitle: TextView by lazy { holder.getView<TextView>(R.id.notReceivedTitle) }
        val receivedTitle: TextView by lazy { holder.getView<TextView>(R.id.receivedTitle) }
        val receivedValue: TextView by lazy { holder.getView<TextView>(R.id.receivedValue) }
        val receivedBtn: ShapeTextView by lazy { holder.getView<ShapeTextView>(R.id.receivedBtn) }
        val couponTitle: TextView by lazy { holder.getView<TextView>(R.id.couponTitle) }
        val couponCount: TextView by lazy { holder.getView<TextView>(R.id.couponCount) }
        val goodsTitle: TextView by lazy { holder.getView<TextView>(R.id.goodsTitle) }
        val goodsCount: TextView by lazy { holder.getView<TextView>(R.id.goodsCount) }
        val usableAmount: TextView by lazy { holder.getView<TextView>(R.id.usableAmount) }
        val activityTips: TextView by lazy { holder.getView<TextView>(R.id.activityTips) }

        if (data != null) {
            moneyValue.text = FormatUtils.moneyKeep2Decimals(data?.totalAmount)
            notReceivedValue.text = FormatUtils.moneyKeep2Decimals(data?.totalOutstandingAmount)
            receivedValue.text = FormatUtils.moneyKeep2Decimals(data?.totalWithdrawalAmount)
            usableAmount.text = FormatUtils.moneyKeep2Decimals(data?.totalUsableAmount)
        }
        receivedBtn.setOnClickListener {
          if (isClickInit){
              moutClickListener.outClick("tixian","")
          }
        }
        activityTips.setOnClickListener {
            StringDialog.newInstance()
                .setUrl(
                    SpanUtils.getBuilder(LibApplication.getAppContext(), "1、")
                        .setForegroundColor(Color.parseColor("#222222")).setBold()
                        .append("分享赚商品分享给好友/朋友圈，好友下单后，")
                        .setForegroundColor(Color.parseColor("#222222"))
                        .append("可获得礼包所有内容（部分内容限制次数）")
                        .setForegroundColor(Color.parseColor("#222222"))
                        .append("，优惠券权益实时到账，分享赚现金权益默认7天后可提现；7天内好友核销订单，则现金收益可立即提现。\n")
                        .setForegroundColor(Color.parseColor("#222222"))
                        .append("2、").setForegroundColor(Color.parseColor("#222222")).setBold()
                        .append("被分享人订单一旦发生退款，分享人的对应权益将自动扣除，可在收益记录中查看。\n")
                        .setForegroundColor(Color.parseColor("#222222"))
                        .append("注：").setForegroundColor(Color.parseColor("#222222")).setBold()
                        .append("若出现不正当手段获取平台利益，经平台核实后有权取消用户参与本次活动的资格，并对账号进行封禁；同时有权撤销违规交易订单，并收回活动中发放的奖品（包含已使用的部分），并追求相关法律责任。\n")
                        .setForegroundColor(
                            Color.parseColor("#222222")
                        )
                        .create()
                )
                .setTitle("分享赚攻略")
                .show(
                    (FrameActivityManager.instance()
                        .topActivity() as BaseActivity).supportFragmentManager, "xiaozhishi"
                )
        }
    }
}