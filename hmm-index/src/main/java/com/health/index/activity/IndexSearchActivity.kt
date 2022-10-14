package com.health.index.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.health.index.R
import com.health.index.adapter.IndexSearchBannerAdapter
import com.health.index.adapter.IndexSearchHistoryAdapter
import com.health.index.adapter.SearchDiscoverAdapter
import com.health.index.contract.IndexSearchContract
import com.health.index.model.IndexAllSee
import com.health.index.presenter.IndexSearchPresenter
import com.healthy.library.adapter.BaseTitleAdapter
import com.healthy.library.adapter.IndexSearchDiscoverAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.builder.ObjectIteraor
import com.healthy.library.builder.SimpleArrayListBuilder
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.*
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_index_search.*
import java.util.*

@Route(path = IndexRoutes.INDEX_SEARCH)
class IndexSearchActivity : BaseActivity(), IsFitsSystemWindows, IndexSearchContract.View,
    TextView.OnEditorActionListener, BaseAdapter.OnOutClickListener, OnRefreshListener {

    private var indexSearchPresenter: IndexSearchPresenter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null
    private var delegateAdapter: DelegateAdapter? = null
    private var indexSearchBannerAdapter: IndexSearchBannerAdapter? = null
    private var searchDiscoverAdapter: SearchDiscoverAdapter? = null
    private var baseTitleAdapter: BaseTitleAdapter? = null
    private var indexSearchHistoryAdapter: IndexSearchHistoryAdapter? = null

    private var mTitleModel: TitleModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        indexSearchPresenter = IndexSearchPresenter(this, this)
        buildList()
        getData()
        buildHistory()
    }

    override fun onResume() {
        super.onResume()
        buildHistory()
    }

    override fun getData() {
        super.getData()
        indexSearchPresenter?.getAdBanner(
            SimpleHashMapBuilder<String, Any>().puts("type", "2")!!
                .puts("triggerPage", "25")!! as MutableMap<String, Any>
        )
        indexSearchPresenter?.querySearchRecords(
            SimpleHashMapBuilder<String, Any>().puts("page", "1")!!
                .puts("pageSize", "10")!!
                .puts(
                    "memberStatus",
                    SpUtils.getValue(mContext, SpKey.STATUS_USER, 0)
                )!! as MutableMap<String, Any>
        )
    }

    private fun buildHistory() {
        val mainSearchModelList = SpUtils.resolveHistoryArrayData(
            SpUtils.getValue(mContext, SpKey.USE_INDEXSEARCH),
            MainSearchModel::class.java
        )
        if (!ListUtil.isEmpty(mainSearchModelList)) {
            indexSearchHistoryAdapter!!.model = "null"
            indexSearchHistoryAdapter!!.setList(mainSearchModelList)
        } else {
            indexSearchHistoryAdapter!!.clear()
        }
    }

    private fun buildList() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview.layoutManager = virtualLayoutManager
        recyclerview.adapter = delegateAdapter
        recyclerview.itemAnimator = null

        indexSearchBannerAdapter = IndexSearchBannerAdapter()
        delegateAdapter?.addAdapter(indexSearchBannerAdapter)

        indexSearchHistoryAdapter = IndexSearchHistoryAdapter()
        delegateAdapter?.addAdapter(indexSearchHistoryAdapter)
        indexSearchHistoryAdapter?.setOutClickListener(this)

        baseTitleAdapter = BaseTitleAdapter()
        delegateAdapter?.addAdapter(baseTitleAdapter)

        searchDiscoverAdapter = SearchDiscoverAdapter()
        searchDiscoverAdapter?.setOutClickListener(this)
        delegateAdapter?.addAdapter(searchDiscoverAdapter)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_index_search
    }

    override fun findViews() {
        super.findViews()
        img_back.setOnClickListener { finish() }
        et_search.setOnEditorActionListener(this)
        layout_refresh.setEnableLoadMore(false)
        layout_refresh.setOnRefreshListener(this)
        clear.setOnClickListener { et_search.text = null }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    clear.visibility = View.VISIBLE
                } else {
                    clear.visibility = View.GONE
                }
            }
        })
        search.setOnClickListener {
            if (!TextUtils.isEmpty(et_search.text.toString().trim())) {
                jumpToResult(et_search.text.toString().trim())
            } else {
                et_search.setText("")
                showToast("请输入搜索内容")
            }
        }
