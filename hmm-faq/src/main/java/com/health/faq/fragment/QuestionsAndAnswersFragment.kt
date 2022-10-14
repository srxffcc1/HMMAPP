package com.health.faq.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.health.faq.R
import com.health.faq.adapter.QuestionAnswersAdapter
import com.health.faq.contract.MyQuestionAnswersContract
import com.health.faq.model.MyQuestion
import com.health.faq.presenter.MyQuestionAnswersPresenter
import com.healthy.library.base.BaseFragment
import com.healthy.library.interfaces.OnNetRetryListener
import com.healthy.library.message.RefreshCountMsg
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.MyRecyclerViewDivider
import com.healthy.library.widget.StatusLayout
import kotlinx.android.synthetic.main.fragment_question_answers.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("ValidFragment")
class QuestionsAndAnswersFragment : BaseFragment(), BaseQuickAdapter.OnItemChildClickListener,
    OnNetRetryListener, MyQuestionAnswersContract.View, BaseQuickAdapter.OnItemClickListener,
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    fun newInstance(t: Int): QuestionsAndAnswersFragment {
        val args = Bundle()
        args.putInt("type", t)
        val fragment = QuestionsAndAnswersFragment()
        fragment.arguments = args
        return fragment
    }

    var type: Int = 0
    private var qaAdapter: QuestionAnswersAdapter? = null
    var list: MutableList<MyQuestion> = mutableListOf()
    var mPresenter: MyQuestionAnswersPresenter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_question_answers
    }

    override fun findViews() {
        setStatusLayout(layout_status)
        layout_status.setOnNetRetryListener(this)
        arguments?.let{
            type = it.getInt("type",0)
        }
        qaAdapter = QuestionAnswersAdapter(list)
        qaAdapter?.onItemChildClickListener = this
        mPresenter = MyQuestionAnswersPresenter(mActivity,this)
        recycler_question_answers.adapter = qaAdapter
        recycler_question_answers.addItemDecoration(
            MyRecyclerViewDivider(
                mContext,
                LinearLayoutManager.VERTICAL,
                18,
                R.color.transparent
            )
        )
        recycler_question_answers.layoutManager = LinearLayoutManager(mContext)
        layout_refresh.setOnRefreshListener(this)
        qaAdapter!!.onItemClickListener = this
        if (type == 1) {
            onRefresh()
        }
    }

    override fun onFirstVisible() {
        super.onFirstVisible()
        if (type == 2) {
            onRefresh()
        }
    }

    /*
      * 数据获取成功
      * */
    override fun getRuestionAnswersSuccess(questionList: MutableList<MyQuestion>) {
        layout_refresh.isRefreshing = false
        if (questionList.size == 0) {
            layout_status.updateStatus(StatusLayout.Status.STATUS_EMPTY)
        } else {
            questionList?.let { qaAdapter?.let { it.setNewData(questionList) } }
        }


    }

    /*
    * 刷新
    * */
    override fun onRefresh() {
        mPresenter?.let { it.getRuestionAnswers(type.toString()) }
    }


    /**
     * adapter点击事件
     *
     * */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

        qaAdapter?.let {
            val question: MyQuestion = it.data[position] as MyQuestion
            println("跳问答详情5")
            ARouter.getInstance()
                .build(if (question.type == 1) FaqRoutes.FAQ_QUESTION_DETAIL else FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                .withString("questionId", it.data[position].id!!.toString())
                .withInt("pos", position)
                .withBoolean("host", type == 1)
                .navigation()
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshCount(refreshCountMsg: RefreshCountMsg) {
        if (type == 1) {
            qaAdapter?.let {
                it.data[refreshCountMsg.pos].readCount = refreshCountMsg.count
                it.refreshNotifyItemChanged(refreshCountMsg.pos)
            }
        }

    }

    override fun getRuestionAnswersFail(msg: String) {
        layout_status.updateStatus(StatusLayout.Status.STATUS_NET_ERR)
    }

    override fun onRetryClick() {
        super.onRetryClick()
        onRefresh()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
}