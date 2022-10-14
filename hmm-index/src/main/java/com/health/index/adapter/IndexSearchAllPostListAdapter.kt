package com.health.index.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.adapter.PostAdapter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.message.IndexSearchInfo
import com.healthy.library.model.PostDetail
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

/**
 * 创建日期：2022/1/4 14:17
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchAllPostListAdapter :
    BaseAdapter<String>(R.layout.index_search_all_post_list_adapter_layout),
    BaseAdapter.OnOutClickListener,
    PostAdapter.OnPostFansClickListener,
    PostAdapter.OnPostLikeClickListener,
    PostAdapter.OnShareClickListener {

    private var key: String? = null
    private var records: MutableList<PostDetail>? = null
    private var recyclerView: RecyclerView? = null
    private var mPostAdapter: PostAdapter? = null//帖子
    private var onShareClickListener: OnShareClickListener? = null
    private var onPostFansClickListener: OnPostFansClickListener? = null
    private var onPostLikeClickListener: OnPostLikeClickListener? = null

    public fun setKey(key: String) {
        this.key = key
    }

    public fun setAdapterData(data: MutableList<PostDetail>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        recyclerView = holder.getView<RecyclerView>(R.id.recyclerView)
        var more = holder.getView<AppCompatImageView>(R.id.more)
        holder.setText(R.id.keyTitle, key)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索帖子", null)
            }
        }
        if (mPostAdapter == null) {
            mPostAdapter = PostAdapter()
            mPostAdapter?.moutClickListener = this
            mPostAdapter?.setOnPostFansClickListener(this)
            mPostAdapter?.setOnPostLikeClickListener(this)
            mPostAdapter?.setOnShareClickListener(this)
            mPostAdapter?.setSearchAll(true)
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mPostAdapter
            }
        }
        mPostAdapter?.setData(records as ArrayList<PostDetail>?)
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun postfansclick(view: View, friendId: String, type: String, frtype: String) {
        onPostFansClickListener?.postfansclick(view, friendId, type, frtype)
    }

    override fun postlikeclick(view: View, postingId: String, type: String) {
        onPostLikeClickListener?.postlikeclick(view, postingId, type)
    }

    override fun postshareclick(
        view: View,
        url: String,
        des: String,
        title: String,
        postId: String
    ) {
        onShareClickListener?.postshareclick(view, url, des, title, postId)
    }

    override fun outClick(function: String, obj: Any) {
        when (function) {
            "submit" -> {
                if (isClickInit) {
                    moutClickListener.outClick("submit", obj)
                }
            }
            "sharePk" -> {
                if (isClickInit) {
                    moutClickListener.outClick("sharePk", obj)
                }
            }
            "coupon" -> {
                if (isClickInit) {
                    moutClickListener.outClick("coupon", obj)
                }
            }
        }
    }

    fun setOnPostFansClickListener(onPostFansClickListener: OnPostFansClickListener) {
        this.onPostFansClickListener = onPostFansClickListener
    }

    fun setOnPostLikeClickListener(onPostLikeClickListener: OnPostLikeClickListener) {
        this.onPostLikeClickListener = onPostLikeClickListener
    }

    fun setOnShareClickListener(onShareClickListener: OnShareClickListener) {
        this.onShareClickListener = onShareClickListener
    }

    interface OnPostFansClickListener {
        fun postfansclick(view: View, friendId: String, type: String, frtype: String)
    }

    interface OnPostLikeClickListener {
        fun postlikeclick(view: View, postingId: String, type: String)
    }

    interface OnShareClickListener {
        fun postshareclick(view: View, url: String, des: String, title: String, postId: String)
    }

    public fun refreshAdapter() {
        mPostAdapter?.let { it.notifyDataSetChanged() }
    }
}

