package com.health.client.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.View
import com.healthy.library.utils.TransformUtil

/**
 * @author Li
 * @date 2019-08-12 11:46
 * @des
 */
class GuideDotView : View {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gap: Int = TransformUtil.dp2px(context, 5f).toInt()
    private var dotNum = 1
    private var selectedIndex = 0
    private val normalColor = Color.parseColor("#E4E4EC")
    private val selectedColor = Color.parseColor("#FF94A3")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(
                height * dotNum + (dotNum - 1) * gap, MeasureSpec.getMode(widthMeasureSpec)
        ), heightMeasureSpec)
    }

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {

        val radius = height * 0.5f
        for (i in 0 until dotNum) {
            val x = gap * i + radius * 2 * i + radius
            paint.color = if (i == selectedIndex) selectedColor else normalColor
            canvas?.drawCircle(x, radius, radius, paint)
        }
    }

    fun setupViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                selectedIndex = p0
                invalidate()
            }
        })
        viewPager.adapter?.let {
            dotNum = it.count
            requestLayout()
        }

    }
}