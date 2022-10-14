package com.health.mine.activity

import android.content.DialogInterface
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.mine.R
import com.health.mine.adapter.*
import com.health.mine.contract.EnlistSignOffContract
import com.health.mine.model.EnlistSignOffModel
import com.health.mine.presenter.EnListSignOffPresenter
import com.healthy.library.adapter.DecorationAdapter
import com.healthy.library.adapter.ItemLineAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.dialog.CustomTipDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.routes.MineRoutes
import kotlinx.android.synthetic.main.mine_activity_enlist_signoff.*

/**
 * @description 工作人员报名核销页面
 * @author long
 * @date 2021/7/23
 */
@Route(path = MineRoutes.MINE_ENLIST_SIGNOFF)
class EnlistSignOffActivity : BaseActivity(), IsFitsSystemWindows,
    EnlistSignOffContract.View {

    @Autowired
    @JvmField
    var verificationCode = ""

    @Autowired
    @JvmField
    var enlistActivityId = ""

    private val mEnlistSignOffPresenter: EnListSignOffPresenter = EnListSignOffPresenter(this, this)
    private lateinit var enlistDetailsHeaderAdapter: EnlistDetailsHeaderAdapter
    private lateinit var enlistSignOffAdapter: EnlistSignOffAdapter
    private lateinit var itemLineAdapter: ItemLineAdapter

    private val mList = mutableListOf<EnlistSignOffModel>()
    private val mMap = mutableMapOf<String, Any>()

    override fun getLayoutId(): Int {
        return R.layout.mine_activity_enlist_signoff
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        setStatusLayout(layout_status)
        top_bar.setSubmitColor(mContext.resources.getColor(R.color.color_444444))

        //详情页面禁止上拉加载
        refresh_layout.apply {
            finishLoadMoreWithNoMoreData()
        }

        getData()

        buildRecyclerHelper()
        initListener()
    }

    private fun buildRecyclerHelper() {
        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        rv.layoutManager = virtualLayoutManager
        rv.adapter = delegateAdapter

        enlistDetailsHeaderAdapter = EnlistDetailsHeaderAdapter()
        delegateAdapter.addAdapter(enlistDetailsHeaderAdapter)

        itemLineAdapter = ItemLineAdapter()
        delegateAdapter.addAdapter(itemLineAdapter)

        enlistSignOffAdapter = EnlistSignOffAdapter()
        delegateAdapter.addAdapter(enlistSignOffAdapter)

        val decorationAdapter = DecorationAdapter()
        decorationAdapter.setModel("")
        delegateAdapter.addAdapter(decorationAdapter)
    }

    private fun initListener() {

        tv_confirm_action.setOnClickListener {
            //确认核销
            mEnlistSignOffPresenter.confirmSignOff(mMap)
        }

        refresh_layout.setOnRefreshListener {
            getData()
        }

    }

    override fun getData() {
        super.getData()
        mMap["verificationCode"] = verificationCode
        mMap["enlistActivityId"] = enlistActivityId
        mEnlistSignOffPresenter.getCodeInfo(mMap)
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        refresh_layout.finishRefresh()
        refresh_layout.finishLoadMoreWithNoMoreData()
    }

    /**
     * 获取签核信息
     * @param resultModel 返回签核信息数据
     */
    override fun getCodeInfoSuccess(resultModel: EnlistActivityModel) {
        if (resultModel.enlistActivity == null) {
            showDataErr()
            return
        }
        mList.clear()
        for (index in 0..3) {
            val enlistSignOffModel = EnlistSignOffModel()
            enlistSignOffModel.key = when (index) {
                0 -> "  姓名"
                1 -> "  性别"
                2 -> "  联系方式"
                3 -> "  所属阶段"
                else -> "  姓名"
            }
            enlistSignOffModel.value = when (index) {
                0 -> "${resultModel.enlistName}  "
                1 -> "${if (resultModel.enlistSex == 1) "男" else "女"}  "
                2 -> "${resultModel.enlistPhone}  "
                3 -> "${resultModel.enlistStage}  "
                else -> "闪电侠  "
            }
            mList.add(enlistSignOffModel)
        }
        enlistDetailsHeaderAdapter.setModel(resultModel)
        itemLineAdapter.setModel("")
        enlistSignOffAdapter.setData(mList as ArrayList<EnlistSignOffModel>)


    }

    /**
     * 确认签核成功回调
     */
    override fun confirmSignOffSuccess() {
        val mTipDialog = CustomTipDialog.newInstance().setMessage("签核成功")
            .setTipType(CustomTipDialog.TOAST_TYPE_SUCCESS)
            .setOnDismissListener(DialogInterface.OnDismissListener {
                finish()
            })
        mTipDialog.show(supportFragmentManager, "scanSuccessDialog")
    }
}