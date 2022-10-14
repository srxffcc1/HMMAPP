package com.health.city.adapter

import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.healthy.library.model.ActivityModel
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * @description
 * @author long
 * @date 2021/6/22
 */
class VoteHeaderAdapter : BaseAdapter<ActivityModel>(
    R.layout.city_item_vote_header_layout
) {


    var mImageUrl: String = ""
    var mVideoUrl: String = ""

    private var countDownTimer: CountDownTimer? = null
    private var mSeconds: Int = 0
    private var mDayView: TextView? = null
    private var mHoursView: TextView? = null
    private var mMinutesView: TextView? = null
    private var mSecondsView: TextView? = null

    fun setResource(imageUrl: String, mVideoUrl: String) {
        this.mImageUrl = imageUrl
        this.mVideoUrl = mVideoUrl
        notifyDataSetChanged()
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val activityModel = getModel()
        holder.setVisible(R.id.uploadLayout, "1" == activityModel?.isActivityStatus)
            .setVisible(R.id.voteTimeLayout, "2" == activityModel?.isActivityStatus)
            .setImg(R.id.iv_img_pic, mImageUrl, R.drawable.ic_upload_pic, R.drawable.ic_upload_pic)
            .setImg(R.id.iv_video, mVideoUrl, R.drawable.ic_upload_flv, R.drawable.ic_upload_flv)
            .setVisible(R.id.iv_img_del, !mImageUrl.isNullOrEmpty())
            .setVisible(R.id.iv_video_del, !mVideoUrl.isNullOrEmpty())
            .setVisible(R.id.isVideo, !mVideoUrl.isNullOrEmpty())
            .setOnClickListenerS(
                View.OnClickListener {
                    var mFunction = ""
                    when (it.id) {
                        R.id.iv_img_pic -> {
                            mFunction = "uploadImage"
                        }
                        R.id.iv_video ->
                            mFunction = if (TextUtils.isEmpty(mVideoUrl)) {
                                "uploadVideo"
                            } else {
                                "lookVideo"
                            }
                        R.id.iv_img_del -> {
                            mImageUrl = ""
                            mFunction = "deleteImage"
                            notifyDataSetChanged()
                        }
                        R.id.iv_video_del -> {
                            mVideoUrl = ""
                            mFunction = "deleteVideo"
                            notifyDataSetChanged()
                        }
                    }
                    if (isClickInit()) {
                        moutClickListener.outClick(mFunction, position)
                    }
                },
                R.id.uploadResources,
                R.id.uploadVideo,
                R.id.iv_img_pic,
                R.id.iv_video,
                R.id.iv_img_del,
                R.id.iv_video_del
            )

        when (activityModel?.isActivityStatus) {
            "1" -> {
                buildEnroll(holder, activityModel)
            }
            "2" -> {
                mDayView = holder.getView(R.id.days_tv)
                mHoursView = holder.getView(R.id.hours_tv)
                mMinutesView = holder.getView(R.id.minutes_tv)
                mSecondsView = holder.getView(R.id.seconds_tv)
                val time = 65025L
                downTimer(time)
            }
        }
    }

    /**
     * 加载报名header内容
     */
    private fun buildEnroll(
        holder: BaseHolder,
        activityModel: ActivityModel
    ) {
        holder.setVisible(R.id.uploadResources, true)
            .setVisible(R.id.uploadVideo, 2 == activityModel.votingType)
    }

    /**
     * 倒计时
     *
     * @param time
     */
    private fun downTimer(time: Long) {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        } else {
            //后台返回距离自动取消剩余：秒数
            mSeconds = time.toString().toInt()
        }
        if (mSeconds > 0) {
            countDownTimer = object : CountDownTimer((mSeconds * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val millisUntilFinished = millisUntilFinished / 1000
                    mSeconds = millisUntilFinished.toInt()
                    val day = (millisUntilFinished / (24 * 60 * 60 * 1000)).toInt()
                    val hours = (millisUntilFinished / (60 * 60)).toInt()
                    val minutes = (millisUntilFinished / 60 % 60).toInt()
                    val seconds = (millisUntilFinished % 60).toInt()

                    mDayView?.text = String.format("%02d", Math.max(0, day))
                    mHoursView?.text = String.format("%02d", Math.max(0, hours))
                    mMinutesView?.text = String.format("%02d", Math.max(0, minutes))
                    mSecondsView?.text = String.format("%02d", Math.max(0, seconds))
                }

                override fun onFinish() {
                    //剩余支付时间结束后进行相应逻辑处理
                    countDownTimer?.cancel()
                }
            }.start()
        }
    }
}