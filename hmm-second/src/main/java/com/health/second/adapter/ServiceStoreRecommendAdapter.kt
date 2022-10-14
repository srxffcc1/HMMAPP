package com.health.second.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.RecommendList
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.TransformUtil

/**
 * @author Long
 * @desc: 服务详情 -> 店铺推荐信息
 * @createTime :2021/10/9 14:13
 */
class ServiceStoreRecommendAdapter :
    BaseAdapter<String>(R.layout.item_service_store_recommend_layout) {

    var mRecommendList: MutableList<RecommendList>? = null
    var isLoadSuccess = false

    private lateinit var listener: (mViewHeight: Int) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (isLoadSuccess) return
        val mContainer = holder.setText(R.id.tv_title, "店铺推荐")
            .setOnClickListener(R.id.tv_lookAll, View.OnClickListener {
                if (isClickInit) {
                    moutClickListener.outClick("lookAllProject", "")// 查看更多项目
                }
            })
            .getView<LinearLayout>(R.id.ll_container)

        mRecommendList?.let {
            mContainer.removeAllViews()
            val mMargin = TransformUtil.dp2px(context, 36f).toInt();
            val w: Int = (ScreenUtils.getScreenWidth(context) - mMargin) / 3
            for (model in it) {
                val params =
                    ConstraintLayout.LayoutParams(w, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                val mView = LayoutInflater.from(context)
                    .inflate(
                        R.layout.item_service_store_recommend_child_layout,
                        mContainer,
                        false
                    )

                val mGoodsImg = mView.findViewById<ImageView>(R.id.goodsImg)
                val mGoodsName = mView.findViewById<TextView>(R.id.goodsName)
                val mGoodsPrice = mView.findViewById<TextView>(R.id.tv_goodsPrice)
                val mPinMoneySingle = mView.findViewById<TextView>(R.id.tv_pinMoneySingle)
                mGoodsName.text = model.goodsTitle
                mGoodsPrice.text = FormatUtils.moneyKeep2Decimals(model.platformPrice)
                mPinMoneySingle.text = "¥${FormatUtils.moneyKeep2Decimals(model.retailPrice)}"
                // 设置划线价样式

                mPinMoneySingle.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                GlideCopy.with(context).load(model.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    .into(mGoodsImg)

                mView.setOnClickListener {
                    if (isClickInit) {
                        moutClickListener.outClick("recommendProject", model)
                    }
                }
                val layoutParams = mGoodsImg.layoutParams
                layoutParams.width = w - TransformUtil.dp2px(context, 16f).toInt()
                layoutParams.height = w - TransformUtil.dp2px(context, 16f).toInt()
                mGoodsImg.layoutParams = layoutParams
                mContainer.addView(mView, params)
            }
            isLoadSuccess = true
        }

        if (::listener.isInitialized) {
            holder.itemView.post {
                listener.invoke(holder.itemView.height)
            }
        }
    }
}