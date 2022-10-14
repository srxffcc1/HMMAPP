package com.health.faq.fragment

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.health.faq.R
import com.health.faq.activity.ExpertListActivity
import com.health.faq.adapter.QuestionAdapter
import com.health.faq.contract.HotQuestionContract
import com.health.faq.decoration.QuestionDecoration
import com.health.faq.model.QuestionModel
import com.health.faq.presenter.HotQuestionPresenter
import com.healthy.library.base.BaseFragment
import com.healthy.library.constant.Events
import com.healthy.library.message.RefreshCountMsg
import com.healthy.library.routes.FaqRoutes
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_faq_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author: Li
 * Date: 2019/7/25 0025
 * Description:
 */
class HotQuestionFragment : BaseFragment(), OnRefreshLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener, HotQuestionContract.View {

    private var timefresh: Long = 0
    private var questionAdapter: QuestionAdapter? = null
    private var hotQuestionPresenter: HotQuestionPresenter? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_faq_list
    }

    override fun findViews() {
    }

    override fun onCreate() {
        questionAdapter = QuestionAdapter(false)
        layout_refresh.setEnableLoadMore(false)
        layout_refresh.setOnRefreshLoadMoreListener(this)
        recycler_question.layoutManager = LinearLayoutManager(mContext)
        recycler_question.addItemDecoration(QuestionDecoration(mContext))
        questionAdapter!!.bindToRecyclerView(recycler_question)
        questionAdapter!!.onItemClickListener = this
        hotQuestionPresenter = HotQuestionPresenter(mContext, this)
        tv_reward.setOnClickListener(this)
        iv_close.setOnClickListener(this)
        tv_ask_expert.setOnClickListener(this)
        layout_bottom.visibility = View.GONE
    }

    override fun onFirstVisible() {
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getData()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        println("跳问答详情:2")
        ARouter.getInstance()
                .build(FaqRoutes.FAQ_QUESTION_DETAIL)
                .withString("questionId", questionAdapter?.data?.get(position)?.questionId)
                .withInt("pos", position)
                .withBoolean("host", false)
                .navigation()

    }

    override fun getData() {
        hotQuestionPresenter?.getQuestionList()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)

    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)

    }

    override fun onRequestFinish() {
        layout_refresh?.finishRefresh()
//        layout_bottom.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshCountMsg(refreshCountMsg: RefreshCountMsg) {
//        if (!refreshCountMsg.host) {
//            questionAdapter?.let {
//                it.data[refreshCountMsg.pos].readCount = refreshCountMsg.count
//                it.notifyItemChanged(refreshCountMsg.pos)
//            }
//        }
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_reward -> {
                MobclickAgent.onEvent(mContext, Events.EVENT_REWARD_BTN)
                ARouter.getInstance().build(FaqRoutes.FAQ_REWARD).navigation()
            }
            v?.id == R.id.iv_close -> layout_bottom.visibility = View.GONE
            v?.id == R.id.tv_ask_expert -> startActivity(Intent(activity,
                    ExpertListActivity::class.java))
        }
    }

    override fun onGetQuestionListSuccess(questionList: MutableList<QuestionModel>?) {
        timefresh=System.currentTimeMillis();
        if (questionList.isNullOrEmpty()) {
            showEmpty()
        } else {
            questionAdapter?.setNewData(questionList)
        }
//        Handler().postDelayed({
//            if(System.currentTimeMillis()-timefresh>5000){
//                getData()
//            }
//        }, 5000)
    }
}