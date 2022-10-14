package com.health.mine.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.mine.R
import com.health.mine.adapter.VipProfitBottomAdapter
import com.health.mine.adapter.VipProfitHeaderAdapter
import com.health.mine.contract.VipProfitContract
import com.health.mine.contract.VipTakeOutContract
import com.health.mine.presenter.VipProfitPresenter
import com.health.mine.presenter.VipTakeOutPresenter
import com.healthy.library.LibApplication
import com.healthy.library.adapter.EmptyAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.BankCardModel
import com.healthy.library.model.TongLianMemberData
import com.healthy.library.model.VipProfitModel
import com.healthy.library.routes.MineRoutes
import com.healthy.library.utils.LogUtils
import com.healthy.library.utils.ObjUtil
import com.healthy.library.utils.SpUtils
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_vip_profit.*
import kotlinx.android.synthetic.main.activity_vip_profit.img_back
import kotlinx.android.synthetic.main.activity_vip_take_out_profit.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Description: (我的收益页面)
 * @author:LiuWei
 * @date 2022/8/25
 * @version V1.0
 */
@Route(path = MineRoutes.MINE_VIPPROFIT)
class VipProfitActivity : BaseActivity(), IsFitsSystemWindows, VipProfitContract.View,
    OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener, VipTakeOutContract.View {

    private var vipProfitBottomAdapter: VipProfitBottomAdapter? = null
    private var vipProfitHeaderAdapter: VipProfitHeaderAdapter? = null
    private var emptyAdapter: EmptyAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null
    private var vipProfitPresenter: VipProfitPresenter? = null
    private var mCurrentPage = 1
    private var totalUsableAmount = "0"
    private val mMap: MutableMap<String, Any> = mutableMapOf()
    private var vipTakeOutPresenter: VipTakeOutPresenter? = null
    private var mBankCardList: MutableList<BankCardModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        vipProfitPresenter = VipProfitPresenter(this, this)
        vipTakeOutPresenter = VipTakeOutPresenter(this, this)
        initView()
    }

    override fun onResume() {
        super.onResume()
        vipTakeOutPresenter?.getBankCardList()
        getData()
        getUserStatus()
    }

    override fun getData() {
        super.getData()
        mMap["pageSize"] = 10
        mMap["pageNum"] = mCurrentPage
        vipProfitPresenter?.getProfitList(mMap)
        vipProfitPresenter?.getVipProfitData()
    }

    private fun initView() {
        img_back.setOnClickListener { finish() }
        refresh_layout.setOnRefreshLoadMoreListener(this)
        showContent()
        cardCenter.setOnClickListener {
            val tongLianMemberData = ObjUtil.getObj(
                SpUtils.getValue(LibApplication.getAppContext(), SpKey.TONGLIANBIZUSER),
                TongLianMemberData::class.java
            )
            if (tongLianMemberData == null || !tongLianMemberData.memberInfo.isIdentityChecked) {//没实名
                isAgree2()
            } else {
                ARouter.getInstance()
                    .build(MineRoutes.MINE_BANKCARDMANAGE)
                    .navigation()
            }
        }
        virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recycler?.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
        }
        vipProfitHeaderAdapter = VipProfitHeaderAdapter()
        delegateAdapter.addAdapter(vipProfitHeaderAdapter)
        vipProfitHeaderAdapter?.setOutClickListener(this)

        vipProfitBottomAdapter = VipProfitBottomAdapter()
        delegateAdapter.addAdapter(vipProfitBottomAdapter)

        emptyAdapter = EmptyAdapter()
        delegateAdapter.addAdapter(emptyAdapter)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_profit
    }

    override fun getVipProfitDataSuccess(resultData: VipProfitModel?) {
        vipProfitHeaderAdapter?.model = ""
        vipProfitHeaderAdapter?.setAdapterData(resultData!!)
        this.totalUsableAmount = resultData!!.totalUsableAmount
    }

    override fun getProfitListSuccess(resultData: MutableList<VipProfitModel>?, isMore: Boolean) {
        if (mCurrentPage == 1) {
            refresh_layout?.finishRefresh()
            if (ListUtil.isEmpty(resultData)) {
                emptyAdapter?.model = ""
            } else {
                emptyAdapter?.clear()
                vipProfitBottomAdapter?.setData(resultData as ArrayList<VipProfitModel>?)
            }
        } else {
            refresh_layout?.finishLoadMore()
            if (!ListUtil.isEmpty(resultData)) {
                vipProfitBottomAdapter?.addDatas(resultData as ArrayList<VipProfitModel>?)
            } else {//没有更多了
                refresh_layout?.setNoMoreData(true)
                refresh_layout?.finishLoadMoreWithNoMoreData()
            }
        }
    }

    override fun identitySuccess(resultData: String) {
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mCurrentPage = 1
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mCurrentPage++
        getData()
    }

    override fun outClick(function: String?, obj: Any?) {
        if (function.equals("tixian")) {
            if (totalUsableAmount.toDouble() <= 0) {
                showToast("暂无可提现金额！")
                return
            }
            if (ListUtil.isEmpty(mBankCardList)) {//没绑卡
                isAgree3()
                return
            }
            val tongLianMemberData = ObjUtil.getObj(
                SpUtils.getValue(LibApplication.getAppContext(), SpKey.TONGLIANBIZUSER),
                TongLianMemberData::class.java
            )
            if (tongLianMemberData == null || !tongLianMemberData.memberInfo.isIdentityChecked) {//没实名
                isAgree()
            } else {
                val calendar: Calendar = Calendar.getInstance()
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 20) {
                    showToast("由于银行监管机构加强金融风险每日20:00之后无法提现")
                    return
                }
                ARouter.getInstance()
                    .build(MineRoutes.MINE_VIPTAKEOUTPROFIT)
                    .withString("totalUsableAmount", totalUsableAmount)
                    .navigation()
            }
        }
    }

    private fun isAgree() {
        StyledDialog.init(this)
        StyledDialog.buildIosAlert(
            "提示",
            "响应国家要求,涉及资金的来往的平台账户都需要完成实名认证!",
            object : MyDialogListener() {
                override fun onFirst() {}
                override fun onThird() {
                    super.onThird()
                }

                override fun onSecond() {
                    ARouter.getInstance().build(MineRoutes.MINE_VIPIDENTITYCHECK).navigation()
                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
            .setBtnText("取消", "去实名").show()
    }

    private fun isAgree2() {
        StyledDialog.init(this)
        StyledDialog.buildIosAlert(
            "提示",
            "响应国家要求,涉及资金的来往的平台账户都需要完成实名认证!",
            object : MyDialogListener() {
                override fun onFirst() {}
                override fun onThird() {
                    super.onThird()
                }

                override fun onSecond() {
                    ARouter.getInstance().build(MineRoutes.MINE_VIPIDENTITYCHECK).navigation()
                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
            .setBtnText("取消", "去实名").show()
    }

    private fun isAgree3() {
        StyledDialog.init(this)
        StyledDialog.buildIosAlert(
            "提示",
            "您暂未绑定银行卡！",
            object : MyDialogListener() {
                override fun onFirst() {}
                override fun onThird() {
                    super.onThird()
                }

                override fun onSecond() {
                    ARouter.getInstance().build(MineRoutes.MINE_VIPADDBANK).navigation()
                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
            .setBtnText("取消", "去绑卡").show()
    }

    override fun getBankCardListSuccess(resultData: MutableList<BankCardModel>?) {
        if (!ListUtil.isEmpty(resultData)) {
            this.mBankCardList.clear()
            this.mBankCardList.addAll(resultData!!)
        }
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

}