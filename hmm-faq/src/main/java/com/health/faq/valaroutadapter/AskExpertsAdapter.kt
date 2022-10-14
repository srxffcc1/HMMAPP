package com.health.faq.valaroutadapter

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.health.faq.model.Item
import com.healthy.library.routes.FaqRoutes


/**
 * @author xinkai.xu
 * 问专家
 * */

class AskExpertsAdapter(context: Context, strings: MutableList<Item>, helper: LayoutHelper, viewTypeItem: Int)
    : DelegateAdapter.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val mHelper: LayoutHelper = helper
    private var mData: MutableList<Item>? = null
    private var view: View? = null
    private var context: Context? = null
    private var mViewTypeItem = -1
    var onAskListener: OnAskListener? = null

    init {
        this.mData = strings
        this.context = context
        this.mViewTypeItem = viewTypeItem
    }


    override fun onCreateLayoutHelper(): LayoutHelper {
        return mHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == mViewTypeItem) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_ask_experts, parent, false)
        }
        return RecyclerViewItemHolder(view!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RecyclerViewItemHolder
        when (position % 4) {
            0 -> {
                holder.vBg.background = context!!.resources.getDrawable(R.drawable.bg_expert_card_one, null)
                holder.ivQ.setImageDrawable(context!!.getDrawable(R.drawable.buleq))
            }
            1 -> {
                holder.vBg.background = context!!.resources.getDrawable(R.drawable.bg_expert_card_two, null)
                holder.ivQ.setImageDrawable(context!!.getDrawable(R.drawable.purpleq))
            }
            2 -> {
                holder.vBg.background = context!!.resources.getDrawable(R.drawable.bg_expert_card_three, null)
                holder.ivQ.setImageDrawable(context!!.getDrawable(R.drawable.redq))
            }
            3 -> {
                holder.vBg.background = context!!.resources.getDrawable(R.drawable.bg_expert_card_four, null)
                holder.ivQ.setImageDrawable(context!!.getDrawable(R.drawable.orangeq))
            }
        }
        var item: Item = mData!![position]
        viewHolder.ivAskHer.setOnClickListener {
            //向他提问
            onAskListener?.onClickListener(item.expertId.toString())


        }
        viewHolder.tvLookQuestion.setOnClickListener {
            //问答详情
            println("跳问答详情1")
            ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                    .withString("questionId", item.questionId.toString())
                    .withInt("pos",position)
                    .withBoolean("index",true)
                    .navigation()
        }
        mData?.let {
            //头像
            context?.let { it1 ->
                com.healthy.library.businessutil.GlideCopy.with(it1)
                        .asBitmap()
                        .load(it[position].faceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)
                        
                        .centerCrop()
                        .into(object : BitmapImageViewTarget(viewHolder.ivHeader) {
                            override fun setResource(resource: Bitmap?) {
                                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context!!.resources, resource)
                                circularBitmapDrawable.isCircular = true
                                viewHolder.ivHeader.setImageDrawable(circularBitmapDrawable)
                            }
                        })
            }
            viewHolder.tvQuestions.text = it[position].detail
            viewHolder.tvName.text = "${it[position].realName}  ${it[position].rank}"
            viewHolder.tvLookCount.text = "${it[position].readCount}人查看"
        }


    }

    interface OnAskListener {
        fun onClickListener(id: String)
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

    fun getData(): MutableList<Item>? {
        return mData
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private inner class RecyclerViewItemHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var vBg: View = itemView.findViewById(R.id.vBg)

        var ivAskHer: ImageView = itemView.findViewById(R.id.ivAskHer)//向他提问
        var tvQuestions: TextView = itemView.findViewById(R.id.tvQuestions)//问题
        var ivHeader: ImageView = itemView.findViewById(R.id.ivHeader)//专家头像
        var tvName: TextView = itemView.findViewById(R.id.tvName)//专家名字，职称
        var tvLookQuestion: TextView = itemView.findViewById(R.id.tvLookQuestion)//问答详情
        var tvLookCount: TextView = itemView.findViewById(R.id.tvLookCount)//查看次数
        var ivQ: ImageView = itemView.findViewById(R.id.ivQ)


    }
}
