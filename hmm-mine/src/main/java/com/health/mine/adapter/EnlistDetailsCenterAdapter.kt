package com.health.mine.adapter

import android.widget.FrameLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.mine.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.widget.X5WebView
import com.tencent.smtt.sdk.CookieSyncManager

/**
 * @description
 * @author long
 * @date 2021/7/23
 */
class EnlistDetailsCenterAdapter
    : BaseAdapter<String>(R.layout.mine_recyclerview_enlist_item_center) {

    private var mWebView: X5WebView? = null
    private var mContentLayout: FrameLayout? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        getModel()?.let {
            val sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                    "initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />" +
                    "<style>img{max-width:100% !important;height:auto}</style>" +
                    "<style>body{max-width:100% !important;word-break:break-all;}</style></head><body>"
            val body = it
            //如存在P标签进行替换 (报名换行移除样式不好看,无换行了）
//            if (!TextUtils.isEmpty(body) && body.contains("<p>")) {
//                //P标签是双标签需替换两次开口和闭口
//                body = body.replace("<p>", "").replace("</p>", "")
//            }
            val data = "$sHead$body</body></html>"
            mContentLayout = holder.getView(R.id.content_layout)
            mWebView = X5WebView(context, null)
            mWebView?.isVerticalFadingEdgeEnabled = false
            mWebView?.isHorizontalScrollBarEnabled = false
            mContentLayout?.addView(
                mWebView!!,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            //mWebView = holder.getView(R.id.webView)
            mWebView?.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)
            //holder.setText(R.id.tv_center_body, HtmlCompat.fromHtml(it,HtmlCompat.FROM_HTML_MODE_LEGACY))
        }
    }

    fun onDestroy() {
        if (mWebView != null) {
            mContentLayout?.removeAllViews()
            mWebView?.destroy()
            mWebView = null
        }
    }
}