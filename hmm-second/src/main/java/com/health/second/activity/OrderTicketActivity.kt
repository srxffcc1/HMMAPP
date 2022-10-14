package com.health.second.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.second.R
import com.health.second.contract.OrderTicketContract
import com.health.second.presenter.OrderTicketPresenter
import com.health.second.weight.OrderTicketDialog
import com.healthy.library.base.BaseActivity
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.OrderList
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.FormatUtils
import com.healthy.library.widget.CornerImageView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_order_ticket.*

@Route(path = SecondRoutes.SECOND_ORDERTICKET)
class OrderTicketActivity : BaseActivity(), IsFitsSystemWindows,
    OnRefreshListener,
    OrderTicketContract.View {

    @Autowired
    @JvmField
    var ticket: String = ""

    private val mMap = mutableMapOf<String, Any?>()
    private var orderTicketPresenter: OrderTicketPresenter? = null
    private var orderList: OrderList? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_order_ticket
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        orderTicketPresenter = OrderTicketPresenter(this, this);
        getData()
    }

    override fun getData() {
        super.getData()
        mMap["ticket"] = ticket
        orderTicketPresenter?.getOrderInfo(mMap)
    }

    override fun onGetOrderInfoSuccess(model: OrderList?) {
        if (model == null || model.orderChild == null) {
            showEmpty()
        } else {
            if (model.orderChild.getOrderStatus() == 2) {
                layout_status.setmEmptyContent("该订单已完成，无需核销")
                showEmpty()
                showToast("该订单已完成")
                return
            }
            if (model.orderChild.getOrderStatus() == 4) {
                layout_status.setmEmptyContent("该订单已退款")
                showEmpty()
                showToast("该订单已退款")
                return
            }
            orderList = model
            buildData(orderList!!)
        }
    }

    private fun buildData(orderList: OrderList) {
        if (orderList.orderChild != null) {
            userName.text = orderList.orderChild.memberName
            userPhone.text = orderList.orderChild.memberPhone
            orderNumber.text = "订单编号 " + orderList.orderChild.orderNum
            shopName.text = orderList.orderChild.orderDetailList[0].shopName
            payAmount.text =
                "￥" + FormatUtils.formatRewardMoney(orderList.orderChild.totalPayAmount)
            buildGoods(orderList.orderChild.orderDetailList!!)
        }
    }

    private fun buildGoods(orderDetailList: List<OrderList.OrderDetailListBean>) {
        goodsLiner.removeAllViews()
        for ((index, model) in orderDetailList.withIndex()) {
            var view: View =
                LayoutInflater.from(mContext).inflate(R.layout.item_order_goods_layout, null)
            var goodsTitle: TextView = view.findViewById<View>(R.id.goodsTitle) as TextView
            var goodsImg: CornerImageView =
                view.findViewById<View>(R.id.goodsImg) as CornerImageView
            var goodsMoney: TextView = view.findViewById<View>(R.id.goodsMoney) as TextView
            var goodsCount: TextView = view.findViewById<View>(R.id.goodsCount) as TextView
            var refundTxt: ShapeTextView = view.findViewById<View>(R.id.refundTxt) as ShapeTextView
            goodsTitle.text = model.goodsTitle
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(model.goodsImage)
                .into(goodsImg)
            goodsMoney.text = "¥" + FormatUtils.moneyKeep2Decimals(model.goodsAmount)
            goodsCount.text = "x" + model.goodsQuantity

            if (model.refundCount > 0) {
                refundTxt.text = String.format("已退%s个", model.refundCount)
                refundTxt.visibility = View.VISIBLE
            } else {
                refundTxt.visibility = View.GONE
            }
            goodsLiner.addView(view)
        }
    }

    override fun onTicketSuccess(result: String?) {
        if (result != null) {
            if (result.contains("核销完成")) {
                var orderTicketDialog: OrderTicketDialog = OrderTicketDialog.newInstance()
                orderTicketDialog.setOnDialogClickListener { finish() }
                orderTicketDialog.show(
                    supportFragmentManager, ""
                )
            } else {
                showToast(result)
            }
        }
    }

    override fun findViews() {
        super.findViews()
        layout_refresh.setOnRefreshListener(this)
        layout_refresh.setEnableLoadMore(false)
        submitBtn!!.setOnClickListener {
            if (orderList != null) {
                val mMap = mutableMapOf<String, Any?>()
                mMap["ticket"] = ticket
                mMap["verifyComment"] = ""
                orderTicketPresenter?.ticket(mMap)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getData()
    }
}