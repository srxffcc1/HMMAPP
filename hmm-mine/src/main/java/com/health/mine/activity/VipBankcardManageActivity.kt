package com.health.mine.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.mine.R
import com.health.mine.adapter.BankcardManageAdapter
import com.health.mine.adapter.VipCardBalanceLogsHeaderAdapter
import com.health.mine.contract.VipTakeOutContract
import com.health.mine.presenter.VipProfitPresenter
import com.health.mine.presenter.VipTakeOutPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.model.BankCardModel
import com.healthy.library.routes.MineRoutes
import kotlinx.android.synthetic.main.activity_bankcard_manage.*
import java.util.ArrayList

/**
 * @description 银行卡管理
 * @author liu
 * @date 2022/8/4
 */
@Route(path = MineRoutes.MINE_BANKCARDMANAGE)
class VipBankcardManageActivity : BaseActivity(), VipTakeOutContract.View  {

    private var bankcardManageAdapter: BankcardManageAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null

    private var vipTakeOutPresenter: VipTakeOutPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        vipTakeOutPresenter = VipTakeOutPresenter(this, this)
        showContent()
        initView()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun getBankCardListSuccess(resultData: MutableList<BankCardModel>?) {
        resultData?.add(BankCardModel())
        bankcardManageAdapter?.setData(resultData as ArrayList<BankCardModel>?)
    }

    override fun getBankCardBinSuccess(resultData: BankCardModel?) {
        TODO("Not yet implemented")
    }

    override fun getSignContractSuccess(resultData: String) {
        TODO("Not yet implemented")
    }

    override fun withdrawApplySuccess(orderNo: String, bizOrderNo: String) {
        TODO("Not yet implemented")
    }

    override fun bindBankCardSuccess() {
        TODO("Not yet implemented")
    }

    override fun payByBackSMSSuccess(result: String) {
        TODO("Not yet implemented")
    }

    override fun getData() {
        super.getData()
        vipTakeOutPresenter?.getBankCardList()
    }
    private fun initView() {

        virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerView?.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
        }

        bankcardManageAdapter = BankcardManageAdapter()
        delegateAdapter.addAdapter(bankcardManageAdapter)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bankcard_manage
    }
}