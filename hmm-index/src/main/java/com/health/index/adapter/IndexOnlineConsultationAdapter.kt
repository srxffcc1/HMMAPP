package com.health.index.adapter

import android.util.Base64
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.constant.SpKey
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/20
 * desc   :
 */
class IndexOnlineConsultationAdapter : BaseAdapter<String>(R.layout.index_online_consultation) {

    override fun getItemViewType(position: Int): Int {
        return 15
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(),12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginBottom = TransformUtil.newDp2px(LibApplication.getAppContext(),12f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

//        val mOnLineNumber = holder.getView<TextView>(R.id.tv_onlineNum)
        val mSubTitle = holder.getView<TextView>(R.id.tv_subtitle)

//        mOnLineNumber.text = "9872"
        mSubTitle.text = HtmlCompat.fromHtml(
            "预计3分钟接通专家，仅需<font color=\"#FF5100\">9.9</font>元",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        holder.itemView.setOnClickListener {

            //System.out.println(SpUtils.getValue(mContext, SpKey.USER_ID));
            var url =
                "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick"
            url = String.format(
                url!!,
                String(
                    Base64.decode(
                        SpUtils.getValue(context, SpKey.USER_ID).toByteArray(),
                        Base64.DEFAULT
                    )
                )
            )
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW)
                .withBoolean("isNeedRef", true)
                .withString("title", "极速问诊")
                .withBoolean("isinhome", false)
                .withBoolean("doctorshop", true)
                .withString("url", url)
                .navigation()
        }
    }
}