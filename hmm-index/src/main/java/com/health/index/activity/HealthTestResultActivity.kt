package com.health.index.activity

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
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
import com.healthy.library.routes.IndexRoutes
import kotlinx.android.synthetic.main.activity_health_test_result.*

@Route(path = IndexRoutes.INDEX_HEALTHTESTRESULT)
class HealthTestResultActivity : BaseActivity(), HealthTestContract.View {

    @Autowired
    @JvmField
    var problemId = ""

    @Autowired
    @JvmField
    var result = ""

    @Autowired
    @JvmField
    var title = ""

    @Autowired
    @JvmField
    var optionList: MutableList<String>? = null

    private var healthTestPresenter: HealthTestPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        healthTestPresenter = HealthTestPresenter(this, this)
        getData()
        buildData()
    }

    override fun onGetAiCategoryListSuccess(result: MutableList<AiCategoryList>?) {
    }

    override fun onGetAiQuestionDetailSuccess(result: MutableList<AiQuestionDetail>?) {
    }

    override fun onGetAiResultSuccess(result: AiResult?) {
        if (result != null && !ListUtil.isEmpty(result.recordDetailsVos)) {
            aiResult.text = Html.fromHtml(result.recordDetailsVos[0].ask)
        } else {
            aiResult.text = "暂无评测结论"
        }
    }

    override fun getData() {
        super.getData()
        healthTestPresenter?.getAiResult(
            SimpleHashMapBuilder<String, Any>()
                .puts("problemId", problemId)!!
                .puts("result", result)!! as MutableMap<String, Any>
        )
    }

    private fun buildData() {
        resultTitle.text = title
        if (!ListUtil.isEmpty(optionList)) {
            optionLiner.removeAllViews()
            for ((index, e) in optionList!!.withIndex()) {
                var view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_health_test_result_layout, null)
                var title = view.findViewById<TextView>(R.id.title)
                title.text = e
                optionLiner.addView(view)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_health_test_result
    }

    override fun findViews() {
        super.findViews()
        infoTxt.paint.isFakeBoldText = true
        infoTxt.postInvalidate()

        resultTxt.paint.isFakeBoldText = true
        resultTxt.postInvalidate()


        resultTitle.paint.isFakeBoldText = true
        resultTitle.postInvalidate()
    }
}