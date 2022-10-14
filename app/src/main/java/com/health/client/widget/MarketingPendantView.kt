package com.health.client.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.health.client.R
import com.healthy.library.LibApplication
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.AppIndexMarketingPendant
import com.healthy.library.utils.GlideOptions
import com.healthy.library.utils.MARouterUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CornerImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


/**
 * author : long
 * Time   :2021/12/6
 * desc   : 首页营销挂件View
 */
class MarketingPendantView : ConstraintLayout {

    private var mContext: Context? = null
    private var mIvLogo: ImageView? = null
    private var mClMarketingPendant: ConstraintLayout? = null
    private var mLlCustomize: LinearLayout? = null
    private var ivMarketingPendantBg: ImageView? = null
    private var mShapeView: View? = null
    private var mSwitch: ImageView? = null
    private var mAnimator: Animator? = null
    private var mAnimatorTime: Long = 300
    private var mCustomizeList: MutableList<AppIndexMarketingPendant>? = null

    /**是否显示*/
    var isShow: Boolean = true

    /** 是否正在执行动画*/
    var isExecute: Boolean = false

    fun setList(mCustomizeList: MutableList<AppIndexMarketingPendant>) {
        this.mCustomizeList = mCustomizeList
        initCustomize()
    }

    constructor(context: Context) : super(context, null) {
        mContext = context
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        mContext = context
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val resources = context.resources

        val mView =
            LayoutInflater.from(context).inflate(R.layout.widget_marketing_pendant_layout, this)
        ivMarketingPendantBg = mView.findViewById<ImageView>(R.id.iv_marketing_pendant_bg)

        //Logo
        mIvLogo = mView.findViewById(R.id.iv_marketingPendantLogo)
        //外层Layout
        mClMarketingPendant = mView.findViewById(R.id.cl_marketingPendant)
        //自定义营销挂件存放View
        mLlCustomize = mView.findViewById(R.id.ll_customize_marketingPendant)
        mShapeView = mView.findViewById(R.id.shapeView)
        //营销挂件开关
        mSwitch = mView.findViewById(R.id.iv_marketingPendantSwitch)

        initListener()
    }

    private fun initListener() {
        mIvLogo?.setOnClickListener { }
        mSwitch?.setOnClickListener {
            mAnimator?.let {
                if (it.isStarted) {
                    return@setOnClickListener
                }
            }
            if (mClMarketingPendant?.visibility == VISIBLE) {
                endAnimator()
            } else {
                startAnimator()
            }
        }
    }

    /**
     * 自定义营销挂件填充
     */
    private fun initCustomize() {
        mLlCustomize?.removeAllViews()
        this.post {
            val mWidthPixels = mLlCustomize!!.width
            for ((index, e) in mCustomizeList!!.withIndex()) {
                var view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_app_index_marketing_pendant_layout, null)
                var iv_avatar = view.findViewById<ImageView>(R.id.iv_avatar)
                var title = view.findViewById<TextView>(R.id.title)
                var content = view.findViewById<TextView>(R.id.content)
                var itemParams = mWidthPixels / 4
                var layoutParams = LinearLayout.LayoutParams(itemParams,itemParams)
                view.layoutParams=layoutParams
                title.text = e.mainTitle
                content.text = e.subTitle
                mContext?.let {
                    GlideCopy.with(it)
                        .load(e.imageUrl)
                        .apply(GlideOptions.withRoundedOptions(TransformUtil.dp2px(mContext, 12f).toInt(), RoundedCornersTransformation.CornerType.ALL))
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(iv_avatar)
                }
                view.setOnClickListener {
                    MARouterUtils.passToTarget(mContext, e)
                }
                mLlCustomize?.addView(view)
            }

//
//            mCustomizeList?.forEachIndexed { index, s ->
//                val imageView = ImageView(mContext)
//                val itemParams = mWidthPixels / 4
//                val layoutParams = LinearLayout.LayoutParams(itemParams, itemParams)
//                layoutParams.gravity = Gravity.CENTER_VERTICAL
//                imageView.layoutParams = layoutParams
//                if (s.imageUrl.contains("gif")) {
//                    Glide.with(imageView.context)
//                        .asGif()
//                        .load(s.imageUrl)
//                        .error(R.drawable.img_1_1_default)
//                        .placeholder(R.drawable.img_1_1_default)
//                        .into(imageView)
//                } else {
//                    Glide.with(imageView.context)
//                        .load(s.imageUrl)
//                        .error(R.drawable.img_1_1_default)
//                        .placeholder(R.drawable.img_1_1_default)
//                        .into(imageView)
//                }
//                imageView.setOnClickListener {
//                    MARouterUtils.passToTarget(imageView.context, s)
//                }
//                mLlCustomize?.addView(imageView)
//            }
        }
    }

    /**
     * 开启（展开）动画
     */
    @SuppressLint("ObjectAnimatorBinding")
    fun startAnimator() {
        if (mClMarketingPendant?.visibility!! == VISIBLE) {
            return
        }
        mAnimator =
            ObjectAnimator.ofFloat(mClMarketingPendant, "translationX", this.right.toFloat(), 0f)
        mAnimator?.apply {
            duration = mAnimatorTime
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    mClMarketingPendant?.visibility = VISIBLE
                    mShapeView?.visibility = GONE
                    mSwitch?.alpha = 0.8f
                    isExecute = true
                    isShow = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isExecute = false
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
            start()
        }
    }

    /**
     * 关闭（收起）动画
     */
    @SuppressLint("ObjectAnimatorBinding")
    fun endAnimator() {
        if (mClMarketingPendant?.visibility!! == GONE) {
            return
        }
        mAnimator = ObjectAnimator.ofFloat(
            mClMarketingPendant, "translationX",
            0f, this.right.toFloat()
        )
        mAnimator?.apply {
            duration = mAnimatorTime
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    mSwitch?.alpha = 1f
                    isExecute = true
                    isShow = false
                }

                override fun onAnimationEnd(animation: Animator?) {
                    mClMarketingPendant?.visibility = GONE
                    mShapeView?.visibility = VISIBLE
                    isExecute = false
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
            start()
        }
    }
}