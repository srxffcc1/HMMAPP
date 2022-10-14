package com.health.second.fragment

import android.os.Bundle
import android.widget.Switch
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.second.R
import com.health.second.adapter.*
import com.health.second.contract.ShopDetailContract
import com.health.second.model.PeopleListModel
import com.health.second.presenter.ShopDetailPresenter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.model.*
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import kotlinx.android.synthetic.main.fragment_shop_detail_goods.*
import java.util.ArrayList

class ShopDetailGoodsFragment : BaseFragment(), BaseAdapter.OnOutClickListener,
    ShopDetailContract.View, OnLoadMoreListener {
    private var shopId: String? = null
    private var merchantType: String? = null

    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var delegateAdapter: DelegateAdapter
    private lateinit var shopDetailGoodsAdapter: ShopDetailGoodsAdapter
    private var shopDetailPresenter: ShopDetailPresenter? = null
    private val mMap = mutableMapOf<String, Any?>()
    private var pageNum = 1
    private var dataType = 1 //1表示按销量升序，2表示按销量降序 3表示按价格升序，4表示按价格降序


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopId = it.getString("shopId")
            merchantType = it.getString("merchantType")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_detail_goods
    }

    override fun findViews() {
        layout_refresh.setOnLoadMoreListener(this)
        layout_refresh.setEnableRefresh(false)
        shopDetailPresenter = ShopDetailPresenter(mContext, this)

        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recycler.layoutManager = virtualLayoutManager
        recycler.adapter = delegateAdapter

        shopDetailGoodsAdapter = ShopDetailGoodsAdapter()
        shopDetailGoodsAdapter.setOutClickListener(this)
        delegateAdapter.addAdapter(shopDetailGoodsAdapter)
        shopDetailGoodsAdapter.setMerchantType(merchantType)

        getData()
        changeType(1)
        txt_sales_volume.setOnClickListener { changeType(1) }
        distanceTxt.setOnClickListener { changeType(2) }
    }

    override fun getData() {
        super.getData()
        when (dataType) {
            1 -> {
                mMap["appSalesSort"] = "asc"
                mMap["platformPriceSort"] = ""
            }
            2 -> {
                mMap["appSalesSort"] = "desc"
                mMap["platformPriceSort"] = ""
            }
            3 -> {
                mMap["appSalesSort"] = ""
                mMap["platformPriceSort"] = "asc"
            }
            4 -> {
                mMap["appSalesSort"] = ""
                mMap["platformPriceSort"] = "desc"
            }
        }
        mMap["pageNum"] = pageNum
        mMap["pageSize"] = "10"
        mMap["shopId"] = shopId
        mMap["publish"] = "1"
        shopDetailPresenter?.getGoodsList(mMap)
    }

    fun changeType(type: Int) {
        pageNum = 1
        when (type) {
            1 -> {
                if (dataType == 1) {
                    dataType = 2
                    sales_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon1))
                    sales_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_up_icon1))
                    distance_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon1))
                    distance_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon))
                } else {
                    dataType = 1
                    sales_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_up_icon))
                    sales_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon))
                    distance_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon1))
                    distance_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon))
                }
            }
            2 -> {
                if (dataType == 3) {
                    dataType = 4
                    sales_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon1))
                    sales_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon))
                    distance_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon1))
                    distance_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_up_icon1))
                } else {
                    dataType = 3
                    sales_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon1))
                    sales_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon))
                    distance_up_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_up_icon))
                    distance_down_img.setImageDrawable(resources.getDrawable(R.drawable.shop_detail_goods_down_icon))
                }
            }
        }
        getData()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopDetailGoodsFragment().apply {
                arguments = Bundle().apply {
                    putString("shopId", param1)
                    putString("merchantType", param2)
                }
            }
    }

    override fun onGetStoreDetailSuccess(storeDetailModel: ShopDetailModel?) {
        TODO("Not yet implemented")
    }

    override fun onGetPeopleListSuccess(result: MutableList<PeopleListModel>?) {
        TODO("Not yet implemented")
    }

    override fun onGetGoodsListSuccess(
        list: MutableList<SortGoodsListModel>?,
        pageInfo: OrderListPageInfo?
    ) {
        showContent()
        pageNum = pageInfo!!.pageNum
        if (pageNum == 1) {
            if (list == null || list!!.size == 0) {
                showEmpty()
            } else {
                shopDetailGoodsAdapter.setData(list as ArrayList<SortGoodsListModel>?)
            }
        } else {
            layout_refresh.finishLoadMore()
            shopDetailGoodsAdapter.addDatas(list as ArrayList<SortGoodsListModel>?)
        }
        if (pageInfo.nextPage == 0) {
            layout_refresh.finishLoadMoreWithNoMoreData()
        } else {
            layout_refresh.setNoMoreData(false)
            layout_refresh.setEnableLoadMore(true)
        }
    }

    override fun onGetMarketingSuccess(list: MutableList<ShopDetailMarketing>?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessManDetail(result: TechnicianResult?) {
        TODO("Not yet implemented")
    }

    override fun outClick(function: String?, obj: Any?) {
        TODO("Not yet implemented")
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getData()
    }

    public fun refresh() {
        pageNum = 1
        getData()
    }
}