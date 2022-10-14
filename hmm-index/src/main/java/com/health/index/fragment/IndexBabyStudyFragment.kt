package com.health.index.fragment

import android.os.Bundle
import android.util.Base64
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
import com.health.index.adapter.IndexSearchHanMomVideoAdapter
import com.health.index.contract.IndexBabyContract
import com.healthy.library.model.NewsInfo
import com.health.index.presenter.IndexBabyPresenter
import com.healthy.library.adapter.EmptyAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.*
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.fragment_index_baby_study.*
import java.util.*

class IndexBabyStudyFragment : BaseFragment(), IndexBabyContract.View {
    private var indexSearchHanMomVideoAdapter: IndexSearchHanMomVideoAdapter? = null//憨妈课堂视频
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

        indexSearchHanMomVideoAdapter = IndexSearchHanMomVideoAdapter()
        delegateAdapter.addAdapter(indexSearchHanMomVideoAdapter)

        getData()
    }

    override fun getData() {
        super.getData()
        indexBabyPresenter?.queryVideoList(
            SimpleHashMapBuilder<String, Any>().puts(
                "pageSize",
                10
            )!!.puts("categoryCode", "003")!!
                .puts("pageNum", 1)!!
                .puts(
                    "memberId", String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    )
                ) as MutableMap<String, Any>
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IndexBabyStudyFragment().apply {
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
        indexSearchHanMomVideoAdapter?.clear()
        indexSearchHanMomVideoAdapter?.setData(records as ArrayList<VideoListModel>)
        if (ListUtil.isEmpty(indexSearchHanMomVideoAdapter?.datas)) {
            emptyAdapter?.model = "null"
            return
        }
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
        TODO("Not yet implemented")
    }

    override fun onSuccessGetAudioNewsList(result: MutableList<SoundAlbum>?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetSoundAlbumsDownList(result: SoundHomeSplit?) {
        TODO("Not yet implemented")
    }

    override fun successGetSoundAlbum(result: SoundTypeSplit?) {
        TODO("Not yet implemented")
    }
}