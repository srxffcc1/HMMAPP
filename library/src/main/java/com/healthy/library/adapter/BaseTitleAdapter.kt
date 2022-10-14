package com.healthy.library.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.layout.ShapeConstraintLayout
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.TitleModel
import com.healthy.library.utils.TransformUtil

/**
 * 公用标题
 *
 * @author: long
 * @date: 2021/4/15
 * @des
 */
class BaseTitleAdapter() :
    BaseAdapter<TitleModel?>(R.layout.mall_top_store_layout) {

    private var marginHorizontal: Int = 0
    private var marginTop: Int = 0

    constructor(marginHorizontal: Int) : this() {
        this.marginHorizontal = marginHorizontal
    }

    constructor(marginHorizontal: Int, marginTop: Int) : this(marginHorizontal) {
        this.marginTop = marginTop
    }

    override fun getItemViewType(position: Int): Int {
        return 16
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        if (marginHorizontal != 0) {
            linearLayoutHelper.marginLeft = marginHorizontal
            linearLayoutHelper.marginRight = marginHorizontal
        }
        if (marginTop != 0) {
            //linearLayoutHelper.marginTop = marginTop
        }
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val mTvTitle = holder.getView<TextView>(R.id.tv_title)
        val mTvRight = holder.getView<TextView>(R.id.textViewEnd)
        val mTitleLayout = holder.getView<ShapeConstraintLayout>(R.id.title_layout)
        val mIvTitleIcon = holder.getView<ImageView>(R.id.iv_title_icon)
        val model = getModel()
        val backgroundDrawable = model!!.backgroundDrawable
        val solidColor = model.solidColor
        val resources = context.resources
        mTvRight.setCompoundDrawablesWithIntrinsicBounds(
            null, null, if (model.isDrawableRightShow) context.resources.getDrawable(
                model.drawableRight
            ) else null, null
        )
        if (!TextUtils.isEmpty(model.title)) {
            if (model.isHtmlText) {
                val spanned = HtmlCompat.fromHtml(model.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
                mTvTitle.text = spanned
            } else mTvTitle.text = model.title
        }
        if (!TextUtils.isEmpty(model.rightTitle)) {
            mTvRight.text = model.rightTitle
        }
        mTvRight.visibility = if (model.isShowRight) View.VISIBLE else View.GONE
        if (backgroundDrawable != -1) mTitleLayout.background =
            resources.getDrawable(backgroundDrawable)

        if (solidColor != 0) {
            mTitleLayout.shapeDrawableBuilder
                .setSolidColor(solidColor)
                .intoBackground()
        }
        mIvTitleIcon.visibility = if (model.titleIcon != 0) View.VISIBLE else View.GONE
        if (model.titleIcon != 0) {
            mIvTitleIcon.setImageResource(model.titleIcon)
        }

        if (model.isRadius) {
            mTitleLayout.shapeDrawableBuilder
                .setRadius(TransformUtil.dp2px(context, model.radius.toFloat()))
                .intoBackground()
        } else {
            if (model.topLeftRadius != 0 && model.topRightRadius != 0) {
                mTitleLayout.shapeDrawableBuilder
                    .setTopLeftRadius(TransformUtil.dp2px(context, model.topLeftRadius.toFloat()))
                    .setTopRightRadius(TransformUtil.dp2px(context, model.topRightRadius.toFloat()))
                    .intoBackground()
            }
            if (model.bottomLeftRadius != 0 && model.bottomRightRadius != 0) {
                mTitleLayout.shapeDrawableBuilder
                    .setBottomLeftRadius(
                        TransformUtil.dp2px(
                            context,
                            model.bottomLeftRadius.toFloat()
                        )
                    )
                    .setBottomRightRadius(
                        TransformUtil.dp2px(
                            context,
                            model.bottomRightRadius.toFloat()
                        )
                    )
                    .intoBackground()
            }
        }
        mTvRight.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick(model.rightTitle, "")
            }
        }
        holder.itemView.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("itemClick", "")
            }
        }
    }
}