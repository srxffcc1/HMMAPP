package com.health.index.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.*
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.routes.TencentLiveRoutes
import com.healthy.library.utils.TransformUtil
import org.greenrobot.eventbus.EventBus

/**
 * author : long
 * Time   :2021/12/14
 * desc   :
 */
class IndexRecommendAdapter : BaseAdapter<String>(R.layout.index_recommend_layout) {

    private var mItemHeight = 0
    private var sizeList = 0;
    private var mTitleList: MutableList<AppIndexTitle>? = null
    fun setTitleList(list: MutableList<AppIndexTitle>?) {
        this.mTitleList = list
    }

    private var mResultList: MutableList<MultiItemEntity>? = ArrayList()
    private var mRightVideoList: MutableList<LiveVideoMain>? = null
    fun setRightVideoList(list: MutableList<LiveVideoMain>?) {
        this.mRightVideoList = list
        checkShow();
    }

    fun checkShow() {
        if (mLeftGoodsList != null && mLeftGoodsList?.size!! > 0) {
            sizeList = 1
        }
        if (mRightVideoList != null && mRightVideoList?.size!! > 0) {
            if (sizeList == 1) {
                sizeList = 3;
            } else {
                sizeList = 2;
            }
        }
        mResultList?.clear()
        if (sizeList == 1) {//只有左边的
            mLeftGoodsList?.let { mResultList?.addAll(it) }
        } else if (sizeList == 2) {//只有右边的
            mRightVideoList?.let { mResultList?.addAll(it) }
        } else if (sizeList == 3) {//左右都有 就要判断数量了
            if (mLeftGoodsList!!.size >= 2) {
                mLeftGoodsList?.let { mResultList?.addAll(it.subList(0, 2)) }//把右边的加入
            } else {
                mLeftGoodsList?.let { mResultList?.addAll(it) }//把右边的加入
            }
            if (mRightVideoList!!.size >= 2) {
                mRightVideoList?.let { mResultList?.addAll(it.subList(0, 2)) }//把右边的加入
            } else {
                mRightVideoList?.let { mResultList?.addAll(it) }//把右边的加入
            }
        } else {

        }
        if (sizeList!! > 0) {
            model = "";
            notifyDataSetChanged()
        } else {
            model = null
            notifyDataSetChanged()
        }
    }

    private var mLeftGoodsList: MutableList<AppIndexList>? = null
    fun setLeftGoodsList(list: MutableList<AppIndexList>?) {
        this.mLeftGoodsList = list
        checkShow();
    }

    private lateinit var listener: (mViewHeight: Int, className: String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int, className: String) -> Unit) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return 7
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(), 12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (::listener.isInitialized && mItemHeight == 0) {
            holder.itemView.post {
                mItemHeight = holder.itemView.height + TransformUtil.newDp2px(
                    LibApplication.getAppContext(),
                    12f
                )
                listener.invoke(mItemHeight, this.javaClass.simpleName)
            }
        }
        val ll_recommend_content = holder.getView<LinearLayout>(R.id.ll_recommend_content)
        buildRecommandList(ll_recommend_content)
        if (sizeList == 1) {//只有左边的
            holder.setVisibility(R.id.liveLayout, View.GONE)

        } else if (sizeList == 2) {//只有右边的
            holder.setVisibility(R.id.recommendLayout, View.GONE)
        }
        if (ListUtil.isEmpty(mTitleList)) {
            holder.setText(R.id.tv_recommend_title, "品类推荐")
            holder.setText(R.id.tv_live_title, "热门直播")
        } else {
            holder.setText(R.id.tv_recommend_title, mTitleList!![0].mainTitle)
            holder.setText(R.id.tv_live_title, mTitleList!![1].mainTitle)
            holder.setText(R.id.iv_recommend_label, mTitleList!![0].subTitle)
            holder.setText(R.id.iv_live_label, mTitleList!![1].subTitle)
        }
    }

    fun buildRecommandList(linearLayout: LinearLayout) {
        linearLayout.removeAllViews()
        for (index in 0 until mResultList!!.size) {
            val item = mResultList!!.get(index)
            var view: View
            if (item.itemType == 0) {//0就是商品
                view = LayoutInflater.from(context)
                    .inflate(R.layout.index_recommend_goods_layout, linearLayout, false)
                var goodsMain: AppIndexList = mResultList!!.get(index) as AppIndexList
                GlideCopy.with(context)
                    .load(goodsMain.imageUrl)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(view.findViewById(R.id.iv_avatar))
                val tv_title = view.findViewById<TextView>(R.id.tv_title)
                val tvsub_title = view.findViewById<TextView>(R.id.tv_goods_label)
                tv_title.setText(goodsMain.mainTitle)
                tvsub_title.setText(goodsMain.subTitle)
                if (index == 0) {
                    tvsub_title.setTextColor(Color.parseColor("#FF5100"))
                } else {
                    tvsub_title.setTextColor(Color.parseColor("#B22CFF"))
                }
                view.setOnClickListener {
                    ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                        .withString("categoryId", goodsMain.categoryId)
                        .withString("categoryName", goodsMain.categoryName)
                        .navigation()
                }
            } else {//其他就是直播
                view = LayoutInflater.from(context)
                    .inflate(R.layout.index_recommend_live_layout, linearLayout, false)
                var liveVideoMain: LiveVideoMain = mResultList!!.get(index) as LiveVideoMain
                GlideCopy.with(context)
                    .load(liveVideoMain.picUrl)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(view.findViewById(R.id.iv_avatar))
                val tv_video_flag = view.findViewById<ImageView>(R.id.tv_video_flag)
                val tv_title = view.findViewById<TextView>(R.id.tv_title)
                tv_title.text = liveVideoMain.courseTitle
                if (liveVideoMain.status == 1) { //预告
                    tv_video_flag.visibility = View.GONE
                } else if (liveVideoMain.status == 2) { //直播中
                    tv_video_flag.visibility = View.VISIBLE
                    GlideCopy.with(context)
                        .load(R.drawable.icon_index_live)
                        .placeholder(R.drawable.icon_index_live)
                        .error(R.drawable.icon_index_live)
                        .into(tv_video_flag)
                } else { //回放
                    tv_video_flag.visibility = View.VISIBLE
                    GlideCopy.with(context)
                        .load(R.drawable.icon_index_live_history)
                        .placeholder(R.drawable.icon_index_live_history)
                        .error(R.drawable.icon_index_live_history)
                        .into(tv_video_flag)
                }
                view.setOnClickListener {
                    //预告
                    ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveNotice)
                        .withString("courseId", liveVideoMain.id)
                        .navigation()
//                    EventBus.getDefault().post(TabChangeModel(2))
                }
            }
            linearLayout.addView(view)
        }
        var needFixSize = 4 - mResultList!!.size
        for (index in 0 until needFixSize) {
            var view = LayoutInflater.from(context)
                .inflate(R.layout.index_recommend_goods_layout, linearLayout, false)
            view.visibility = View.INVISIBLE
            linearLayout.addView(view)
        }
    }

}