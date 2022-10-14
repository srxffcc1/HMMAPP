package com.health.index.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.PostDetail
import com.healthy.library.model.VideoListModel
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.ParseUtils

class IndexMonthlyVideoAdapter :
    BaseAdapter<String>(R.layout.index_item_monthly_video_adapter) {

    private var indexMonthlyVideoListAdapter: IndexMonthlyVideoListAdapter? = null
    private var records: MutableList<VideoListModel>? = null
    private var postDetail: PostDetail? = null
    private var shareListener: ShareVideoListener? = null

    public fun setShareVideoListener(data: ShareVideoListener) {
        this.shareListener = data
    }

    public fun setAdapterData(data: MutableList<VideoListModel>) {
        this.records = data
    }

    public fun setPKData(data: PostDetail?) {
        this.postDetail = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val videoRecyclerView = holder.getView<RecyclerView>(R.id.videoRecyclerView)
        if (indexMonthlyVideoListAdapter == null) {
            indexMonthlyVideoListAdapter = IndexMonthlyVideoListAdapter()
            videoRecyclerView.apply {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(videoRecyclerView)
                this.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL, false
                )
                adapter = indexMonthlyVideoListAdapter
            }
            indexMonthlyVideoListAdapter!!.setShareVideoListener(object :
                IndexMonthlyVideoListAdapter.ShareVideoListener {
                override fun shareVideo(id: String, title: String, bitmap: Bitmap) {
                    shareListener!!.shareVideo(id, title, bitmap)
                }
            })
        }
        if (!ListUtil.isEmpty(records)) {
            indexMonthlyVideoListAdapter!!.setData(records!! as ArrayList<VideoListModel>?)
        }

        if (postDetail != null && postDetail?.pkActivity != null) {
            holder.setVisibility(R.id.pkLayout,View.VISIBLE)
            try {
                if (TextUtils.isEmpty(postDetail?.pkActivity?.squareImgUrl) || TextUtils.isEmpty(
                        postDetail?.pkActivity?.conSideImgUrl
                    )
                ) {
                    holder.setVisibility(R.id.tabs, View.GONE)
                } else {
                    holder.setVisibility(R.id.tabs, View.VISIBLE)
                }
            } catch (e: Exception) {
            }
            postDetail?.pkActivity?.squareImgUrl?.let { holder.setImg(R.id.iv_pkVoting_square, it) }
            postDetail?.pkActivity?.conSideImgUrl?.let {
                holder.setImg(
                    R.id.iv_pkVoting_conSide,
                    it
                )
            }
            holder.setText(R.id.postingTitle, postDetail?.pkActivity?.activityTitle)
            holder.setText(
                R.id.tipContent,
                postDetail!!.postingRichContent
            )
            holder.setText(
                R.id.pkVoting_popularity,
                ParseUtils.parseNumber(postDetail?.pkActivity?.initialPopularity, 10000, "万") + "人气"
            )
            if (TextUtils.isEmpty(postDetail?.pkActivity?.currentUserLastVoteStand)) {
                holder.setText(
                    R.id.tv_pkVoting_square_name,
                    postDetail?.pkActivity?.squareTitle
                )
                holder.setText(
                    R.id.tv_pkVoting_conSide_name,
                    postDetail?.pkActivity?.conSideTitle
                )
                holder.setVisibility(R.id.square_percentage, View.GONE)
                holder.setVisibility(R.id.conSide_percentage, View.GONE)
            } else {
                holder.setVisibility(R.id.square_percentage, View.VISIBLE)
                holder.setVisibility(R.id.conSide_percentage, View.VISIBLE)
                if (postDetail?.pkActivity?.currentUserLastVoteStand.equals("1")) {
                    holder.setText(
                        R.id.tv_pkVoting_square_name,
                        postDetail?.pkActivity?.squareTitle
                    )
                    holder.setText(
                        R.id.tv_pkVoting_conSide_name,
                        postDetail?.pkActivity?.conSideTitle
                    )
                    holder.setText(
                        R.id.square_percentage,
                        postDetail?.pkActivity?.squarePercentage.toString() + "%(我投)"
                    )
                    holder.setText(
                        R.id.conSide_percentage,
                        postDetail?.pkActivity?.conSidePercentage.toString() + "%"
                    )
                } else {
                    holder.setText(
                        R.id.tv_pkVoting_square_name,
                        postDetail?.pkActivity?.squareTitle
                    )
                    holder.setText(
                        R.id.tv_pkVoting_conSide_name,
                        postDetail?.pkActivity?.conSideTitle
                    )
                    holder.setText(
                        R.id.square_percentage,
                        postDetail?.pkActivity?.squarePercentage.toString() + "%"
                    )
                    holder.setText(
                        R.id.conSide_percentage,
                        postDetail?.pkActivity?.conSidePercentage.toString() + "%(我投)"
                    )
                }
            }
            var mPkVotingTime = holder.getView<AppCompatTextView>(R.id.pkVoting_time_title)
            val mActivityEndTime = DateUtils.str2Long(
                postDetail?.pkActivity?.activityEndTime,
                DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
            )
            val mActivityStartTime = DateUtils.str2Long(
                postDetail?.pkActivity?.activityStartTime,
                DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
            )
            val time = (mActivityEndTime - System.currentTimeMillis()) / 1000 //转换为秒
            val startTime = (mActivityStartTime - System.currentTimeMillis()) / 1000 //转换为秒

            if (startTime < 0) {
                if ("1" == postDetail?.pkActivity?.status && mActivityEndTime > System.currentTimeMillis()) {
                    checkTimeOut(mPkVotingTime, time)
                } else {
                    mPkVotingTime.setText("活动已结束")
                }
            } else {
                mPkVotingTime.setText("活动未开始")
            }
            holder.setOnClickListener(R.id.squareLayout, View.OnClickListener {
                if (isClickInit) {
                    moutClickListener.outClick("voteStand", "1")
                }
            })
            holder.setOnClickListener(R.id.conSideLayout, View.OnClickListener {
                if (isClickInit) {
                    moutClickListener.outClick("voteStand", "2")
                }
            })
        }else{
            holder.setVisibility(R.id.pkLayout,View.GONE)
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    public interface ShareVideoListener {
        fun shareVideo(id: String, title: String, bitmap: Bitmap)
    }

    private fun checkTimeOut(mPkVotingTime: TextView, time: Long) {
        try {
            var countDownTimer = object : CountDownTimer(if (time <= 0) 0 else time * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var millisUntilFinished = millisUntilFinished
                    millisUntilFinished = millisUntilFinished / 1000
                    val day = millisUntilFinished.toInt() / (24 * 60 * 60)
                    val hours = (millisUntilFinished / (60 * 60) - day * 24).toInt()
                    val minutes = millisUntilFinished.toInt() / 60 - day * 24 * 60 - hours * 60
                    val seconds =
                        millisUntilFinished.toInt() - day * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60
                    val mTimeText: String
                    mTimeText = if (day > 0) {
                        (String.format("%02d", Math.max(0, day)) + ":"
                                + String.format("%02d", Math.max(0, hours)) + ":"
                                + String.format("%02d", Math.max(0, minutes)) + ":"
                                + String.format("%02d", Math.max(0, seconds)))
                    } else {
                        (String.format("%02d", Math.max(0, hours)) + ":"
                                + String.format("%02d", Math.max(0, minutes)) + ":"
                                + String.format("%02d", Math.max(0, seconds)))
                    }
                    mPkVotingTime.text = "倒计时\t" + mTimeText
                }

                override fun onFinish() {
                    //剩余支付时间结束后进行相应逻辑处理
                    mPkVotingTime.text = "活动已结束"
                }
            }
            countDownTimer.start()
        } catch (e: Exception) {
        }
    }

}