package com.health.index.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.health.index.R
import com.health.index.contract.IndexSearchContract
import com.health.index.fragment.SearchAllFragment
import com.health.index.fragment.SearchChildFragment
import com.health.index.model.IndexAllSee
import com.health.index.presenter.IndexSearchPresenter
import com.healthy.library.adapter.CanReplacePageAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.builder.ObjectIteraor
import com.healthy.library.builder.SimpleArrayListBuilder
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.message.IndexSearchInfo
import com.healthy.library.model.*
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.activity_index_search.*
import kotlinx.android.synthetic.main.activity_index_search.clear
import kotlinx.android.synthetic.main.activity_index_search.et_search
import kotlinx.android.synthetic.main.activity_index_search.img_back
import kotlinx.android.synthetic.main.activity_index_search.search
import kotlinx.android.synthetic.main.activity_index_search_result.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@Route(path = IndexRoutes.INDEX_SEARCHRESULT)
class IndexSearchResultActivity : BaseActivity(), IsFitsSystemWindows,
    TextView.OnEditorActionListener, IndexSearchContract.View {

    @Autowired
    @JvmField
    var key = ""

    private var mTitles = mutableListOf("综合", "课堂", "短视频", "帖子", "问答", "百科", "商品", "服务门店")
    private var mFragmentList = ArrayList<Fragment>()
    private var searchAllFragment: SearchAllFragment? = null
    private var searchChildFragment1: SearchChildFragment? = null
    private var searchChildFragment2: SearchChildFragment? = null
    private var searchChildFragment3: SearchChildFragment? = null
    private var searchChildFragment4: SearchChildFragment? = null
    private var searchChildFragment5: SearchChildFragment? = null
    private var searchChildFragment6: SearchChildFragment? = null
    private var searchChildFragment7: SearchChildFragment? = null
    private var pageAdapter: CanReplacePageAdapter? = null

    private var indexSearchPresenter: IndexSearchPresenter? = null
    private var mSearchDiscover = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
        indexSearchPresenter = IndexSearchPresenter(this, this)
        if (key.isNullOrEmpty().not()) {
            et_search.setText(key)
        }
        buildTab()
        getData()
    }

    override fun getData() {
        super.getData()
        indexSearchPresenter?.querySearchRecords(
            SimpleHashMapBuilder<String, Any>().puts("page", "1")!!
                .puts("pageSize", "10")!!
                .puts(
                    "memberStatus",
                    SpUtils.getValue(mContext, SpKey.STATUS_USER, 0)
                )!! as MutableMap<String, Any>
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun buildTab() {
        mFragmentList.clear()
        searchAllFragment = SearchAllFragment.newInstance(key.toString(), "0")
        searchChildFragment1 = SearchChildFragment.newInstance(key.toString(), "1")
        searchChildFragment2 = SearchChildFragment.newInstance(key.toString(), "2")
        searchChildFragment3 = SearchChildFragment.newInstance(key.toString(), "3")
        searchChildFragment4 = SearchChildFragment.newInstance(key.toString(), "4")
        searchChildFragment5 = SearchChildFragment.newInstance(key.toString(), "5")
        searchChildFragment6 = SearchChildFragment.newInstance(key.toString(), "6")
        searchChildFragment7 = SearchChildFragment.newInstance(key.toString(), "7")
        mFragmentList.add(searchAllFragment!!)
        mFragmentList.add(searchChildFragment1!!)
        mFragmentList.add(searchChildFragment2!!)
        mFragmentList.add(searchChildFragment3!!)
        mFragmentList.add(searchChildFragment4!!)
        mFragmentList.add(searchChildFragment5!!)
        mFragmentList.add(searchChildFragment6!!)
        mFragmentList.add(searchChildFragment7!!)
        pager.offscreenPageLimit = mFragmentList.size - 1
        pageAdapter = CanReplacePageAdapter(supportFragmentManager, mFragmentList, mTitles)
        pager.adapter = pageAdapter
        tab.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab?.text == null) {
                    return
                }
                val trim = tab.text.toString().trim { it <= ' ' }
                val spStr = SpannableString(trim)
                val styleSpan_B = StyleSpan(Typeface.BOLD)
                spStr.setSpan(styleSpan_B, 0, trim.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tab.text = spStr
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (tab?.text == null) {
                    return
                }
                val trim = tab.text.toString().trim { it <= ' ' }
                val spStr = SpannableString(trim)
                val styleSpan_B = StyleSpan(Typeface.NORMAL)
                spStr.setSpan(styleSpan_B, 0, trim.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tab.text = spStr
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tab.setupWithViewPager(pager)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_index_search_result
    }

    override fun findViews() {
        super.findViews()
        img_back.setOnClickListener { finish() }
        et_search.setOnEditorActionListener(this)
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
                buildSearchHistory(et_search.text.toString().trim())
                refreshChildData(et_search.text.toString().trim())
            } else {
                et_search.setText("")
                showToast("请输入搜索内容")
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (!TextUtils.isEmpty(et_search.text.toString().trim())) {
                buildSearchHistory(et_search.text.toString().trim())
                refreshChildData(et_search.text.toString().trim())
            } else {
                et_search.setText("")
                showToast("请输入搜索内容")
            }
        }
        return false
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

    private fun refreshChildData(str: String) {
        searchAllFragment?.refresh(str)
        searchChildFragment1?.refresh(str)
        searchChildFragment2?.refresh(str)
        searchChildFragment3?.refresh(str)
        searchChildFragment4?.refresh(str)
        searchChildFragment5?.refresh(str)
        searchChildFragment6?.refresh(str)
        searchChildFragment7?.refresh(str)
    }

    private fun buildSearchRecordsList(mSearchDiscover: MutableList<SearchRecordsModel>) {
        searchAllFragment?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment1?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment2?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment3?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment4?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment5?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment6?.setSearchRecordsList(mSearchDiscover)
        searchChildFragment7?.setSearchRecordsList(mSearchDiscover)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun childSearch(info: IndexSearchInfo) {
        if (info.type == 1) {
            if (info.searchStr.isNotEmpty()) {
                et_search.setText(info.searchStr)
                buildSearchHistory(info.searchStr)
                refreshChildData(info.searchStr)
            }
        }
        if (info.type == 2) {//搜索全部页面各个模块点击更多
            when (info.searchStr) {
                "跳转搜索课堂" -> pager.currentItem = 1
                "跳转搜索短视频" -> pager.currentItem = 2
                "跳转搜索帖子" -> pager.currentItem = 3
                "跳转搜索问答" -> pager.currentItem = 4
                "跳转搜索百科" -> pager.currentItem = 5
                "跳转搜索商品" -> pager.currentItem = 6
                "跳转搜索服务门店" -> pager.currentItem = 7
            }
        }
    }

    override fun onQuerySearchRecordsSuccess(
        records: MutableList<SearchRecordsModel>?,
        isMore: Boolean
    ) {
        if (records!!.isNotEmpty()) {
            buildSearchRecordsList(records!!)
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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun onQueryQuestionListSuccess(
        records: MutableList<FaqExportQuestion>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?) {
        TODO("Not yet implemented")
    }
}