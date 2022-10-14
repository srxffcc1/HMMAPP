package com.healthy.library.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

class EmptyAdapter @JvmOverloads constructor(viewId: Int = R.layout.empty_adapter_layout) :
    BaseAdapter<String?>(viewId) {
    private var mBackgroundColor = -1
    private var mEmptyImgResource = R.drawable.status_empty_normal
    private var mEmptyText = "暂无数据"
    private var mEmptyTextColor = Color.parseColor("#9596A4")
    private var mEmptyContentHeight = -1

    public fun setEmptyImg(imgResource:Int){
        this.mEmptyImgResource = imgResource
    }
    public fun setEmptyText(emptyText :String){
        this.mEmptyText = emptyText
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (mBackgroundColor != -1) {
            val view = holder.getView<View>(R.id.empty_content)
            view.setBackgroundColor(context.resources.getColor(mBackgroundColor))
        }
        if(mEmptyContentHeight != -1){
            val mEmptyContent = holder.getView<ConstraintLayout>(R.id.empty_content)
            val layoutParams = mEmptyContent.layoutParams
            layoutParams.height = mEmptyContentHeight
            mEmptyContent.layoutParams = layoutParams
        }

        val mEmptyImg = holder.getView<ImageView>(R.id.emptyImg)
        mEmptyImg.setImageResource(mEmptyImgResource)
        holder.setText(R.id.emptyTitle,mEmptyText)
            .setTextColor(R.id.emptyTitle,mEmptyTextColor)

    }

    fun setmBackgroundColor(mBackgroundColor: Int) {
        this.mBackgroundColor = mBackgroundColor
    }

    fun setEmptyTextColor(emptyTextColor:Int){
        this.mEmptyTextColor = emptyTextColor
    }

    fun setEmptyContentHeight(emptyContentHeight:Int){
        this.mEmptyContentHeight = emptyContentHeight
    }
}