package com.health.faq.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.faq.R
import com.health.faq.adapter.DetailedAdapter
import com.health.faq.contract.DetailedContract
import com.health.faq.model.DetatiledBean
import com.health.faq.presenter.DetatiledPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.interfaces.OnNetRetryListener
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.DateUtils
import com.healthy.library.widget.CustomDatePicker
import com.healthy.library.widget.StatusLayout
import kotlinx.android.synthetic.main.activity_detailed.*

/**
 * @author xinkai.xu
 * 收入 消费明细
 * */
@Route(path = FaqRoutes.DETEILEDACTIVITY)
class DetailedActivity : BaseActivity(), DetailedContract.View, View.OnClickListener, OnNetRetryListener {
    private var selectTime: Long = System.currentTimeMillis()
    private var detailedAdapter: DetailedAdapter? = null
    private var mList: MutableList<DetatiledBean> = mutableListOf()
    private var mPresenter: DetatiledPresenter? = null

    @Autowired
    @JvmField
    var type: Int = 0

    override fun onClick(v: View?) {
        when (v) {
            tvDate -> {
                mDatePicker!!.show(selectTime)
            }
        }
    }

    private var mDatePicker: CustomDatePicker? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_detailed
    }

    override fun findViews() {

    }


    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        setStatusLayout(slDetailed)
        slDetailed.setOnNetRetryListener(this)
        detailedAdapter = DetailedAdapter(type, R.layout.adapter_detatiled, mList)
        mPresenter = DetatiledPresenter(this)
        rvDetailed.adapter = detailedAdapter
        rvDetailed.layoutManager = LinearLayoutManager(mContext)
        initDatePicker()
        tvDate.setOnClickListener(this)
        var day: String = DateUtils.long2Str(System.currentTimeMillis(), false).substring(0, 7)
        mPresenter?.getDetailed(day, type)

    }

    //  获取数据成功
    override fun getDetailedSuccess(detatiledBeans: MutableList<DetatiledBean>) {
        if (detatiledBeans.size == 0) {
            slDetailed.updateStatus(StatusLayout.Status.STATUS_EMPTY)
        } else {
            detailedAdapter?.setNewData(detatiledBeans)
        }


    }

    override fun onRetryClick() {
        super.onRetryClick()
        var day: String = DateUtils.long2Str(System.currentTimeMillis(), false).substring(0, 7)
        mPresenter?.getDetailed(day, type)
    }


    private fun initDatePicker() {
        val beginTimestamp = DateUtils.str2Long("2000-01-01", false)
        val endTimestamp = System.currentTimeMillis()
        tvDate.text = "本月"
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = CustomDatePicker(this, CustomDatePicker.Callback { timestamp ->
            selectTime = timestamp
            if (DateUtils.long2Str(endTimestamp, false).substring(0, 7) == DateUtils.long2Str(timestamp, false).substring(0, 7)) {
                tvDate.text = "本月"
            } else {
                tvDate.text = "${DateUtils.long2Str(timestamp, false)
                        .substring(0, 4)}年${DateUtils.long2Str(timestamp, false).substring(5, 7)}月"
            }
            //调用接口重新请求
            var day: String = DateUtils.long2Str(timestamp, false).substring(0, 7)
            mPresenter?.getDetailed(day, type)
        }, beginTimestamp, endTimestamp)
        mDatePicker!!.setSelectedTime(selectTime, true)
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker!!.setCancelable(false)
        // 不显示时和分
        mDatePicker!!.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker!!.setScrollLoop(false)
        // 不允许滚动动画
        mDatePicker!!.setCanShowAnim(false)
        mDatePicker!!.setCanShowDayTime(false)
    }

}
