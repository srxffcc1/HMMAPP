package com.health.index.fragment

import android.os.Bundle
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
import com.health.index.adapter.IndexTabKnowledgeAdapter
import com.health.index.contract.IndexBabyContract
import com.healthy.library.model.NewsInfo
import com.health.index.presenter.IndexBabyPresenter
import com.healthy.library.adapter.EmptyAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.*
import kotlinx.android.synthetic.main.fragment_index_baby_study.*
import java.text.SimpleDateFormat
import java.util.*

class IndexBabyLookFragment : BaseFragment(), IndexBabyContract.View,
    IndexTabKnowledgeAdapter.OnLikeClickListener {
    private var indexTabKnowledgeAdapter: IndexTabKnowledgeAdapter? = null
    private var indexBabyPresenter: IndexBabyPresenter? = null
    private var emptyAdapter: EmptyAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_index_baby_study
    }

    override fun findViews() {
        indexBabyPresenter = IndexBabyPresenter(mContext, this)

        virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview?.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
            isNestedScrollingEnabled = false
        }

        emptyAdapter = EmptyAdapter()
        delegateAdapter.addAdapter(emptyAdapter)

        indexTabKnowledgeAdapter = IndexTabKnowledgeAdapter()
        indexTabKnowledgeAdapter?.setOnLikeClickListener(this)
        delegateAdapter.addAdapter(indexTabKnowledgeAdapter)

        getData()
    }

    override fun getData() {
        super.getData()
        indexBabyPresenter?.getRecommendNews(
            SimpleHashMapBuilder<String, Any>().puts(
                "pageSize",
                10
            )!!.puts("addressCity", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))!!
                .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))!!
                .puts("addressArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))!!
                .puts("currentPage", 1)!!
                .puts(
                    "queryDate",
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                )!! as MutableMap<String, Any>
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IndexBabyLookFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    public fun refresh(){
        getData()
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


    override fun onSuccessGetNewsList(indexAllSees: MutableList<NewsInfo>?, isMore: Boolean) {
        indexTabKnowledgeAdapter?.setData(indexAllSees as ArrayList<NewsInfo>?)

        if (ListUtil.isEmpty(indexTabKnowledgeAdapter?.datas)) {
            emptyAdapter?.model = "null"
            return
        }
    }

    override fun onSuccessGetAudioNewsList(indexAllSees: MutableList<SoundAlbum>?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetSoundAlbumsDownList(result: SoundHomeSplit?) {
        TODO("Not yet implemented")
    }

    override fun successGetSoundAlbum(result: SoundTypeSplit?) {
        TODO("Not yet implemented")
    }

    override fun articleLike(id: String, function: String) {
        indexBabyPresenter?.articleLike(
            SimpleHashMapBuilder<String, Any>()
                .puts("function", function)!!
                .puts("knowledgeId", id)!! as MutableMap<String, Any>
        )
    }
}