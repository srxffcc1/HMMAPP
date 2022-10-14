package com.health.faq.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.health.faq.adapter.ExpertsListAdapter
import com.health.faq.model.Expert
import com.health.faq.model.ExpertModel
import com.healthy.library.base.BaseRefreshLoadActivity
import com.healthy.library.constant.Functions
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.JsonUtils
import com.healthy.library.widget.StatusLayout
import org.json.JSONObject

/**
 * @author
 * 专家列表
 *
 * */
class ExpertListActivity : BaseRefreshLoadActivity() {
    var mList: MutableList<Expert> = mutableListOf()
    var expertModel: ExpertModel? = null
    var expertsListAdapter: ExpertsListAdapter = ExpertsListAdapter(mList)
    override fun getTopBarText(): String {
        return "专家列表"
    }

    override fun getAdapter(): BaseQuickAdapter<*, *> {
        return expertsListAdapter
    }

    override fun isLoadMore(): Boolean {
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        expertsListAdapter.setOnItemChildClickListener { adapter, _, position ->
            getExpertStatus((adapter.data[position] as Expert).userId.toString())
        }
    }

    /*
    * 点击事件
    * */
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter?.let {
            val expert: Expert = it.data[position] as Expert
            ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                    .withString("id", expert.userId.toString())
                    .navigation()
        }
    }


    override fun getRvData(isRefresh: Boolean) {
        val map = HashMap<String, Any>(3)
        map["function"] = Functions.FUNCTION_EXPERT_LIST
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(this, mContext, false,
                        true, true, false, false, false) {
                    override fun onGetResultSuccess(obj: String?) {
                        super.onGetResultSuccess(obj)

                        expertModel = Gson().fromJson<ExpertModel>(obj!!)
                        expertModel?.let {
                            refreshSuccess(it.data)

                        }
                        fun onGetResultFail(result: String?) {
                            super.onGetResultFail(result)
                            refreshFail(StatusLayout.Status.STATUS_NET_ERR)
                        }
                    }
                })
    }

    private fun getExpertStatus(expertId: String) {
        val map = HashMap<String, Any>(2)
        map[Functions.FUNCTION] = Functions.FUNCTION_EXPERT_STATUS
        map["userId"] = expertId
        val observer = object : StringObserver(this, mContext, false,
                true, true, false, false, true) {
            override fun onGetResultSuccess(obj: String?) {
                super.onGetResultSuccess(obj)
                try {
                    val jsonObject = JSONObject(obj)
                    val status = JsonUtils.getInt(jsonObject, "data")
                    if (status == 1) {
                        ARouter.getInstance().build(FaqRoutes.FAQ_ASK_EXPERT)
                                .withString("id", expertId)
                                .navigation()
                    } else {

                        Toast.makeText(mContext, "当前专家不在线", Toast.LENGTH_SHORT).show()
                        ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                                .withString("id", expertId)
                                .navigation()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        ObservableHelper
                .createObservable(this, map)
                .subscribe(observer)
    }
}