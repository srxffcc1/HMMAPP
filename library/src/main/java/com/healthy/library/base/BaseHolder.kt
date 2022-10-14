package com.healthy.library.base

import android.content.Context
import android.graphics.Typeface
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.bumptech.glide.Glide

/**
 * @author Li
 * @date 2019-08-15 16:58
 * @des
 */
@Suppress("UNCHECKED_CAST")
open class BaseHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    private var context: Context = itemView.context

    private var sparseArray = SparseArray<View>()
    fun <T : View> getView(viewId: Int): T {
        if (sparseArray[viewId] == null) {
            sparseArray.put(viewId, itemView.findViewById(viewId))
        }
        return sparseArray[viewId] as T
    }

    fun setOnClickListener(id: Int, listener: View.OnClickListener):BaseHolder {
        getView<View>(id).setOnClickListener(listener)
        return this
    }

    fun setOnClickListenerS(listener: View.OnClickListener, vararg ids: Int):BaseHolder {
        ids.forEach {
            getView<View>(it).setOnClickListener(listener)
        }
        return this
    }

    fun setText(id: Int, txt: CharSequence?):BaseHolder {
        getView<TextView>(id).text = txt
        return this
    }

    fun setImage(id: Int, res: Int):BaseHolder {
        getView<ImageView>(id).setImageResource(res)
        return this
    }

    fun setBackGround(id: Int, res: Int):BaseHolder {
        getView<View>(id).setBackgroundResource(res)
        return this
    }

    fun setTextColor(id: Int, color: Int):BaseHolder {
        getView<TextView>(id).setTextColor(color)
        return this
    }

    fun setTextColorRes(id: Int, color: Int):BaseHolder {
        getView<TextView>(id).setTextColor(context.resources.getColor(color))
        return this
    }

    fun setTextSize(id: Int, size: Float):BaseHolder {
        getView<TextView>(id).textSize = size
        return this
    }

    fun setTextTypeface(id: Int, typeface: Typeface):BaseHolder {
        getView<TextView>(id).typeface = typeface
        return this
    }

    fun setVisibility(id: Int, visibility: Int):BaseHolder{
        getView<View>(id).visibility = visibility
        return this
    }
    fun setVisibility(id: Int, visibility: Boolean):BaseHolder{
        getView<View>(id).visibility = if(visibility){ View.VISIBLE}else{View.INVISIBLE}
        return this
    }

    fun setImg(id: Int, url: String) : BaseHolder{
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(url)
                
                .into(getView(id))
        return this
    }

    fun setImg(id: Int, url: Int) : BaseHolder{
        com.healthy.library.businessutil.GlideCopy.with(context)
            .load(url)
            
            .into(getView(id))
        return this
    }

    fun setImg(id: Int, url: String, errorImgId: Int, placeholderImgId: Int): BaseHolder  {
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(url)
                .error(errorImgId)
                .placeholder(placeholderImgId)
                .into(getView(id))
        return this
    }

    fun setVisible(@IdRes viewId: Int, visible: Boolean): BaseHolder {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }
}