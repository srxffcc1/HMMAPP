package com.health.faq.valaroutadapter

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.health.faq.R
import com.health.faq.model.RewardQuestion
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.FormatUtils


/**
 * @author xinkai.xu
 * @sample 悬赏求助
 *
 * */
class RewardForHelpAdapter(context: Context, mutableList: MutableList<RewardQuestion>, helper: LayoutHelper, mViewTypeItem: Int) : DelegateAdapter.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var mHelper: LayoutHelper = helper
    private var mData: MutableList<RewardQuestion>? = null

    private var context: Context? = null
    private var mViewTypeItem = -1
    private var view: View? = null

    init {
        this.mData = mutableList
        this.context = context
        this.mHelper = helper
        this.mViewTypeItem = mViewTypeItem
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == mViewTypeItem) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_reward_for_help, parent, false)
        }


        return RecyclerViewItemHolder(view!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val rvHolder = holder as RecyclerViewItemHolder
        val model: RewardQuestion = mData!![position]
        context?.let {
            com.healthy.library.businessutil.GlideCopy.with(it)
                .asBitmap()
                .load(model.faceUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .centerCrop()
                .into(object : BitmapImageViewTarget(holder.ivHeader) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context!!.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        holder.ivHeader.setImageDrawable(circularBitmapDrawable)
                    }
                })
        }

        //昵称
        rvHolder.tvName.text = model.nickName//昵称
        rvHolder.tvType.text = model.currentStatus//当前状态
        rvHolder.tvMoney.text = "￥${FormatUtils.formatRewardMoney(model.rewardMoney.toString())}"//钱
        rvHolder.tvQuestion.text = model.introduction//问题详情
        /*
        * 1：备孕 2：怀孕 3：产后
        * */
        when (model.currentStatusType) {
            1 -> rvHolder.tvType.background = context!!.getDrawable(R.drawable.shape_period_1)
            2 -> rvHolder.tvType.background = context!!.getDrawable(R.drawable.shape_period_2)
            3 -> rvHolder.tvType.background = context!!.getDrawable(R.drawable.shape_period_3)
        }
        rvHolder.clRewardHelp.setOnClickListener {

            println("跳问答详情:4")
            ARouter.getInstance()
                    .build(FaqRoutes.FAQ_QUESTION_DETAIL)
                    .withString("questionId", model.id.toString())
                    .navigation()
        }


    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return mViewTypeItem
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private inner class RecyclerViewItemHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var ivHeader: ImageView = itemView.findViewById(R.id.ivHeader)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvType: TextView = itemView.findViewById(R.id.tvType)
        var tvMoney: TextView = itemView.findViewById(R.id.tvMoney)
        var tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        var clRewardHelp: ConstraintLayout = itemView.findViewById(R.id.clRewardHelp)

    }
}
