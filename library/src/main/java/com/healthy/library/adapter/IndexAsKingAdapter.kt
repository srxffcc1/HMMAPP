package com.healthy.library.adapter

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.LibApplication
import com.healthy.library.R
import com.healthy.library.banner.UIndicator
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.AppQuestionTmp
import com.healthy.library.model.FaqExportQuestion
import com.healthy.library.routes.AppRoutes
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.AutoPollRecyclerView

/**
 * author : long
 * Time   :2021/12/13
 * desc   : 大家都在问 (可以共用 后续支持搜索列表页使用）
 */
class IndexAsKingAdapter : BaseAdapter<String>(R.layout.index_asking_item_layout) {

    var qusetionList: MutableList<FaqExportQuestion> = mutableListOf<FaqExportQuestion>()
    private var mBannerAdapter: IndexSpeedyConsultationAdapter? = null

    private var mExpertAdapter: IndexExpertListAdapter? = null
//    private var indexExpertBannerAdapter: IndexExpertBannerAdapter? = null
    private var mAutoPollRecyclerView: AutoPollRecyclerView? = null
    private var mList = mutableListOf<AppQuestionTmp>()

    private var mItemHeight = 0

    private lateinit var listener: (mViewHeight: Int, className: String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int, className: String) -> Unit) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return 5
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(), 12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        // 极速问诊
        mAutoPollRecyclerView = holder.getView<AutoPollRecyclerView>(R.id.banner_SpeedyConsultation)
        val mRecyclerView = holder.getView<RecyclerView>(R.id.recyclerView)
//        val mBanner = holder.getView<ViewPager2Banner>(R.id.banner)
        val mUIndicator = holder.getView<UIndicator>(R.id.indicator)
        var questionAll = holder.getView<AppCompatTextView>(R.id.questionAll)
        if (mBannerAdapter == null) {
            mBannerAdapter = IndexSpeedyConsultationAdapter()
            mList.clear()
            mList.add(AppQuestionTmp("儿科", "请问宝宝经常吐奶，头往后仰，是不是奶粉过敏牛乳蛋白过敏导致的？"))
            mList.add(AppQuestionTmp("妇产科", "产后一周，奶水不足，怎么办？"))
            mList.add(AppQuestionTmp("儿科", "6个月的宝宝经常拉绿便便是什么回事？"))
            mList.add(AppQuestionTmp("儿科", "宝贝背部涨了很多小红痘痘，手臂上也有，请问这是国民还是什么？"))
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "我家宝2个多月，咳嗽有痰，去医院听诊说是肺炎，体温持续37.5，今天做的雾化。阿莫西林克拉维酸钾吃了3天还是咳，尤其是雾化做完后咳嗽加重"
                )
            )
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "拉肚子一天3-5次，绿色带有黏液，有时候有黑色，吃了蒙脱石散和双歧杆菌四联活菌也没什么效果，目母乳不够，混合喂养，宝宝28号满3个月 "
                )
            )
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "您好医生，我家孩子五个月，晚上睡觉睡不踏实，老是翻来翻去，频繁夜醒是怎么回事，每天都十一二才睡，白天也不咋睡"
                )
            )
            mList.add(AppQuestionTmp("儿科", "孩子一到春天就全身都是包"))
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "宝宝快4个月了，最近母乳严重不足，刚给宝宝添加奶粉，宝宝不吃奶粉也不吸奶瓶，每次给他吃奶粉只吃15毫升，现在是2个小时喂一次，在喂就不吃了，他也感觉不到饿，一天就昏睡，尿量也没有，也不敢给他吃母乳，怕吃了母乳更加不吃奶粉，不知道怎么办了，急！"
                )
            )
            mList.add(
                AppQuestionTmp(
                    "妇产科",
                    "你好医生 我生完小孩一个月了 从孩子刚出生就没奶水 到现在也一直没有 一点涨的感觉都没有 乳房软软的 是不是有什么病呀 还有可能有奶水吗"
                )
            )
            mAutoPollRecyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mBannerAdapter
                mBannerAdapter?.setData(mList)
                isNestedScrollingEnabled = false
                start()
            }
        }

        if (mExpertAdapter == null) {
            mExpertAdapter = IndexExpertListAdapter()
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(mRecyclerView)
            //监听 滚动 获取具体位置
            mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (recyclerView != null && recyclerView.childCount > 0) {
                            mUIndicator.setSelectedItem(
                                (snapHelper.findSnapView(recyclerView.layoutManager)!!.layoutParams
                                        as RecyclerView.LayoutParams).viewAdapterPosition
                            )
                        }
                    }
                }
            })

            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            mRecyclerView.apply {

                this.layoutManager = layoutManager
                isNestedScrollingEnabled = false
                adapter = mExpertAdapter
            }
        }
        mExpertAdapter?.setNewData(qusetionList)
        mUIndicator.attachToBanner(qusetionList.size)

//        if (indexExpertBannerAdapter == null) {
//            indexExpertBannerAdapter = IndexExpertBannerAdapter()
//
//            mBanner.apply {
//                setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
//                    override fun onPageSelected(position: Int) {
//                        super.onPageSelected(position)
//                        mUIndicator.setSelectedItem(position)
//                    }
//                })
//                adapter = indexExpertBannerAdapter
//            }
//            mBanner.setAutoPlay(false)
////            mBanner.setAutoTurningTime(3000)
//            mBanner.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
//        }
//
//        if (!ListUtil.isEmpty(qusetionList)) {
//            indexExpertBannerAdapter!!.setAdapterData(qusetionList, context)
//            indexExpertBannerAdapter!!.notifyDataSetChanged()
//        }

        if (::listener.isInitialized && mItemHeight == 0) {
            holder.itemView.post {
                mItemHeight = holder.itemView.height + TransformUtil.newDp2px(
                    LibApplication.getAppContext(),
                    12f
                )
                listener.invoke(mItemHeight, this.javaClass.simpleName)
            }
        }
        holder.itemView.setOnClickListener {
//            var url =
//                "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick"
//            url = String.format(
//                url!!,
//                String(
//                    Base64.decode(
//                        SpUtils.getValue(context, SpKey.USER_ID).toByteArray(),
//                        Base64.DEFAULT
//                    )
//                )
//            )
//            ARouter.getInstance()
//                .build(IndexRoutes.INDEX_WEBVIEW)
//                .withBoolean("isNeedRef", true)
//                .withString("title", "极速问诊")
//                .withBoolean("isinhome", false)
//                .withBoolean("doctorshop", true)
//                .withString("url", url)
//                .navigation()
            ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_FAQ2)
                    .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE))
                    .navigation()
        }
    }

}