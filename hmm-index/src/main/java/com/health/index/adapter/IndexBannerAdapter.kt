package com.health.index.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.banner.BannerAdapter
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.AdModel
import com.healthy.library.model.AppIndexCustomItem
import com.healthy.library.model.TabChangeModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.MARouterUtils
import org.greenrobot.eventbus.EventBus

/**
 * author : long
 * Time   :2021/12/8
 * desc   :
 */
class IndexBannerAdapter : BaseAdapter<String>(R.layout.index_home_banner_layout) {

    var indexMenus: MutableList<AppIndexCustomItem> = ArrayList<AppIndexCustomItem>()
    private var mTopMargin = 0
    private var mFunctionLayout: RelativeLayout? = null
    private var mBannerAdapter: BannerAdapter? = null
    private var selectedPos = 0
    private var mStatus = 0
    private var mItemHeight = 0
    private var bannerImgs: MutableList<AdModel>? = null

    private lateinit var listener: (mViewHeight: Int, className: String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int, className: String) -> Unit) {
        this.listener = listener
    }

    fun setStatus(status: Int) {
        this.mStatus = status
    }

    fun setTopMargin(mTopMargin: Int) {
        this.mTopMargin = mTopMargin
    }

    fun setBannerImgs(bannerImgs: MutableList<AdModel>?) {
        this.bannerImgs = bannerImgs
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        mFunctionLayout = holder.getView(R.id.cl_posting)
        mFunctionLayout?.let {
            val layoutParams = it.layoutParams as ConstraintLayout.LayoutParams
            if (layoutParams.topMargin != mTopMargin) {
                layoutParams.topMargin = mTopMargin
                it.layoutParams = layoutParams
            }
        }
        if (!ListUtil.isEmpty(indexMenus)) {
            holder.setText(R.id.tv_postingTile, indexMenus.get(0).settingName)
                .setText(R.id.tv_serviceTitle, indexMenus.get(1).settingName)
                .setText(R.id.tv_parentingTitle, indexMenus.get(2).settingName)
                .setText(R.id.tv_shoppingTitle, indexMenus.get(3).settingName)
            try {
                if (indexMenus.get(0).subscript == null) {
                    holder.setVisibility(R.id.stv_posting_Subscript, false)
                } else {
                    holder.setVisibility(R.id.stv_posting_Subscript, true)
                    holder.setText(R.id.stv_posting_Subscript, indexMenus.get(0).subscript)
                }
                if (indexMenus.get(1).subscript == null) {
                    holder.setVisibility(R.id.stv_service_Subscript, false)
                } else {
                    holder.setVisibility(R.id.stv_service_Subscript, true)
                    holder.setText(R.id.stv_service_Subscript, indexMenus.get(1).subscript)
                }
                if (indexMenus.get(2).subscript == null) {
                    holder.setVisibility(R.id.stv_parenting_Subscript, false)
                } else {
                    holder.setVisibility(R.id.stv_parenting_Subscript, true)
                    holder.setText(R.id.stv_parenting_Subscript, indexMenus.get(2).subscript)
                }
                if (indexMenus.get(3).subscript == null) {
                    holder.setVisibility(R.id.stv_shopping_Subscript, false)
                } else {
                    holder.setVisibility(R.id.stv_shopping_Subscript, true)
                    holder.setText(R.id.stv_shopping_Subscript, indexMenus.get(3).subscript)
                }
            } catch (e: Exception) {
            }
        }
        val mBanner = holder.getView<ViewPager2Banner>(R.id.banner)
        if (mStatus in 1..3) {
            holder.setVisibility(R.id.iv_status_bg, View.VISIBLE)
        } else {
            holder.setVisibility(R.id.iv_status_bg, View.GONE)
        }
        if (mBannerAdapter == null) {
            mBannerAdapter = BannerAdapter()

            mBanner.apply {
                setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        selectedPos = position
                    }
                })
                adapter = mBannerAdapter
            }
            mBanner.setAutoTurningTime(3000)
            mBannerAdapter?.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setDimensionRatio("375:300")
                setOnItemClickListener {
                    MARouterUtils.passToTarget(context, bannerImgs!![selectedPos])
                    moutClickListener.outClick("banner", bannerImgs!![selectedPos].id)
                }
            }
            if (::listener.isInitialized && mItemHeight == 0) {
                holder.itemView.post {
                    mItemHeight = holder.itemView.height
                    listener.invoke(mItemHeight, this.javaClass.simpleName)
                }
            }
        }
        if (ListUtil.isEmpty(bannerImgs)) {
            bannerImgs = mutableListOf()
            bannerImgs?.add(AdModel("R.drawable.home_default_bg", "/service/PointsSignIn"))
        } else {
            mBannerAdapter?.setData(listOf<Any>(*bannerImgs?.toTypedArray()!!))
            mBannerAdapter?.notifyDataSetChanged()
        }

        holder.setOnClickListenerS(View.OnClickListener {
            if (!isClickInit) {
                return@OnClickListener
            }
            when (it.id) {
                R.id.cl_posting -> {
                    if (ListUtil.isEmpty(indexMenus)) {
                        EventBus.getDefault().post(TabChangeModel(1))
                    } else {
                        moutClickListener.outClick(
                            indexMenus[0].initialName,
                            indexMenus[0].androidUrl
                        )
                    }
                }
                R.id.cl_service -> {
                    if (ListUtil.isEmpty(indexMenus)) {
                        ARouter.getInstance()
                            .build(SecondRoutes.MAIN_MODULE)
                            .navigation()
                    } else {
                        moutClickListener.outClick(
                            indexMenus[1].initialName,
                            indexMenus[1].androidUrl
                        )
                    }
                }
                R.id.cl_parenting -> {
                    if (ListUtil.isEmpty(indexMenus)) {
                        ARouter.getInstance()
                            .build(IndexRoutes.INDEX_INDEXBABY)
                            .navigation()
                    } else {
                        moutClickListener.outClick(
                            indexMenus[2].initialName,
                            indexMenus[2].androidUrl
                        )
                    }
                }
                R.id.cl_shopping -> {
                    if (ListUtil.isEmpty(indexMenus)) {
                        EventBus.getDefault().post(TabChangeModel(3))
                    } else {
                        moutClickListener.outClick(
                            indexMenus[3].initialName,
                            indexMenus[3].androidUrl
                        )
                    }
                }
            }
        }, R.id.cl_posting, R.id.cl_service, R.id.cl_parenting, R.id.cl_shopping)
    }

}