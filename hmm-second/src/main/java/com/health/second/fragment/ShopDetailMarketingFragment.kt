package com.health.second.fragment

import android.os.Bundle
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.second.R
import com.health.second.adapter.ShopDetailMarketingAdapter
import com.health.second.contract.ShopDetailContract
import com.health.second.model.PeopleListModel
import com.health.second.presenter.ShopDetailPresenter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.constant.SpKey
import com.healthy.library.model.*
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.fragment_shop_detail_info.*
import java.util.*


class ShopDetailMarketingFragment : BaseFragment(), BaseAdapter.OnOutClickListener,
    ShopDetailContract.View {

    private var shopId: String? = null
    private var merchantType: String? = null
    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var delegateAdapter: DelegateAdapter
    private lateinit var shopDetailMarketingAdapter: ShopDetailMarketingAdapter
    private var shopDetailPresenter: ShopDetailPresenter? = null
    private val mMap = mutableMapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopId = it.getString("shopId")
            merchantType = it.getString("merchantType")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_detail_marketing
    }

    override fun findViews() {
        shopDetailPresenter = ShopDetailPresenter(mContext, this)

        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recycler.layoutManager = virtualLayoutManager
        recycler.adapter = delegateAdapter

        shopDetailMarketingAdapter = ShopDetailMarketingAdapter()
        shopDetailMarketingAdapter.setOutClickListener(this)
        delegateAdapter.addAdapter(shopDetailMarketingAdapter)
        shopDetailMarketingAdapter.setMerchantType(merchantType, shopId)
        getData()
    }

    override fun getData() {
        super.getData()
        mMap.clear()
        mMap["shopId"] = shopId
        mMap["pageNum"] = 1
        mMap["pageSize"] = "10"
        mMap["partnerId"] = SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)
        shopDetailPresenter?.getMarketingList(mMap)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopDetailMarketingFragment().apply {
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
        TODO("Not yet implemented")
    }

    override fun onGetMarketingSuccess(list: MutableList<ShopDetailMarketing>?) {
        showContent()
        if (list != null && list.size > 0) {
            shopDetailMarketingAdapter.setData(list as ArrayList<ShopDetailMarketing>?)
        } else {
            showEmpty()
        }
    }

    override fun onSuccessManDetail(result: TechnicianResult?) {
        TODO("Not yet implemented")
    }

    override fun outClick(function: String?, obj: Any?) {
        TODO("Not yet implemented")
    }

    public fun refresh() {
        getData()
    }
}