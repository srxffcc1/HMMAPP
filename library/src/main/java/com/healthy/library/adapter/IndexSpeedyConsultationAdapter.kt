package com.healthy.library.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_ShapeView.view.ShapeTextView
import com.healthy.library.LibApplication
import com.healthy.library.R
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.AppQuestionTmp
import com.healthy.library.routes.FaqRoutes

/**
 * author : long
 * Time   :2021/12/13
 * desc   :
 */
class IndexSpeedyConsultationAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private var mData: MutableList<AppQuestionTmp>? = mutableListOf()

    fun setData(data: MutableList<AppQuestionTmp>) {
        mData = if (ListUtil.isEmpty(data)) mutableListOf() else data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.index_item_speedy_consultation_layout, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val stv_Label = holder.getView<ShapeTextView>(R.id.stv_Label)
        val content = mData!![position % mData!!.size]?.content
        val clazz = mData!![position % mData!!.size]?.clazz
        holder.setText(R.id.tv_body, content)
        holder.setText(R.id.stv_Label, clazz)
        stv_Label.setTextColor(
            Color.parseColor(
                if (clazz.contentEquals("儿科")) {
                    "#1568FF"
                } else {
                    "#FF156C"
                }
            )
        )
        stv_Label.shapeDrawableBuilder.setSolidColor(
            Color.parseColor(
                if (clazz.contentEquals("儿科")) {
                    "#1A0092FF"
                } else {
                    "#1AFF156C"
                }
            )
        )
            .intoBackground()
    }

}