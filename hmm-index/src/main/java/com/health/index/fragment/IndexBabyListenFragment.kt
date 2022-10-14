package com.health.index.fragment

import android.os.Bundle
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
//import com.health.index.adapter.IndexBabyListenTopAdapter
import com.health.index.contract.IndexBabyContract
import com.healthy.library.model.NewsInfo
import com.health.index.presenter.IndexBabyPresenter
import com.healthy.library.adapter.EmptyAdapter
import com.healthy.library.adapter.SoundListAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Block
import com.healthy.library.constant.Functions
import com.healthy.library.model.*
//import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
//import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
//import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
//import com.ximalaya.ting.android.opensdk.model.track.TrackList
import kotlinx.android.synthetic.main.fragment_index_baby_study.*
import java.util.*

class IndexBabyListenFragment : BaseFragment(), IndexBabyContract.View{

    private var indexBabyPresenter: IndexBabyPresenter? = null
    private var emptyAdapter: EmptyAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null
//    private var indexBabyListenTopAdapter: IndexBabyListenTopAdapter? = null
    private var soundListAdapter: SoundListAdapter? = null
    var map = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_index_baby_listen
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

//        indexBabyListenTopAdapter = IndexBabyListenTopAdapter()
//        delegateAdapter.addAdapter(indexBabyListenTopAdapter)

        soundListAdapter = SoundListAdapter("1")
        delegateAdapter.addAdapter(soundListAdapter)

        getData()
    }

    override fun getData() {
        super.getData()
        emptyAdapter!!.model=""
//        indexBabyPresenter?.getSoundAlbums(
//            SimpleHashMapBuilder<String, Any>()
//                .puts("pageSize", 10)!!
//                .puts("audioFrequencyCategoryId", "14")!!
//                .puts("audioType", "2")!!
//                .puts("currentPage", 1)!!
//                .puts("sortBy", "updated_at")!!
//                .puts("calcDimension", "2")!!
//                .puts("audioCategoryName", "育儿")!!
//                .puts(Functions.FUNCTION, "8065")!! as MutableMap<String, Any>
//        )
//
//        indexBabyPresenter?.getSoundAlbumsDown(
//            SimpleHashMapBuilder<String, Any>()
//                .puts("pageSize", 10)!!
//                .puts("number", "2")!!
//                .puts("audioType", "1")!!
//                .puts("currentPage", 1)!!
//                .puts(Functions.FUNCTION, "8066")!! as MutableMap<String, Any>
//        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IndexBabyListenFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    public fun refresh() {
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

    override fun onSuccessGetNewsList(indexAllSees: MutableList<NewsInfo>?, isMore: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetAudioNewsList(indexAllSees: MutableList<SoundAlbum>?) {
    }

    override fun onSuccessGetSoundAlbumsDownList(result: SoundHomeSplit?) {
        if (result?.values != null) {
            var i = 0
            while (i < result.values.size) {
                val album: SoundAlbum = result.values[i]
                if (Block.checkIsBlockReplace(album.album_title)) {
                    result.values.removeAt(i)
                    i--
                }
                i++
            }
            soundListAdapter!!.setData(result.values as ArrayList<SoundAlbum?>)
        }
    }

    override fun successGetSoundAlbum(result: SoundTypeSplit?) {
//        if (!ListUtil.isEmpty(result?.albums)) {
//            var id = result!!.albums[0].id.toString()
//            for ((index, e) in result.albums.withIndex()) {
//                if (result.albums[index].album_title == "育儿") {
//                    id = result.albums[index].id.toString()
//                }
//            }
//            indexBabyListenTopAdapter?.setAdapterId(id)
//            map.clear()
//            map[DTransferConstants.ALBUM_ID] = id
//            map[DTransferConstants.SORT] = "asc"
//            map[DTransferConstants.PAGE] = "1"
//            CommonRequest.getInstanse().defaultPagesize = 5
//            CommonRequest.getTracks(map, this)
//        }
    }

//    override fun onError(p0: Int, p1: String?) {
//
//    }
//
//    override fun onSuccess(list: TrackList?) {
//        indexBabyListenTopAdapter?.setAdapterData(list!!.tracks)
//        indexBabyListenTopAdapter?.model = "null"
//    }
}