//        et_search.postDelayed({
//            et_search.requestFocus()
//            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            manager.showSoftInput(et_search, 0)
//        }, 1000)
    }

    override fun onQuerySearchRecordsSuccess(
        records: MutableList<SearchRecordsModel>?,
        isMore: Boolean
    ) {
        layout_refresh.finishRefresh()
        searchDiscoverAdapter?.clear()
        if (records!!.isNotEmpty()) {
            var status = SpUtils.getValue(mContext, SpKey.STATUS_USER, 0)
            when (status) {
                1 -> {//备孕中
                    mTitleModel =
                        TitleModel().setTitle("备孕阶段关注热点").setRightTitle("").setShowRight(false)
                }
                2 -> {//怀孕中
                    mTitleModel =
                        TitleModel().setTitle("怀孕阶段关注热点").setRightTitle("").setShowRight(false)
                }
                3 -> {//育儿期
                    mTitleModel =
                        TitleModel().setTitle("育儿阶段关注热点").setRightTitle("").setShowRight(false)
                }
                else -> {
                    mTitleModel =
                        TitleModel().setTitle("关注热点").setRightTitle("").setShowRight(false)
                }
            }

            baseTitleAdapter?.model = mTitleModel
            searchDiscoverAdapter?.setData(records as ArrayList<SearchRecordsModel>)
        }
    }

    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onQueryHmmVideoListSuccess(
        records: MutableList<VideoListModel>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSuccessFan(result: Any?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessLike() {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetActivityList() {
        TODO("Not yet implemented")
    }

    override fun onGetBannerSuccess(adModels: MutableList<AdModel>?) {
        indexSearchBannerAdapter?.clear()
        if (ListUtil.isEmpty(adModels)) {
            indexSearchBannerAdapter?.model = null
        } else {
            GlideCopy.with(this)
                .load(adModels!![0].photoUrls)

                .into(object : SimpleTarget<Drawable?>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        indexSearchBannerAdapter?.model = "null"
                    }
                })
            indexSearchBannerAdapter?.setAdv(adModels)
        }
    }

    override fun onQueryGoodsListSuccess(
        records: MutableList<SortGoodsListModel>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onAddPraiseSuccess(result: String, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onSearchShopListSuccess(records: MutableList<ShopDetailModel>?) {
        TODO("Not yet implemented")
    }

    override fun onQueryArticleListSuccess(records: MutableList<IndexAllSee>?, isMore: Boolean) {

    }

    override fun onQueryQuestionListSuccess(
        records: MutableList<FaqExportQuestion>?,
        isMore: Boolean
    ) {

    }

    override fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?) {
        TODO("Not yet implemented")
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (!TextUtils.isEmpty(et_search.text.toString().trim())) {
                jumpToResult(et_search.text.toString().trim())
            } else {
                et_search.setText("")
                showToast("请输入搜索内容")
            }
        }
        return false
    }

    private fun jumpToResult(searchStr: String) {
        indexSearchPresenter?.addSearchRecord(
            SimpleHashMapBuilder<String, Any>().puts("searchContent", searchStr)!!
                .puts(
                    "shopId",
                    SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                )!!
                .puts("contentSource", 1)!!
                .puts("function", "search_record_001")!!
              as MutableMap<String, Any>
        )
        buildSearchHistory(searchStr)
        ARouter.getInstance()
            .build(IndexRoutes.INDEX_SEARCHRESULT)
            .withString("key", searchStr)
            .navigation()
    }

    private fun buildSearchHistory(searchStr: String) {
        val mainSearchModelList = SpUtils.resolveHistoryArrayData(
            SpUtils.getValue(mContext, SpKey.USE_INDEXSEARCH),
            MainSearchModel::class.java
        )
        if (!ListUtil.isEmpty(mainSearchModelList)) {
            if (!ListUtil.checkObjIsInList(
                    SimpleArrayListBuilder<String>()!!.putList(
                        mainSearchModelList,
                        ObjectIteraor<MainSearchModel> { o -> o.keyword }) as MutableList<String>,
                    searchStr
                )
            ) {
                mainSearchModelList.add(0, MainSearchModel(searchStr))
            } else {//说明存在历史，把存在的历史移到第一个
                var oldPosition = 0
                for ((index, e) in mainSearchModelList.withIndex()) {//拿到已存在的历史的position
                    if (e.keyword.equals(searchStr)) {
                        oldPosition = index
                    }
                }
                if (oldPosition > 0) {//调换位置，把当前位置的前移，其他位置的后移
                    for (index in 0..oldPosition) {
                        Collections.swap(mainSearchModelList, index, 0)
                    }
                }
            }
        } else {
            mainSearchModelList.add(0, MainSearchModel(searchStr))
        }
        SpUtils.store(mContext, SpKey.USE_INDEXSEARCH, Gson().toJson(mainSearchModelList))
    }

    override fun outClick(function: String?, obj: Any?) {
        if (function == "lable") {
            jumpToResult(obj as String)
        }
        if (function == "matchClick") {
            jumpToResult(obj as String)
        }
        if (function == "delete") {
            SpUtils.store(mContext, SpKey.USE_INDEXSEARCH, null)
            showToast("清除成功")
            Handler().postDelayed(Runnable { buildHistory() }, 200)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getData()
    }

}