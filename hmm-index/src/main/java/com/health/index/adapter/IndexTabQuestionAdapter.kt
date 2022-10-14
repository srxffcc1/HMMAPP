package com.health.index.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.FaqExportQuestion
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.ImageSpanCentre

class IndexTabQuestionAdapter private constructor(viewId: Int) :
    BaseAdapter<FaqExportQuestion?>(viewId) {

    override fun getItemViewType(position: Int): Int {
        return 19
    }

    constructor() : this(R.layout.index_mon_question) {}

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.bgColor = Color.WHITE
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(baseHolder: BaseHolder, i: Int) {
        val indexAllQuestion = datas[i]!!
        val cityNo = LocUtil.getCityNo(context, SpKey.LOC_ORG)
        try {
            baseHolder.setVisibility(
                R.id.ivTip,
                if (cityNo == indexAllQuestion.addressCity) View.VISIBLE else View.INVISIBLE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val spannableString = SpannableString("  ${indexAllQuestion.introduction}")
        val drawable = context.resources.getDrawable(com.healthy.library.R.drawable.index_ask)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val imageSpan = ImageSpanCentre(drawable, ImageSpanCentre.CENTRE)
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        baseHolder.setText(R.id.tvQuestions, spannableString)
            .setText(R.id.ivName, indexAllQuestion.realName)
            .setText(R.id.ivYear, indexAllQuestion.ranks)
            .setText(R.id.ivSee, indexAllQuestion.readCount.toString())

        val ivHeader = baseHolder.getView<ImageView>(R.id.ivHeader)
        if (indexAllQuestion.faceUrl != null) {

            val apply = RequestOptions()
                .error(R.drawable.img_avatar_default)
                .placeholder(R.drawable.img_avatar_default)
                .transform(CircleCrop())

            Glide.with(context)
                .load(indexAllQuestion.faceUrl)
                .apply(apply)
                .into(ivHeader)
        }

        val mQuestionContent = baseHolder.getView<ConstraintLayout>(R.id.question_content)
        mQuestionContent.setPadding(
            0, 0, 0,
            if (i == datas.size - 1) 0 else TransformUtil.dp2px(context, 12f).toInt()
        )

        baseHolder.itemView.setOnClickListener { //System.out.println("跳问答详情4");
            ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                .withString("questionId", indexAllQuestion.questionId.toString())
                .withInt("pos", i)
                .withBoolean("index", true)
                .navigation()
        }
    }
}