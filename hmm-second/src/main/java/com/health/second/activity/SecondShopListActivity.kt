package com.health.second.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.second.R
import com.health.second.adapter.SecondShopListAdapter
import com.health.second.contract.SecondShopContract
import com.health.second.presenter.SecondShopPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.PhoneUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import kotlinx.android.synthetic.main.activity_second_shop_list.*

/**
 * @description 异业门店列表
 * @author long
 * @createTime 2021/10/09
 */
@Route(path = SecondRoutes.SECOND_SHOP_LIST)
class SecondShopListActivity : BaseActivity(), IsFitsSystemWindows, SecondShopContract.View {

    @Autowired
    @JvmField
    var shopId: String = ""

    @Autowired
    @JvmField
    var goodsShopIds: Array<String>? = null

    private var mShopPresenter: SecondShopPresenter? = null
    private var mParamsMap = mutableMapOf<String, Any>()

    private var mHeaderTitle: TextView? = null
    private lateinit var mShopListAdapter: SecondShopListAdapter
    private val mShopList = mutableListOf<ShopDetailModel>()

    override fun getLayoutId(): Int {
        return R.layout.activity_second_shop_list
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        setStatusLayout(layout_status)
        mShopPresenter = SecondShopPresenter(mContext, this)
        mParamsMap["shopId"] = shopId
        mParamsMap["shopTypeList"] = arrayOf("1")
        getData()
    }

    override fun findViews() {
        super.findViews()
        initListener()
        buildRecyclerHelper()
    }


    override fun getData() {
        super.getData()
        mShopPresenter?.getShopList(mParamsMap)
    }

    private fun initListener() {
    }

    private fun buildRecyclerHelper() {
        recyclerview.layoutManager = LinearLayoutManager(mContext)
        mShopListAdapter = SecondShopListAdapter()

        /*** 设置header文本 */
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            TransformUtil.dp2px(mContext, 50f).toInt()
        )
        layoutParams.leftMargin = TransformUtil.dp2px(mContext, 4f).toInt()
        mHeaderTitle = AppCompatTextView(mContext)
        mHeaderTitle?.apply {
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(mContext.resources.getColor(R.color.color_333333))
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            this.layoutParams = layoutParams
        }
        mShopListAdapter.addHeaderView(mHeaderTitle)

        //条目点击事件
        mShopListAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent()
            intent.putExtra("id", mShopListAdapter.data[position].id)
            setResult(RESULT_OK, intent)
            finish()
        }
        //条目子控件点击事件
        mShopListAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_distance -> PhoneUtils.callPhone(
                    mContext,
                    mShopListAdapter.data[position].appointmentPhone
                )
            }
        }
        recyclerview.adapter = mShopListAdapter
    }

    override fun onSucessGetShopDetailOnly(detailModel: ShopDetailModel?) {
        TODO("Not yet implemented")
    }

    override fun onGetShopListSuccess(shopList: MutableList<ShopDetailModel>?) {
        if (shopList.isNullOrEmpty() || goodsShopIds.isNullOrEmpty()) {
            showEmpty()
            return
        }
        mShopList.clear()
        for (model in shopList) {
            if (checkNowServiceShopIsRealy(model.id, goodsShopIds)) {
                mShopList.add(model)
            }
        }
        mShopList.sort()
        mHeaderTitle?.text = "共${mShopList.size}家"
        mShopListAdapter.setNewData(mShopList)
    }

    private fun checkNowServiceShopIsRealy(shopId: String, shopIdList: Array<String>?): Boolean {
        if (shopIdList == null || shopIdList.isEmpty()) {
            return false
        }
        for (i in shopIdList.indices) {
            if (shopId == shopIdList[i]) {
                return true
            }
        }
        return false
    }
}