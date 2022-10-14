package com.health.index.activity

import android.os.Build
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
import com.health.index.adapter.IndexHealthTestMainAdapter
import com.health.index.contract.HealthTestContract
import com.health.index.model.AiCategoryList
import com.health.index.model.AiQuestionDetail
import com.health.index.model.AiResult
import com.health.index.presenter.HealthTestPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.StatusBarTxtColorUtil
import kotlinx.android.synthetic.main.activity_health_test_main.*

@Route(path = IndexRoutes.INDEX_HEALTHTESTMAIN)
class HealthTestMainActivity : BaseActivity(), IsFitsSystemWindows, HealthTestContract.View {

    private var indexHealthTestMainAdapter: IndexHealthTestMainAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null
    private var delegateAdapter: DelegateAdapter? = null
    private var healthTestPresenter: HealthTestPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusTxtWhite(true)
    }

    override fun init(savedInstanceState: Bundle?) {
        healthTestPresenter = HealthTestPresenter(this, this)
        getData()
        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerView?.layoutManager = virtualLayoutManager
        recyclerView?.adapter = delegateAdapter

        indexHealthTestMainAdapter = IndexHealthTestMainAdapter()
        delegateAdapter?.addAdapter(indexHealthTestMainAdapter)
    }

    override fun getData() {
        super.getData()
        healthTestPresenter?.getAiCategoryList(
                SimpleHashMapBuilder<String, Any>()!! as MutableMap<String, Any>
        )
    }

    private fun setStatusTxtWhite(bDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (bDark) {
                StatusBarTxtColorUtil.transparencyBar(this)
            }
            if (bDark) {
                StatusBarTxtColorUtil.setLightStatusBar(this, false, true)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_health_test_main
    }

    override fun findViews() {
        super.findViews()
        img_back.setOnClickListener { finish() }
    }

    override fun onGetAiCategoryListSuccess(result: MutableList<AiCategoryList>?) {
        if (ListUtil.isEmpty(result)) {

        } else {
            indexHealthTestMainAdapter?.setAdapterData(result!!)
            indexHealthTestMainAdapter?.model = ""
        }
    }

    override fun onGetAiQuestionDetailSuccess(result: MutableList<AiQuestionDetail>?) {

    }

    override fun onGetAiResultSuccess(result: AiResult?) {
    }
}