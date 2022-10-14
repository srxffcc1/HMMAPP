package com.health.second.fragment

import android.os.Bundle
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.second.R
import com.health.second.adapter.ShopDetailGoodsAdapter
import com.health.second.adapter.ShopDetailTechnicianAdapter
import com.health.second.adapter.StoreDetailQualificationsAdapter
import com.health.second.contract.ShopDetailContract
import com.health.second.model.PeopleListModel
import com.health.second.presenter.ShopDetailPresenter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.constant.SpKey
import com.healthy.library.model.*
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlinx.android.synthetic.main.fragment_shop_detail_info.*
import kotlinx.android.synthetic.main.fragment_shop_detail_info.layout_refresh

class ShopDetailInfoFragment : BaseFragment(), BaseAdapter.OnOutClickListener,
    ShopDetailContract.View {
    private var shopId: String? = null
    private var param2: String? = null

    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var delegateAdapter: DelegateAdapter
    private lateinit var shopDetailTechnicianAdapter: ShopDetailTechnicianAdapter
    private lateinit var storeDetailQualificationsAdapter: StoreDetailQualificationsAdapter
    private var shopDetailPresenter: ShopDetailPresenter? = null
    private val mMap = mutableMapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopId = it.getString("shopId")
            param2 = it.getString("param2")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_detail_info
    }

    override fun findViews() {
        shopDetailPresenter = ShopDetailPresenter(mContext, this)

        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recycler.layoutManager = virtualLayoutManager
        recycler.adapter = delegateAdapter

        shopDetailTechnicianAdapter = ShopDetailTechnicianAdapter()
        shopDetailTechnicianAdapter.setOutClickListener(this)
        delegateAdapter.addAdapter(shopDetailTechnicianAdapter)

        storeDetailQualificationsAdapter = StoreDetailQualificationsAdapter()
        storeDetailQualificationsAdapter.setOutClickListener(this)
        delegateAdapter.addAdapter(storeDetailQualificationsAdapter)

        getData()
    }

    override fun getData() {
        super.getData()
        shopDetailPresenter?.getStoreDetail(shopId)
        mMap.clear()
        mMap["shopId"] = shopId
        mMap["currentPage"] = 1
        mMap["pageSize"] = "10000"
        shopDetailPresenter?.getPeopleList(mMap)
    }

    override fun onGetStoreDetailSuccess(storeDetailModel: ShopDetailModel?) {
        showContent()
        storeDetailQualificationsAdapter.clear()
        if (storeDetailModel != null) {
            storeDetailQualificationsAdapter.setModel(storeDetailModel)
        } else {
            showEmpty()
        }
    }

    override fun onGetPeopleListSuccess(result: MutableList<PeopleListModel>?) {
        showContent()
        if (result != null && result.size > 0) {
            val data = mutableListOf<Any?>()
            data.add(result)
            shopDetailTechnicianAdapter.setShopId(shopId)
            shopDetailTechnicianAdapter.setData(data as ArrayList<ArrayList<PeopleListModel>>)
        } else {
            shopDetailTechnicianAdapter.clear()
        }
    }

    override fun onGetGoodsListSuccess(
        list: MutableList<SortGoodsListModel>?,
        pageInfo: OrderListPageInfo?
    ) {
        TODO("Not yet implemented")
    }

    override fun onGetMarketingSuccess(list: MutableList<ShopDetailMarketing>?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessManDetail(result: TechnicianResult?) {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopDetailInfoFragment().apply {
                arguments = Bundle().apply {
                    putString("shopId", param1)
                    putString("param2", param2)
                }
            }
    }

    override fun outClick(function: String?, obj: Any?) {
        TODO("Not yet implemented")
    }

    public fun refresh() {
        getData()
    }
}