package com.health.second.adapter

import android.text.TextUtils
import android.widget.FrameLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.JsBridge
import com.healthy.library.utils.ResizeImgWebViewClient
import com.healthy.library.utils.WebViewSetting
import com.healthy.library.widget.X5WebView

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/9 14:48
 */
class ServiceGoodsDetailAdapter :
    BaseAdapter<String>(R.layout.item_service_goods_detail_layout) {

    private var mWebView: X5WebView? = null
    private var mContentLayout: FrameLayout? = null
    var isLoadSuccess = false

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    private val resizeImgWebViewClient: ResizeImgWebViewClient =
        object : ResizeImgWebViewClient() {}

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        /*val mGoodsImg = holder.getView<ImageView>(R.id.goodsImg)
        val mContainer = holder.getView<LinearLayout>(R.id.ll_container)*/
        if (isLoadSuccess) return
        isLoadSuccess = true
        mContentLayout = holder.getView(R.id.content_layout)
        mWebView = X5WebView(context, null)
        mContentLayout?.addView(
            mWebView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        mWebView?.apply {
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            webViewClient = resizeImgWebViewClient
            //setLayerType(View.LAYER_TYPE_NONE, null)
            settings?.javaScriptEnabled = true
            addJavascriptInterface(JsBridge(), "JsBridge")
            WebViewSetting.setWebViewParam(mWebView, context)
        }

        val sHead =
            "<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />" +
                    "<style>img{max-width:100% !important;height:auto}</style><style>body{max-width:100% !important;word-break:break-all;}</style></head><body>"
        var body: String = model
        //如存在P标签进行替换
        if (!TextUtils.isEmpty(body) && body.contains("<p>")) {
            //P标签是双标签需替换两次开口和闭口
            body = body.replace("<p>", "").replace("</p>", "")
        }
        val data = "$sHead$body</body></html>"
        mWebView!!.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null)

        /*GlideCopy.with(mGoodsImg.context)
            .load("https://img0.baidu.com/it/u=2398434160,746234797&fm=26&fmt=auto")
            .into(mGoodsImg)
        for (index in 0..20) {
            val mView = LayoutInflater.from(context)
                .inflate(R.layout.item_service_goods_detail_project_layout, mContainer, false)
            val mProjectName = mView.findViewById<TextView>(R.id.tv_projectName)
            val mProjectPrice = mView.findViewById<TextView>(R.id.tv_projectPrice)
            val mProjectDesc = mView.findViewById<TextView>(R.id.tv_projectDesc)
            mProjectDesc.visibility = if (index % 2 == 0) View.GONE else View.VISIBLE
            mProjectPrice.text = "¥${100 * index}"
            mContainer.addView(mView)
        }*/
    }

    fun onDestroy() {
        if (mWebView != null) {
            mContentLayout!!.removeAllViews()
            mWebView?.removeAllViews()
            mWebView?.destroy()
            mWebView = null
        }
    }
}