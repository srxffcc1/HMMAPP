package com.healthy.library.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.healthy.library.R
import com.healthy.library.adapter.GiftSelectShopListAdapter
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.contract.GiftSelectContract
import com.healthy.library.model.Coupon
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.presenter.GiftSelectPresenter
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.StatusLayout
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.dialog_gift_select_shop_list.*

/**
 * @description
 * @author long
 * @date 2021/9/16
 */
@Suppress("INACCESSIBLE_TYPE")
class GiftSelectShopListDialog : BottomSheetDialogFragment(), GiftSelectContract.View {

    private var mAlertDialog: AlertDialog? = null
    private lateinit var mTitle: TextView
    private lateinit var mMoney: TextView
    private lateinit var mTvActivity: TextView
    private lateinit var mTvDesc: TextView
    private lateinit var mTvShopName: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var layoutStatus: StatusLayout
    private var shopId: String = ""

    private val mMap = mutableMapOf<String, Any>()
    private lateinit var mGiftSelectPresenter: GiftSelectPresenter
    private lateinit var mShopListAdapter: GiftSelectShopListAdapter
    private var mShopModelResultListener: ShopModelResultListener? = null
    private var mCoupon: Coupon? = null
    private var mDepartID: String = ""
    private var mPersonID: String = ""
    private var mShopList: MutableList<ShopDetailModel> = mutableListOf()
    // isCheckShop 1、服务卷由于商家没有配置门店 改为true不拦截不然一直提示；2、选中了一家门店状态改为true返回不在提示；反之其他情况提示文案
    private var isCheckShop: Boolean = false

    companion object {
        fun newInstance(): GiftSelectShopListDialog {
            val args = Bundle()
            val fragment = GiftSelectShopListDialog()
            fragment.arguments = args
            return fragment
        }
    }

    fun setCoupon(coupon: Coupon) {
        this.mCoupon = coupon
    }

    fun setShopId(shopId: String) {
        this.shopId = shopId;
    }

    fun setShopModelResultListener(mShopModelResultListener: ShopModelResultListener) {
        this.mShopModelResultListener = mShopModelResultListener
    }

    interface ShopModelResultListener {
        fun getShopModel(shopModel: ShopDetailModel)
        fun showTip(message: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mAlertDialog == null && requireContext() != null) {
            val view: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_gift_select_shop_list, null)

            initView(view)

            mGiftSelectPresenter = GiftSelectPresenter(requireActivity(), this)

            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true)
                .create()

            mAlertDialog?.apply {
                isCancelable = true
                setCanceledOnTouchOutside(true)

                val window = mAlertDialog!!.window
                if (window != null) {
                    window.setWindowAnimations(R.style.BottomDialogAnimation)
                    val decorView = window.decorView
                    decorView.setPadding(0, 0, 0, 0)
                    decorView.setBackgroundColor(Color.TRANSPARENT)
                    //decorView.setBackgroundResource(R.drawable.shape_dialog)
                    val params = window.attributes
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.gravity = Gravity.BOTTOM
                    window.attributes = params
                }

                /*setOnKeyListener { dialog, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (!isCheckShop) {
                            mShopModelResultListener?.let {
                                it.showTip("为了更好的为您提供服务，请选择您最方便前往的门店")
                            }
                            return@setOnKeyListener true
                        }
                        dismiss()
                    }
                    return@setOnKeyListener false
                }*/
            }
            getData()
        }
        return mAlertDialog!!
    }

    private fun initView(view: View) {
        layoutStatus = view.findViewById(R.id.layout_status)
        mTitle = view.findViewById(R.id.tv_title)
        mMoney = view.findViewById(R.id.tv_Money)
        mTvActivity = view.findViewById(R.id.tv_activity)
        mTvDesc = view.findViewById(R.id.tv_desc_body)
        mTvShopName = view.findViewById(R.id.tv_shopName)
        mRecyclerView = view.findViewById(R.id.recycler)
        val layoutParams = mMoney.layoutParams as? ConstraintLayout.LayoutParams
        var checkShopId = 0
        mCoupon?.let {
            mTitle.text = it.GoodsName
            mMoney.text = it.Price
            mTvShopName.text = if (it.shopName.isNullOrEmpty()) "选择服务门店" else "${it.getShopName()}"
            isCheckShop = it.shopName.isNullOrEmpty().not()
            mTvActivity.text = it.gbTypeName
            if (it.Price.length > 4) {
                mMoney.textSize = TransformUtil.sp2px(context, 8f)
                layoutParams?.rightMargin = TransformUtil.dp2px(context, 3f).toInt()
                mMoney.layoutParams = layoutParams
            }
            if (it.CheckShopId.isNullOrEmpty().not()) {
                checkShopId = it.CheckShopId.toInt()
            }
        }

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mShopListAdapter = GiftSelectShopListAdapter()
        mShopListAdapter.setCheckShopId(checkShopId)
        mShopListAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                mShopModelResultListener?.let {
                    val shopModel = mShopListAdapter.data[position]
                    isCheckShop = true
                    mCoupon?.DepartID = shopModel.ytbDepartID
                    mCoupon?.PersonID = shopModel.PersonID
                    it.getShopModel(shopModel)
                    dismiss()
                }
            }
        mRecyclerView.adapter = mShopListAdapter

        view.findViewById<ImageView>(R.id.dialog_close).setOnClickListener {
            //if (isCheckShop) {
                dismiss()
            /*} else {
                mShopModelResultListener?.let {
                    it.showTip("为了更好的为您提供服务，请选择您最方便前往的门店")
                }
            }*/
        }
    }

    override fun getData() {
        //获取门店列表
        if (TextUtils.isEmpty(shopId)) {
            shopId = SpUtils.getValue(context, SpKey.CHOSE_SHOP)
        }
        mMap["shopId"] = shopId
        mGiftSelectPresenter.getShopList(mMap)
    }

    /**
     * 门店列表数据回调
     */
    override fun onGetShopListSuccess(detialModel: MutableList<ShopDetailModel>) {
        if (ListUtil.isEmpty(detialModel)) {
            isCheckShop = true
            showEmpty()
            return
        }
        mCoupon?.PersonInfo?.let {
            it.forEachIndexed { index, personInfo ->
                detialModel.forEachIndexed { index, newStoreDetialModel ->
                    if (personInfo.DepartID == newStoreDetialModel.ytbDepartID) {
                        newStoreDetialModel.PersonID = personInfo.PersonID
                        mShopList.add(newStoreDetialModel)
                    }
                }
            }
        }
        //排序
        mShopList.sort()
        mShopListAdapter.setNewData(mShopList)
        showContent()
    }

    override fun showLoading() {
        layoutStatus?.let {
            it.updateStatus(StatusLayout.Status.STATUS_LOADING)
        }
    }

    override fun showToast(msg: CharSequence?) {
    }

    override fun showNetErr() {
        layoutStatus?.let {
            it.updateStatus(StatusLayout.Status.STATUS_NET_ERR)
        }
    }

    override fun onRequestStart(disposable: Disposable?) {
    }

    override fun showContent() {
        layoutStatus?.let {
            it.updateStatus(StatusLayout.Status.STATUS_CONTENT)
        }
    }

    override fun showEmpty() {
        layoutStatus?.let {
            it.updateStatus(StatusLayout.Status.STATUS_EMPTY)
        }
    }

    override fun onRequestFinish() {
    }

    override fun showDataErr() {
        layoutStatus?.let {
            it.updateStatus(StatusLayout.Status.STATUS_DATA_ERR)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {

        }
    }
}