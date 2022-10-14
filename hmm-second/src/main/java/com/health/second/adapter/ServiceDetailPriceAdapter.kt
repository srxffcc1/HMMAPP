package com.health.second.adapter

import android.graphics.Paint
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.Goods2DetailKick
import com.healthy.library.model.Goods2DetailPin
import com.healthy.library.model.GoodsDetail
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.ParseUtils
import com.healthy.library.utils.StringUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Long
 * @desc: 服务详情 -> 服务金额
 * @createTime :2021/10/9 10:36
 */
class ServiceDetailPriceAdapter :
    BaseAdapter<GoodsDetail>(R.layout.item_service_detail_price_layout) {

    /*** 0 普通 1 砍价 2 拼团 3 秒杀*/
    private var mGoodsType: Int = 0
    private var mGoodsDetailKick: Goods2DetailKick? = null
    private var mGoodsDetailPin: Goods2DetailPin? = null
    private var countDownTimer: CountDownTimer? = null
    var isLoadSuccess = false

    fun setGoodsKickPin(goodsDetailKick: Goods2DetailKick?, goodsDetailPin: Goods2DetailPin?) {
        this.mGoodsDetailKick = goodsDetailKick
        this.mGoodsDetailPin = goodsDetailPin
    }

    private lateinit var listener: (mViewHeight: Int) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int) -> Unit) {
        this.listener = listener
    }

    fun setGoodsType(goodsType: Int) {
        this.mGoodsType = goodsType
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (isLoadSuccess) return
        isLoadSuccess = true
        model?.let {

            val mImageRes = when (mGoodsType) {
                2 -> R.drawable.service_assembleid_top_bg
                3 -> R.drawable.service_seckill_top_bg
                else -> R.drawable.service_bargainid_top_bg
            }

            val mDay = holder.getView<TextView>(R.id.txtDay)
            val mHour = holder.getView<TextView>(R.id.txtHour)
            val mMin = holder.getView<TextView>(R.id.txtMin)
            val mSec = holder.getView<TextView>(R.id.txtSec)

            val numTxt: String?
            val activityPrice: Double?
            val activityPinMoneySingle: Double?
            when (mGoodsType) {
                1 -> {
                    numTxt = "${mGoodsDetailKick?.bargainInfoDTO?.joinNum}已砍"
                    activityPrice = mGoodsDetailKick?.bargainInfoDTO?.floorPrice
                    activityPinMoneySingle = it.marketingGoodsChildren[0].retailPrice

                    checkTime(
                        mGoodsDetailKick?.bargainInfoDTO?.startTime!!,
                        mGoodsDetailKick?.bargainInfoDTO?.endTime!!,
                        mGoodsType,
                        mDay,
                        mHour,
                        mMin,
                        mSec
                    )
                }
                2 -> {
                    numTxt = "${mGoodsDetailPin?.assembleDTO?.joinNum}已拼"
                    activityPrice = mGoodsDetailPin?.assembleDTO?.assemblePrice
                    activityPinMoneySingle = it.marketingGoodsChildren[0].retailPrice

                    checkTime(
                        mGoodsDetailPin?.assembleDTO?.startTime!!,
                        mGoodsDetailPin?.assembleDTO?.endTime!!,
                        mGoodsType,
                        mDay,
                        mHour,
                        mMin,
                        mSec
                    )
                }
                3 -> {
                    numTxt = "${it.marketingSales}已抢"
                    activityPrice = it.marketingGoodsChildren[0].marketingPrice
                    activityPinMoneySingle = it.marketingGoodsChildren[0].retailPrice
                    checkTime("", it.marketingEndTime, mGoodsType, mDay, mHour, mMin, mSec)
                }
                else -> {
                    numTxt = "${it.appSales}"
                    activityPrice = it.platformPrice
                    activityPinMoneySingle = it.retailPrice
                }
            }

            holder.setText(R.id.numTxt, numTxt)
                .setText(R.id.activity_price, "${FormatUtils.moneyKeep2Decimals(activityPrice!!)}")
                .setText(
                    R.id.activity_pinMoneySingle,
                    "¥${FormatUtils.moneyKeep2Decimals(activityPinMoneySingle)}"
                )
                .setText(R.id.goodsTitle, it.goodsTitle)
                .setText(
                    R.id.tv_goodsPrice,
                    StringUtils.getResultPrice(it.platformPrice.toString())
                )
                .setText(
                    R.id.tv_pinMoneySingle,
                    "¥${StringUtils.getResultPrice(it.retailPrice.toString())}"
                )
                .setText(
                    R.id.tv_count, "已售${
                        ParseUtils.parseNumber(
                            (it.appSales).toString(),
                            10000,
                            "万"
                        )
                    }"
                )
                .setImage(R.id.iv_activity_bg, mImageRes)
                .setText(
                    R.id.pinPeopleNum,
                    "${mGoodsDetailPin?.assembleDTO?.regimentSize.toString()}人团"
                )
                .setVisible(R.id.tv_count, false)
                .setVisible(R.id.pinPeopleNum, mGoodsType == 2)
                .setVisible(R.id.goodsSpecMoneyLeft, mGoodsType == 1)
                .setVisible(R.id.cl_activity_price, mGoodsType != 0)
                .setVisible(R.id.activity_pinMoneySingle, activityPinMoneySingle > 0)
                .setVisible(R.id.tv_goodsPrice,mGoodsType==0)
                .setVisible(R.id.tv_pinMoneySingle,(mGoodsType==0))
                .setVisible(R.id.tv_price_left,mGoodsType==0)
        }

        if (::listener.isInitialized) {
            holder.itemView.post {
                listener.invoke(holder.itemView.height)
            }
        }

        // 设置划线价样式
        holder.getView<TextView>(R.id.activity_pinMoneySingle).paint.flags =
            Paint.STRIKE_THRU_TEXT_FLAG
        holder.getView<TextView>(R.id.tv_pinMoneySingle).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    /**
     * 设置活动倒计时
     */
    private fun checkTime(
        startStrTime: String,
        endStrTime: String,
        type: Int, // 1 砍价 2 拼团 3 秒杀
        kickDay: TextView,
        kickHour: TextView,
        kickMin: TextView,
        kickSec: TextView
    ) {
        var startTime: Date? = null
        var endTime: Date? = null
        try {
            if (type != 3) {
                startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startStrTime)
            }
            endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStrTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var desorg: Long = 0
        var timer: Long = 0
        if (type != 3) {
            desorg = startTime?.time!!
            timer = startTime.time
        }
        if (endTime != null) {
            timer = endTime.time
            desorg = endTime.time
        }
        timer = timer.minus(System.currentTimeMillis())
        countDownTimer?.let {
            it.cancel()
        }
        kickDay.visibility = View.VISIBLE
        if (timer > 0) {
            //System.out.println("开始计时");
            val finalTimer = timer
            val finalDesorg = desorg
            countDownTimer = object : CountDownTimer(finalTimer, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val array = DateUtils.getDistanceTime(
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                        DateUtils.formatLongAll(finalDesorg.toString())
                    )
                    /*if ("0" == array[0]) { //0天
                        kickDay.visibility = View.GONE
                    }*/
                    kickDay.text = array[0]
                    kickHour.text = array[1]
                    kickMin.text = array[2]
                    kickSec.text = array[3]
                }

                override fun onFinish() {
                    //kickDay.visibility = View.GONE
                    kickDay.text = "00"
                    kickHour.text = "00"
                    kickMin.text = "00"
                    kickSec.text = "00"
                    if (isClickInit()) {
                        countDownTimer?.cancel()
                        countDownTimer = null
                        moutClickListener.outClick("倒计时结束", "")
                    }
                }
            }.start()
            //将此 countDownTimer 放入list.
        } else {
            kickDay.text = "00"
            kickHour.text = "00"
            kickMin.text = "00"
            kickSec.text = "00"
        }
    }

    fun cancelTimer() {
        countDownTimer?.let {
            it.cancel()
            countDownTimer = null
        }
    }

}