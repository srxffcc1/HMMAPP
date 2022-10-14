package com.health.index.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.IndexMenu
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.TransformUtil

class IndexFunctionGridAdapter : BaseAdapter<String?>(R.layout.index_function_content) {
    private var indexMenus: List<IndexMenu>? = null
    private var mItemHeight = 0

    private lateinit var listener: (mViewHeight: Int,className:String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int,className:String) -> Unit) {
        this.listener = listener
    }

    fun setIndexMenus(indexMenus: List<IndexMenu>) {
        this.indexMenus = indexMenus
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return 3
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val gridLayoutHelper = GridLayoutHelper(5)
//        gridLayoutHelper.setMargin(
//            TransformUtil.dp2px(LibApplication.getAppContext(), 10f).toInt(),
//            TransformUtil.dp2px(LibApplication.getAppContext(), 20f).toInt(),
//            0, 0
//        )
        return gridLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, i: Int) {
        val mGridLayout = holder.getView<GridLayout>(R.id.functionGrid)
        mGridLayout.removeAllViews()
        if (!ListUtil.isEmpty(indexMenus)) {
            addFunctions(context, mGridLayout, indexMenus)

            if(::listener.isInitialized && mItemHeight == 0){
                holder.itemView.post{
                    mItemHeight = holder.itemView.height + TransformUtil.dp2px(LibApplication.getAppContext(), 20f).toInt()
                    listener.invoke(mItemHeight,this.javaClass.simpleName)
                }
            }
        }
    }

    private fun addFunctions(context: Context, gridLayout: GridLayout, urls: List<IndexMenu>?) {
        gridLayout.removeAllViews()
        var columnCount = 5
        val splictreal = urls!!.size / 2.0
        val splictrealstring = (splictreal.toString() + "").split("\\.".toRegex()).toTypedArray()
        columnCount = if (splictrealstring.size > 1 && splictrealstring[1].toInt() > 0) {
            (urls.size / 2) + 1
        } else {
            (urls.size / 2)
        }
        if (urls.size <= 10) {
            columnCount = 5
        }
        if (urls.size <= 5) {
            columnCount = 5
        }
        gridLayout.columnCount = columnCount
        gridLayout.rowCount = 2
        val needsize = urls.size
        for (i in 0 until needsize) {
            val indexMenu = urls[i]
            val pos = i

            //params.bottomp = TransformUtil.newDp2px(context, TransformUtil.dp2px(context, 5f));
            val view =
                LayoutInflater.from(context).inflate(R.layout.index_mon_function, gridLayout, false)
            val stv_function_Subscript = view.findViewById<ShapeTextView>(R.id.stv_function_Subscript)
//            if(i %2==0){
//                view.setBackgroundColor(Color.parseColor("#1C63FF"))
//            }else{
//                view.setBackgroundColor(Color.parseColor("#FF3A7F"))
//            }
            val params = view.layoutParams
            params.width = ScreenUtils.getScreenWidth(context) / 5
            if(indexMenu?.imageRes==0){
                GlideCopy.with(context)
                    .load(indexMenu?.imageResString)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into((view.findViewById<View>(R.id.iv_category) as ImageView))
            }else{
                GlideCopy.with(context)
                    .load(indexMenu?.imageRes)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into((view.findViewById<View>(R.id.iv_category) as ImageView))
            }
            if(TextUtils.isEmpty(indexMenu?.subscript)){
                stv_function_Subscript.visibility=View.INVISIBLE
            }else{
                stv_function_Subscript.visibility=View.VISIBLE
                stv_function_Subscript.setText(indexMenu?.subscript)
            }
            val tv_category = view.findViewById<TextView>(R.id.tv_category)
            tv_category.text = indexMenu.settingname
            view.setOnClickListener {
                if (moutClickListener != null) {
                    moutClickListener.outClick(indexMenu.name, indexMenu.appIndexCustomItem)
                }
            }
            gridLayout.addView(view, params)
        }
    }
}