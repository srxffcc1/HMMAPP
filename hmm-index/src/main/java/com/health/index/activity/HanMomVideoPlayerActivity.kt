package com.health.index.activity

import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.index.R
import com.health.index.adapter.VerticalViewPagerAdapter
import com.health.index.contract.HanMomVideoContract
import com.health.index.presenter.HanMomVideoPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.message.CommentEvent
import com.healthy.library.model.IndexAllQuestion
import com.healthy.library.model.PageInfoEarly
import com.healthy.library.model.VideoCategory
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.StatusBarTxtColorUtil
import kotlinx.android.synthetic.main.activity_han_mom_video_player.*
import org.greenrobot.eventbus.EventBus
import java.util.*

@Route(path = IndexRoutes.INDEX_HANMOMVIDEODETAIL)
class HanMomVideoPlayerActivity : BaseActivity(), IsFitsSystemWindows, HanMomVideoContract.View {

    @Autowired
    @JvmField
    var id = ""

    @Autowired
    @JvmField
    var categoryCode = ""

    @Autowired
    @JvmField
    var scope = 1

    var memberId = String(
        Base64.decode(
            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
            Base64.DEFAULT
        )
    )
    private var videoPlayerAdapter: VerticalViewPagerAdapter? = null
    private var hanMomVideoPresenter: HanMomVideoPresenter? = null
    private var mMap = mutableMapOf<String, Any?>()
    private var pageNum = 1
    private var videoList: MutableList<VideoListModel> = ArrayList()//所有的视频列表
    private var categoryList: MutableList<VideoCategory> = ArrayList()//所有的分类列表

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusTxtWhite(true)
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
        return R.layout.activity_han_mom_video_player
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        hanMomVideoPresenter = HanMomVideoPresenter(mContext, this)
        initAdapter()
        getData()
        if (categoryCode.isNotEmpty()) {
            getLoadMoreData(categoryCode)
        }
    }

    override fun getData() {
        super.getData()
        showLoading()
        hanMomVideoPresenter?.getVideoDetail(
            SimpleHashMapBuilder<String, Any>().puts("id", id)!!
                .puts("memberId", memberId) as MutableMap<String, Any>, 1
        )
        hanMomVideoPresenter?.getUserInfo(mutableMapOf<String, Any?>())
        hanMomVideoPresenter?.getTabList(
            SimpleHashMapBuilder<String, Any>().puts(
                "scope",
                "0"
            )!! as MutableMap<String, Any>
        )
        hanMomVideoPresenter?.getTabList(
            SimpleHashMapBuilder<String, Any>().puts(
                "scope",
                "1"
            )!! as MutableMap<String, Any>
        )
    }

    private fun getLoadMoreData(categoryCode: String) {
        mMap.clear()
        mMap["categoryCode"] = categoryCode
        mMap["pageNum"] = pageNum
        mMap["pageSize"] = 10
        mMap["memberId"] = memberId
        hanMomVideoPresenter?.getVideoList(mMap)
    }

    override fun findViews() {
        super.findViews()
        img_back.setOnClickListener { finish() }
    }

    override fun onGetTabListSuccess(result: MutableList<VideoCategory>?) {
        if (!ListUtil.isEmpty(result)) {
            categoryList.addAll(result!!)
        }
    }

    override fun onGetVideoListSuccess(
        result: MutableList<VideoListModel>?,
        pageInfoEarly: PageInfoEarly?
    ) {
        if (ListUtil.isEmpty(result)) {
            showEmpty()
            return
        }
        if (pageInfoEarly!!.nextPage > 0) {
            videoList.addAll(result!!)
            buildList()
        } else {//说明当前categoryCode下没数据了，换一个再请求
            buildCategory()
        }
    }

    private fun buildCategory() {
        if (videoList.size == 0) {
            showToast("暂无更多数据了")
            return
        }
        var oldPosition = 0
        for ((index, e) in categoryList.withIndex()) {
            if (e.categoryCode.equals(categoryCode)) {
                oldPosition = index
            }
        }
        categoryList.removeAt(oldPosition)
        getLoadMoreData(categoryList[0].categoryCode)
    }

    private fun buildList() {
        var oldPosition = 0
        for ((index, e) in videoList.withIndex()) {
            if (e.id.equals(id)) {
                oldPosition = index
            }
        }
        if (oldPosition > 0) {//把视频列表里请求回来的相同的视频移除掉
            videoList.removeAt(oldPosition)
        }
        videoPlayerAdapter!!.setUrlList(videoList)
        videoPlayerAdapter!!.notifyDataSetChanged()
    }

    override fun onGetVideoDetailSuccess(result: VideoListModel?, type: Int) {
        if (result != null) {
            videoList.add(result)
            videoPlayerAdapter!!.setUrlList(videoList)
            videoPlayerAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onAddPraiseSuccess(result: String?, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initAdapter() {
        videoPlayerAdapter = VerticalViewPagerAdapter(supportFragmentManager)
        viewpager.offscreenPageLimit = 1
        viewpager.adapter = videoPlayerAdapter
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                SpUtils.store(mContext, "videoIsScroll", true)
                EventBus.getDefault().post(CommentEvent(5));
                if (position == videoList.size - 3) {
                    pageNum++
                    getLoadMoreData(categoryCode)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }
}