package com.health.second.adapter

import android.graphics.Color
import android.os.CountDownTimer
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.business.KKGroupGoodsDialog
import com.healthy.library.business.PinWithShop2Dialog
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.AssemableTeam
import com.healthy.library.model.Goods2DetailPin
import com.healthy.library.routes.DiscountRoutes
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.SpanUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CornerImageView
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Long
 * @desc: 正在拼团列表数据
 * @createTime :2021/10/18 15:25
 */
class ServiceAssembleTeamAdapter :
    BaseAdapter<String>(R.layout.item_include_pin_bottom) {

    private var mGoods2DetailPin: Goods2DetailPin.Assemble? = null
    private var teamList: MutableList<AssemableTeam>? = null
    private val countDownCounters = SparseArray<CountDownTimer>()

    private lateinit var listener: (mViewHeight: Int) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int) -> Unit) {
        this.listener = listener
    }

    private var mItemHeight: Int = 0

    fun getItemHeight(): Int {
        return mItemHeight
    }

    fun setGoodsDetailPin(mGoods2DetailPin: Goods2DetailPin.Assemble?) {
        this.mGoods2DetailPin = mGoods2DetailPin
    }

    fun setTeamList(teamList: MutableList<AssemableTeam>?) {
        this.teamList = teamList
    }

    fun getTeamList(): MutableList<AssemableTeam>? {
        return teamList
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        val toInt = TransformUtil.dp2px(LibApplication.getAppContext(), 12f).toInt()
        linearLayoutHelper.marginTop = toInt
        linearLayoutHelper.marginLeft = toInt
        linearLayoutHelper.marginRight = toInt
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val mGroupLL = holder
            .setOnClickListenerS(
                View.OnClickListener {
                    when (it.id) {
                        R.id.groupTitleLL -> {
                            if (!ListUtil.isEmpty(teamList) && teamList?.size!! < 3) return@OnClickListener
                            PinWithShop2Dialog.newInstance()
                                .setData(
                                    mGoods2DetailPin,
                                    mGoods2DetailPin?.id.toString()
                                )
                                .setPinOnDialogClickListener { v, firstcoupon ->
                                    if (isClickInit) {
                                        moutClickListener.outClick("groupDialog", firstcoupon)
                                    }
                                }
                                .show(
                                    (context as AppCompatActivity).supportFragmentManager,
                                    "teamDialog"
                                )
                        }
                    }
                }, R.id.groupTitleLL
            )
            .getView<LinearLayout>(R.id.groupLL)
        holder.itemView.background = context.resources.getDrawable(R.drawable.shape_pregnancy)
        teamList?.let {
            if (it.size == mGroupLL.childCount) {
                return@let
            }
            holder.setVisible(R.id.morePin, it.size > 4)
            val cct = mutableListOf<AssemableTeam>()
            if (it.size > 4) {
                cct.add(it[0])
                cct.add(it[1])
                cct.add(it[2])
                cct.add(it[3])
            } else {
                cct.addAll(it)
            }
            for (i in cct.indices) {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.mall_item_activity_group_goods, null)
                val item = cct[i]

                val groupIcon: CornerImageView = view.findViewById<CornerImageView>(R.id.groupIcon)
                val groupName: TextView = view.findViewById<TextView>(R.id.groupName)
                val groupTeamNumber: TextView = view.findViewById<TextView>(R.id.groupTeamNumber)
                val groupTimeLimit: TextView = view.findViewById<TextView>(R.id.groupTimeLimit)
                val groupButton: TextView = view.findViewById<TextView>(R.id.groupButton)
                val dividerN = view.findViewById<View>(R.id.dividerN)
                GlideCopy.with(context)
                    .load(item.memberFaceUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(groupIcon)
                groupTeamNumber.text = SpanUtils.getBuilder(context, "还差")
                    .setForegroundColor(Color.parseColor("#444444"))
                    .append(item.remainderNum).setForegroundColor(Color.parseColor("#F02846"))
                    .append("人成团").setForegroundColor(Color.parseColor("#444444"))
                    .create()
                groupName.text = item.memberNickName
                if (item.isMyTeam(context)) {
                    groupButton.text = "邀请好友"
                } else {
                    groupButton.text = "去参团"
                }
                dividerN.visibility = View.VISIBLE
                if (i == cct.size - 1) {
                    dividerN.visibility = View.INVISIBLE
                }
                item.realEndTime = mGoods2DetailPin?.endTime
                item.regimentTimeLength = mGoods2DetailPin?.regimentTimeLength!!
                item.regimentSize = mGoods2DetailPin?.regimentSize!!
                try {
                    var countDownTimer: CountDownTimer? = null
                    if (countDownCounters.get(groupTimeLimit.hashCode()) != null) {
                        countDownTimer = countDownCounters.get(groupTimeLimit.hashCode())
                    }else{
                          countDownTimer = object : CountDownTimer(0, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }
                            override fun onFinish() {
                            }
                        }
                    }
                    checkTimeOutPin(countDownTimer!!, item, groupTimeLimit, true)
                } catch (e: Exception) {
                }

                groupButton.setOnClickListener {
                    if (item.isMyTeam(context)) {
                        ARouter.getInstance()
                            .build(DiscountRoutes.DIS_GROUPDETAIL)
                            .withString("teamNum", item.teamNum)
                            .navigation()
                    } else {
                        KKGroupGoodsDialog.newInstance()
                            .setAssemableTeam(item)
                            .setGroupGoodsDialogClicker { v ->
                                if (isClickInit) {
                                    moutClickListener.outClick("groupDialog", item)
                                }
                            }.show(
                                (context as AppCompatActivity).supportFragmentManager,
                                "kkOkDialog"
                            )
                    }
                }
                mGroupLL.addView(view)
            }

            val pinNowTeam = holder.getView<ConstraintLayout>(R.id.pinNowTeam)
            pinNowTeam.post {
                mItemHeight = pinNowTeam.height
                if (::listener.isInitialized) {
                    listener.invoke(mItemHeight)
                }
            }
        }
    }

    private fun checkTimeOutPin(
        countDownTimer: CountDownTimer,
        url: AssemableTeam,
        destview: TextView,
        needtimer: Boolean
    ) {
        var countDownTimer: CountDownTimer? = countDownTimer
        if (countDownTimer != null) {
            countDownTimer.cancel()
            countDownTimer = null
        }
        if (needtimer) {
            var startTime: Date? = null
            var endTime: Date? = null
            try {
                startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime)
                endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.realEndTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val nd = (1000 * url.regimentTimeLength * 60 * 60).toLong() //加入时间之后需要多少小时
            var desorg = startTime!!.time + nd
            var timer = startTime.time + nd
            if (endTime != null && endTime.time <= timer) {
                timer = endTime.time
                desorg = endTime.time
            }
            timer -= System.currentTimeMillis()
            if (timer > 0) {
                ////System.out.println("开始计时");
                val finalTimer = timer
                val finalDesorg = desorg
                countDownTimer = object : CountDownTimer(finalTimer, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val array = DateUtils.getDistanceTimeString(
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                Date()
                            ), DateUtils.formatLongAll(finalDesorg.toString() + "")
                        )
                        destview.text = "剩余$array"
                    }

                    override fun onFinish() {
                        destview.text = "过期"
                    }
                }.start()
                countDownCounters.put(destview.hashCode(), countDownTimer)
            } else {
                destview.text = "过期"
            }
        }
    }

}