package com.health.faq.presenter

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.health.faq.contract.DetailedContract
import com.health.faq.model.DetatiledBean
import com.healthy.library.LibApplication
import com.healthy.library.constant.Functions
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.FrameActivityManager
import org.json.JSONObject
import java.util.HashMap

class DetatiledPresenter(baseView: DetailedContract.View) : DetailedContract.Presenter {
    var baseView: DetailedContract.View? = null

    init {
        this.baseView = baseView

    }

    /**
     *
     * 获取消费记录 收入明细
     * @param type 1:收入明细 2：憨豆豆
     * @param time 时间
     * */
    override fun getDetailed(time: String, type: Int) {
        val map = HashMap<String, Any>()

        when (type) {
            1 -> {
                map["function"] = Functions.FUNCTION_DETAILED_INCOME
            }
            2 -> {
                map["function"] = Functions.FUNCTION_DETAILED__CONSUMPTION
            }
        }
        map["yearAndMonth"] = time
        ObservableHelper.createObservable(FrameActivityManager.instance().topActivity(), map)
                .subscribe(object : StringObserver(baseView, LibApplication.getAppContext(), false,
                        true, true, false, false, false) {
                    override fun onGetResultSuccess(obj: String?) {
                        super.onGetResultSuccess(obj)
                        var mutableList: MutableList<DetatiledBean>? = Gson().fromJson(JSONObject(obj)["data"].toString())
                        //解析数据
                        mutableList!!.let {
                            baseView?.getDetailedSuccess(it)
                        }
                    }

                })
    }
}