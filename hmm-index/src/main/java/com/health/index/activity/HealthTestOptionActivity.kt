package com.health.index.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.index.R
import com.health.index.contract.HealthTestContract
import com.health.index.model.AiCategoryList
import com.health.index.model.AiQuestionDetail
import com.health.index.model.AiResult
import com.health.index.presenter.HealthTestPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.interfaces.OnLabelClickListener
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.widget.StackLabel
import kotlinx.android.synthetic.main.activity_health_test_option.*
import java.text.SimpleDateFormat
import java.util.*

@Route(path = IndexRoutes.INDEX_HEALTHTESTOPTION)
class HealthTestOptionActivity : BaseActivity(), HealthTestContract.View {

    @Autowired
    @JvmField
    var id = ""

    @Autowired
    @JvmField
    var title = ""

    private var healthTestPresenter: HealthTestPresenter? = null

    private var selectList = mutableListOf<Option>()
    private var resultList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        healthTestPresenter = HealthTestPresenter(this, this)
        getData()
    }

    override fun getData() {
        super.getData()
        healthTestPresenter?.getAiQuestionDetail(
            SimpleHashMapBuilder<String, Any>()
                .puts("id", id)!! as MutableMap<String, Any>
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_health_test_option
    }

    override fun findViews() {
        super.findViews()
        top_title.paint.isFakeBoldText = true
        top_title.postInvalidate()
        commit.setOnClickListener {
            if (!checkSelect()) {
                showToast("请回答全部问题")
            } else {
                ARouter.getInstance()
                    .build(IndexRoutes.INDEX_HEALTHTESTRESULT)
                    .withString("problemId", id)
                    .withString("result", buildResult())
                    .withObject("optionList", resultList)
                    .withString("title", title)
                    .navigation()
            }
        }
    }

    private fun checkSelect(): Boolean {
        for ((index, e) in selectList.withIndex()) {
            if (e.option == null) {
                return false
            }
        }
        return true
    }

    private fun buildResult(): String {
        resultList.clear()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var result = StringBuilder()
        resultList.add("评测时间：" + df.format(Date()))
        for ((index, e) in selectList.withIndex()) {
            result.append((index + 1).toString() + e.option)
            resultList.add(e.askTitle + "：" + e.answer)
        }
        return result.toString()
    }

    override fun onGetAiCategoryListSuccess(result: MutableList<AiCategoryList>?) {
    }

    override fun onGetAiQuestionDetailSuccess(result: MutableList<AiQuestionDetail>?) {

        if (!ListUtil.isEmpty(result)) {
            optionLiner.removeAllViews()
            for ((index, e) in result!!.withIndex()) {
                var view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_health_test_option_layout, null)
                var title = view.findViewById<TextView>(R.id.title)
                var stackLabelView = view.findViewById<StackLabel>(R.id.stackLabelView)
                stackLabelView.maxLines = e.answerBos!!.size
                title.text = e.askTitle
                title.paint.isFakeBoldText = true
                title.postInvalidate()
                var list = mutableListOf<String>()
                for ((index, i) in e.answerBos.withIndex()) {
                    list.add(e.answerBos[index].answer.toString())
                }
                selectList.add(Option(index, e.id, e.askTitle))
                stackLabelView.labels = list
                stackLabelView.setOnLabelClickListener(object : OnLabelClickListener {
                    override fun onClick(pos: Int, v: View?, s: String?) {
                        selectList[index].option = e.answerBos[pos].serialNum
                        selectList[index].answer = s
                    }
                })
                optionLiner.addView(view)
            }
        }
    }

    override fun onGetAiResultSuccess(result: AiResult?) {
    }

    open class Option {
        var index = 0
        var id: String? = null
        var option: String? = null
        var askTitle: String? = null
        var answer: String? = null

        constructor()

        constructor(index: Int, id: String?, askTitle: String?) {
            this.index = index
            this.id = id
            this.askTitle = askTitle
        }

    }
}