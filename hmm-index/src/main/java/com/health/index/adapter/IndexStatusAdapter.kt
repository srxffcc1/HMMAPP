package com.health.index.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.health.index.R
import com.health.index.interfaces.IndexListener
import com.health.index.model.IndexBean
import com.health.index.model.IndexStatusRecyclerBean
import com.healthy.library.model.UserInfoMonModel
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.ParseUtils
import com.healthy.library.utils.TransformUtil
import java.util.*

/**
 * desc: 调整后此适配器只有两种状态  备孕中（1）、怀孕中（1）
 */
class IndexStatusAdapter(var mContext: Context) :
    BaseAdapter<IndexBean?>(R.layout.index_mon_header_for_pregnancy) {
    private var memberSex = "1" //默认1男2女
    private val status = 0
    private var mIndexListener: IndexListener? = null
    var count = 0
    private var userInfoMonModel: UserInfoMonModel? = null
    private var oldDate: Date? = null
    private var mDate: Date? = null
    private var areaNo: String? = null
    private var indexStatusChildAdapter: IndexStatusChildAdapter? = null
    private var clickCount = 0 //TODO 后续删除

    private var mItemHeight = 0
    private lateinit var listener: (mViewHeight: Int, className: String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int, className: String) -> Unit) {
        this.listener = listener
    }

    fun setAreaNo(areaNo: String?) {
        this.areaNo = areaNo
    }

    fun setUserInfoMonModel(userInfoMonModel: UserInfoMonModel?) {
        this.userInfoMonModel = userInfoMonModel
    }

    fun setMemberSex(memberSex: String) {
        this.memberSex = memberSex
    }

    fun setOnIndexListener(mIndexListener: IndexListener?) {
        this.mIndexListener = mIndexListener
    }

    fun revertDate() {
        mDate = oldDate
    }

    fun getOldDate(): Date {
        return if (oldDate == null) Date() else oldDate!!
    }

    fun setmDate(mDate: Date?) {
        oldDate = this.mDate
        this.mDate = mDate
    }

    val date: Date
        get() = if (mDate == null) Date() else mDate!!


    override fun getItemViewType(position: Int): Int {
        return 2
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(baseHolder: BaseHolder, i: Int) {
        val indexBean = model
        val mGroup = baseHolder.getView<Group>(R.id.group1)
        val mClParentingOrPregnant =
            baseHolder.getView<ConstraintLayout>(R.id.cl_ParentingOrPregnant)
        var mTvDay = baseHolder.getView<TextView>(R.id.tv_day)
        val mNumberValueOne = baseHolder.getView<TextView>(R.id.tv_numberValue_one)
        val mNumberValueOneTitle = baseHolder.getView<TextView>(R.id.tv_numberValue_one_Title)
        val mNumberValueTwo = baseHolder.getView<TextView>(R.id.tv_numberValue_two)
        val mNumberValueTwoTitle = baseHolder.getView<TextView>(R.id.tv_numberValue_two_Title)
        val mIvStatusPicture = baseHolder.getView<ImageView>(R.id.iv_status_picture)
        val mRecyclerView = baseHolder.getView<ViewPager2Banner>(R.id.recycler_pregnancy)
        baseHolder.setImage(
            R.id.iv_status_bg,
            when {
                indexBean!!.status == 1 -> R.drawable.index_probability_render_bg
                indexBean.status == 2 -> R.drawable.index_pregnancy_bg
                else -> R.drawable.index_boby_bg
            }
        )
        mGroup.visibility = if (indexBean.status == 1) View.VISIBLE else View.GONE
        mClParentingOrPregnant.visibility =
            if (indexBean.status == 2 || indexBean.status == 3) View.VISIBLE else View.GONE

        //备孕
        System.out.println("adapter状态:" + indexBean.status)
        if (indexBean.status == 1) {
            val mIvCircle = baseHolder.getView<ImageView>(R.id.iv_circle)
            mTvDay = baseHolder.getView(R.id.tv_probability_render_day)
            val mProbabilityRender = baseHolder.getView<TextView>(R.id.tv_probability_render)
            val mPregnancyContent = baseHolder.getView<TextView>(R.id.tv_pregnancy_content)

            Glide.with(mContext)
                .asGif()
                .load(R.drawable.index_probability_render_circle_bg)
                .into(mIvCircle)

            mTvDay.text = DateUtils.long2Str(System.currentTimeMillis(), "MM月dd日")
            try {
                if (indexBean.pregnancyPrepareHomeDataResult == null) {
                    mPregnancyContent.text = "排卵期期间，频繁啪啪并不会增加受孕几率。隔一天同房是最佳频率，既不会太疲劳，又可以“养精蓄锐”。"
                } else {
                    mPregnancyContent.text = indexBean.pregnancyPrepareHomeDataResult.content
                }
                mProbabilityRender.text =
                    (ParseUtils.parseFloat(indexBean.pregnancyPrepareHomeDataResult.pregnancyProbability)).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //怀孕
        else if (indexBean.status == 2) {
            mNumberValueOne.text = "0"
            mNumberValueTwo.text = "0"
            if (indexBean.babyAndMomChangeResult != null) {
                if (indexBean.babyAndMomChangeResult.getWeight().contains("-")) {
                    try {
                        mNumberValueOne.text =
                            if (indexBean.babyAndMomChangeResult != null)
                                indexBean.babyAndMomChangeResult.getWeight().replace(
                                    "g",
                                    ""
                                ).split("-")[1] else "0"
                    } catch (e: Exception) {
                    }
                } else {
                    try {
                        mNumberValueOne.text =
                            if (indexBean.babyAndMomChangeResult != null)
                                indexBean.babyAndMomChangeResult.getWeight().replace(
                                    "g",
                                    ""
                                ).split("-")[0] else "0"
                    } catch (e: Exception) {
                    }
                }
            }

            if (indexBean.babyAndMomChangeResult != null) {
                if (indexBean.babyAndMomChangeResult.getCrownRumpLength().contains("-")) {
                    try {
                        mNumberValueTwo.text =
                            if (indexBean.babyAndMomChangeResult != null)
                                indexBean.babyAndMomChangeResult.getCrownRumpLength().replace(
                                    "cm",
                                    ""
                                ).split("-")[1] else "0"
                    } catch (e: Exception) {
                    }
                } else {
                    try {
                        mNumberValueTwo.text =
                            if (indexBean.babyAndMomChangeResult != null)
                                indexBean.babyAndMomChangeResult.getCrownRumpLength().replace(
                                    "cm",
                                    ""
                                ).split("-")[0] else "0"
                    } catch (e: Exception) {
                    }
                }
            }
            mNumberValueOneTitle.text = "胎重(g)"
            mNumberValueTwoTitle.text = "身长(cm)"
            val layoutParams = mNumberValueTwo.layoutParams as ConstraintLayout.LayoutParams
            if (mNumberValueOne.text.toString() == "0" || mNumberValueTwo.text.toString() == "0") {
                layoutParams.leftToRight = R.id.tv_numberValue_one_Title
            } else {
                layoutParams.leftToRight = R.id.tv_numberValue_one
            }
            mNumberValueTwo.layoutParams = layoutParams

            val layoutParams1 = mIvStatusPicture.layoutParams as ConstraintLayout.LayoutParams
            layoutParams1.width = TransformUtil.newDp2px(mContext, 80f)
            layoutParams1.height = TransformUtil.newDp2px(mContext, 100f)
            layoutParams1.rightMargin = TransformUtil.newDp2px(mContext, 38f)
            mIvStatusPicture.layoutParams = layoutParams1

            Glide.with(mContext)
                .asGif()
                .load(R.drawable.index_pregnancy_first_trimester)
                .into(mIvStatusPicture)

//            mIvStatusPicture.setOnClickListener {
//                clickCount++
//                val drawable = when (clickCount % 3) {
//                    1 -> R.drawable.index_pregnancy_second_trimester
//                    2 -> R.drawable.index_pregnancy_late_pregnancy
//                    else -> R.drawable.index_pregnancy_first_trimester
//
//                }
//                Glide.with(mContext)
//                    .asGif()
//                    .load(drawable)
//                    .into(mIvStatusPicture)
//            }

            mTvDay.text =
                if (indexBean.babyAndMomChangeResult != null) indexBean.babyAndMomChangeResult.gestationDay else ""
            try {
                if (indexStatusChildAdapter == null) {
                    indexStatusChildAdapter = IndexStatusChildAdapter()
                    mRecyclerView.setPageMargin(
                        0,
                        TransformUtil.newDp2px(mContext, 38f),
                        TransformUtil.newDp2px(mContext, 10f)
                    )
                    mRecyclerView.isAutoPlay = true
                    mRecyclerView.setAutoTurningTime(5000)
                    mRecyclerView.adapter = indexStatusChildAdapter
                }
                val statusRecyclerBeans: MutableList<IndexStatusRecyclerBean> = ArrayList()
                val mon = IndexStatusRecyclerBean()
                val son = IndexStatusRecyclerBean()
                mon.content = indexBean.babyAndMomChangeResult.momContent
                mon.type = 0
                mon.mon2son = 0
                try {
                    mon.imageurl = userInfoMonModel!!.faceUrl
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                son.content = indexBean.babyAndMomChangeResult.babyContent
                son.type = 0
                son.mon2son = 1
                son.imageurl = indexBean.babyAndMomChangeResult.babyImage
                statusRecyclerBeans.add(son)
                statusRecyclerBeans.add(mon)
                indexStatusChildAdapter?.setClasszz("孕期")
                indexStatusChildAdapter?.date = date
                indexStatusChildAdapter?.setNewData(statusRecyclerBeans)

            } catch (e: Exception) {
                mRecyclerView.visibility = View.GONE
                e.printStackTrace()
            }
        }
        //育儿
        else if (indexBean.status == 3) {
            mNumberValueOne.text =
                indexBean.postpartumRecoveryResult.weight.replace("kg", "").split("-")[1]
            mNumberValueTwo.text =
                indexBean.postpartumRecoveryResult.height.replace("cm", "").split("-")[1]
            mNumberValueOneTitle.text = "体重(kg)"
            mNumberValueTwoTitle.text = "身高(cm)"
            val layoutParams = mNumberValueTwo.layoutParams as ConstraintLayout.LayoutParams
            if (mNumberValueOne.text.toString() == "0" || mNumberValueTwo.text.toString() == "0") {
                layoutParams.leftToRight = R.id.tv_numberValue_one_Title
            } else {
                layoutParams.leftToRight = R.id.tv_numberValue_one
            }
            mNumberValueTwo.layoutParams = layoutParams

            val layoutParams1 = mIvStatusPicture.layoutParams as ConstraintLayout.LayoutParams
            layoutParams1.width = TransformUtil.newDp2px(mContext, 68f)
            layoutParams1.height = TransformUtil.newDp2px(mContext, 89f)
            layoutParams1.rightMargin = TransformUtil.newDp2px(mContext, 50f)
            mIvStatusPicture.layoutParams = layoutParams1

            Glide.with(mContext)
                .asGif()
                .load(R.drawable.index_status_parenting)
                .into(mIvStatusPicture)

            try {
                mTvDay.text = "宝宝\t" + indexBean.postpartumRecoveryResult.babyDay
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                if (indexStatusChildAdapter == null) {
                    indexStatusChildAdapter = IndexStatusChildAdapter()
                    mRecyclerView.setPageMargin(
                        0,
                        TransformUtil.newDp2px(mContext, 38f),
                        TransformUtil.newDp2px(mContext, 10f)
                    )
                    mRecyclerView.isAutoPlay = true
                    mRecyclerView.setAutoTurningTime(5000)
                    mRecyclerView.adapter = indexStatusChildAdapter
                }
                val statusRecyclerBeans: MutableList<IndexStatusRecyclerBean> = ArrayList()
                val mon = IndexStatusRecyclerBean()
                val son = IndexStatusRecyclerBean()
                mon.content = indexBean.postpartumRecoveryResult.momContent
                mon.mon2son = 0
                mon.type = 0
                try {
                    mon.imageurl = userInfoMonModel!!.faceUrl
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                son.content = indexBean.postpartumRecoveryResult.content
                son.type = 1
                son.mon2son = 1
                son.imageurl = indexBean.postpartumRecoveryResult.babyImage
                son.weight = indexBean.postpartumRecoveryResult.weight
                son.height = indexBean.postpartumRecoveryResult.height
                statusRecyclerBeans.add(son)
                statusRecyclerBeans.add(mon)
                indexStatusChildAdapter?.setClasszz("产后")
                indexStatusChildAdapter?.date = date
                indexStatusChildAdapter?.setNewData(statusRecyclerBeans)

            } catch (e: Exception) {
                mRecyclerView.visibility = View.GONE
                e.printStackTrace()
            }
        }

        if (::listener.isInitialized && mItemHeight == 0) {
            baseHolder.itemView.post {
                mItemHeight = baseHolder.itemView.height
                listener.invoke(mItemHeight, this.javaClass.simpleName)
            }
        }

        /* baseHolder.getView(R.id.iv_day_decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIndexListener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(getDate());
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    mIndexListener.onDateDecrease(calendar.getTime());
                    setmDate(calendar.getTime());
                }
            }
        });
        baseHolder.getView(R.id.iv_day_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIndexListener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(getDate());
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mIndexListener.onDateIncrease(calendar.getTime());
                    setmDate(calendar.getTime());
                }
            }
        });*/
    }
}