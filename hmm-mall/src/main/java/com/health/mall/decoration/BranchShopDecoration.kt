package com.health.mall.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.healthy.library.utils.TransformUtil

/**
 * @author Li
 * @date 2019-08-07 11:20
 * @des 分店分割线
 */
class BranchShopDecoration(var context: Context) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private var paint: Paint = Paint()
    private var leftPadding: Int
    private var topPadding: Int
    private var rightPadding: Int
    private var bottomPadding: Int

    init {
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#E4E8EE")
        paint.strokeWidth = TransformUtil.dp2px(context, 1f)
        leftPadding = TransformUtil.dp2px(context, 20f).toInt()
        topPadding = TransformUtil.dp2px(context, 21f).toInt()
        rightPadding = TransformUtil.dp2px(context, 33f).toInt()
        bottomPadding = TransformUtil.dp2px(context, 15f).toInt()
    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val bottom = view.bottom
            c.drawLine(0f, bottom.toFloat() + bottomPadding,
                    parent.width.toFloat(), bottom.toFloat() + bottomPadding, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(leftPadding, topPadding, rightPadding, bottomPadding)
    }
}