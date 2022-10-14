package com.health.mine.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.mine.R
import com.health.mine.adapter.VipCardBalanceLogsAdapter
import com.health.mine.adapter.VipCardBalanceLogsHeaderAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.BalanceModel
import com.healthy.library.routes.MineRoutes
import kotlinx.android.synthetic.main.activity_vip_card_balance_logs.*
import java.util.ArrayList

@Route(path = MineRoutes.MINE_VIPCARDBALANCELOGSACTIVITY)
class VipCardBalanceLogsActivity : BaseActivity(), IsFitsSystemWindows {

    @Autowired
    @JvmField
    var list: MutableList<BalanceModel.VipClassInfoList>? = null

    @Autowired
    @JvmField
    var classCardYe: String? = null

    private var vipCardBalanceLogsAdapter: VipCardBalanceLogsAdapter? = null
    private var vipCardBalanceLogsHeaderAdapter: VipCardBalanceLogsHeaderAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun findViews() {
        super.findViews()
        img_back.setOnClickListener { finish() }
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        if (ListUtil.isEmpty(list)) {
            showEmpty()
        } else {
            showContent()
            virtualLayoutManager = VirtualLayoutManager(mContext)
            val delegateAdapter = DelegateAdapter(virtualLayoutManager)
            recycler?.apply {
                layoutManager = virtualLayoutManager
                adapter = delegateAdapter
            }
            vipCardBalanceLogsHeaderAdapter = VipCardBalanceLogsHeaderAdapter()
            delegateAdapter.addAdapter(vipCardBalanceLogsHeaderAdapter)

            vipCardBalanceLogsAdapter = VipCardBalanceLogsAdapter()
            delegateAdapter.addAdapter(vipCardBalanceLogsAdapter)

            vipCardBalanceLogsHeaderAdapter?.model = classCardYe
            vipCardBalanceLogsAdapter?.setData(list as ArrayList<BalanceModel.VipClassInfoList>)
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_card_balance_logs
    }
}