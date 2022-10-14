package com.health.city.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.google.android.material.tabs.TabLayout

/**
 * 创建日期：2021/11/5 17:27
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.widget
 * 类说明：
 */

public class IndicatorView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null)

    private var bindTabLayout: TabLayout? = null
    var indicatorWidth = 0f
    var indicatorheight = -1f
    private var indicatorLeft = 0f
    private var lastIndicatorLeft = -1f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private var lastSelectedPosition = 0

    private val animator = ValueAnimator.ofFloat()

    fun setIndicatorColor(color: Int) {
        paint.color = color
    }

    fun setupWithTabLayout(tabLayout: TabLayout) {
        bindTabLayout = tabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                lastSelectedPosition = tab!!.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                calculate()
            }
        })

        tabLayout.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_UP){
                handler.postDelayed({
                    calculateScroll()
                },300)
            }
            return@setOnTouchListener false
        }
        // 这种方式可能会看到从最左边滚动到初始位置，改为在onSizeChange中计算初始位置
//        tabLayout.post {
//            calculate()
//        }
    }
    // 计算指示器的目标位置
    private fun calculate(){
        val selectedTabPosition = bindTabLayout?.selectedTabPosition ?: -1
        if (selectedTabPosition >= 0) {
            // mTabStrip是TabLayout的子view，他是TabView的直接父View，
            val mTabStrip = bindTabLayout?.getChildAt(0)
            if (mTabStrip is ViewGroup?) {
                mTabStrip?.let {

                    // 计算tab要滚动多少
                    var targetScrollX = calculateScrollXForTab(selectedTabPosition,0f)
                    // tab的父view的长度
                    val tabStripWidth = it.measuredWidth
                    // 计算滚动的实际距离，滚动不能越界
                    if (targetScrollX + bindTabLayout!!.measuredWidth > tabStripWidth){
                        // 滚动到最右边，滚动最大值就是 tab的父view的长度 - tabLayout的长度
                        targetScrollX = tabStripWidth - bindTabLayout!!.measuredWidth
                    }else if (targetScrollX < 0){
                        // 滚动到最左边，滚动最小值为0，不能为负数
                        targetScrollX = 0
                    }
                    val tabView = mTabStrip.getChildAt(selectedTabPosition)
                    tabView?.let {
                        indicatorLeft = it.left + (it.measuredWidth - indicatorWidth) / 2 - (targetScrollX )
                        if (lastIndicatorLeft == -1f){
                            lastIndicatorLeft = indicatorLeft
                            postInvalidate()
                        }else{
                            animator()
                        }
                    }
                }
            }
        }
    }

    private fun calculateScroll(){
        val selectedTabPosition = bindTabLayout?.selectedTabPosition ?: -1
        if (selectedTabPosition >= 0) {
            // mTabStrip是TabLayout的子view，他是TabView的直接父View，
            val mTabStrip = bindTabLayout?.getChildAt(0)
            val currentScrollX = bindTabLayout?.scrollX?:0
            if (mTabStrip is ViewGroup?) {
                mTabStrip?.let {

                    val tabView = mTabStrip.getChildAt(selectedTabPosition)
                    tabView?.let {
                        indicatorLeft = it.left - currentScrollX + (it.measuredWidth - indicatorWidth) / 2
                        animator()
                    }
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 计算起始位置
        val selectedTabPosition = bindTabLayout?.selectedTabPosition ?: -1
        if (selectedTabPosition >= 0) {
            val mTabStrip = bindTabLayout?.getChildAt(0)
            mTabStrip?.also {
                // 计算tab要滚动多少
                var targetScrollX = calculateScrollXForTab(selectedTabPosition, 0f)
                // tab的父view的长度
                val tabStripWidth = it.measuredWidth
                // 计算滚动的实际距离，滚动不能越界
                if (targetScrollX + bindTabLayout!!.measuredWidth > tabStripWidth) {
                    // 滚动到最右边，滚动最大值就是 tab的父view的长度 - tabLayout的长度
                    targetScrollX = tabStripWidth - bindTabLayout!!.measuredWidth
                } else if (targetScrollX < 0) {
                    // 滚动到最左边，滚动最小值为0，不能为负数
                    targetScrollX = 0
                }
                if (mTabStrip is ViewGroup?) {
                    val tabView = mTabStrip.getChildAt(selectedTabPosition)
                    tabView?.let {
                        indicatorLeft = it.left + (it.measuredWidth - indicatorWidth) / 2 - (targetScrollX)
                        lastIndicatorLeft = indicatorLeft
                    }
                }
            }
        }
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (indicatorheight == -1f) indicatorheight = measuredHeight.toFloat()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rectF.set(indicatorLeft, measuredHeight - indicatorheight, indicatorLeft + indicatorWidth, measuredHeight.toFloat())
        canvas?.drawRoundRect(rectF, indicatorheight / 2, indicatorheight / 2, paint)
    }

    private fun animator() {
        if (animator.isRunning){
            animator.cancel()
        }
        animator.setFloatValues(lastIndicatorLeft, indicatorLeft)
        animator.duration = 300L
        animator.addUpdateListener { animation ->
            indicatorLeft = animation?.animatedValue as Float
            lastIndicatorLeft = indicatorLeft
            postInvalidate()
        }
        animator.start()
    }

    /**
     * 计算TabLayout要滚动多少，预期值，不是实际距离。
     * 此方法是从TabLayout类中复制过来的。选中指定position的tab，预期是，把此tab移动到中间位置
     * 此方法就是计算这个预期值的
     */
    private fun calculateScrollXForTab(position: Int, positionOffset: Float): Int {
        if (bindTabLayout?.tabMode == TabLayout.MODE_SCROLLABLE) {
            bindTabLayout?.let {
                val mTabStrip = it.getChildAt(0) as ViewGroup
                val selectedChild = mTabStrip.getChildAt(position)
                val nextChild = if (position + 1 < mTabStrip.getChildCount())
                    mTabStrip.getChildAt(position + 1)
                else
                    null
                val selectedWidth = selectedChild?.width ?:0
                val nextWidth = nextChild?.width ?:0

                // base scroll amount: places center of tab in center of parent
                val scrollBase = selectedChild!!.getLeft() + selectedWidth / 2 - it.width / 2
                // offset amount: fraction of the distance between centers of tabs
                val scrollOffset = ((selectedWidth + nextWidth).toFloat() * 0.5f * positionOffset).toInt()

                return if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                    scrollBase + scrollOffset
                else
                    scrollBase - scrollOffset
            }

        }
        return 0
    }

}