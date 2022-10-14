package com.healthy.library.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.healthy.library.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.contract.ServiceGoodsSpecContract
import com.healthy.library.model.GoodsSpecCell
import com.healthy.library.model.GoodsSpecDetail
import com.healthy.library.model.GoodsSpecLimit
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.presenter.ServiceGoodsSkuPresenter
import com.healthy.library.utils.FormatUtils
import com.healthy.library.widget.ImageTextView
import com.healthy.library.widget.IncreaseDecreaseView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import com.zhy.view.flowlayout.TagView
import io.reactivex.rxjava3.disposables.Disposable


/**
 * @author Long
 * @desc: 异业商品多规格弹框
 * @createTime :2021/10/11 9:57
 */
class SecondGoodsSpecDialog : DialogFragment(), ServiceGoodsSpecContract.View {

    private var mAlertDialog: AlertDialog? = null
    private var mGoodsImg: ImageView? = null
    private var mClose: ImageView? = null
    private var mGoodsSpecMoneyLeft: TextView? = null
    private var mGoodsSpecMoney: TextView? = null
    private var mPinPeopleNum: TextView? = null
    private var mGoodsCount: TextView? = null
    private var mGoodsSelect: TextView? = null
    private var mSubmitBtn: TextView? = null
    private var mIncreaseDecrease: IncreaseDecreaseView? = null
    private var mSpecLL: LinearLayout? = null
    private var mGoodsSpecList = mutableListOf<GoodsSpecCell>()
    var mShopModel: ShopDetailModel? = null
    var mGoodsSpecDetail: GoodsSpecDetail? = null
    var exSkuList: MutableList<GoodsSpecDetail>? = null // 类似砍价拼团这种
    var regimentSize: Int = 0//为了显示几人拼的标签
    var mode = 1  //1就是左(加入购物车或单独购买) 2就是右(立即抢购或参与拼团、砍价
    var isNtReal = false //是否新人
    var isSingleSelectAct = false
    var mGoodsSpecDetailNew: GoodsSpecDetail? = null
    private var destmap = mutableMapOf<String, Any>()
    private var mParamsMap = mutableMapOf<String, Any?>()
    var selectSku: String = ""
    private var marketingType: String? = ""
    private var mapMarketingGoodsId: String? = ""
    private var mGoodsId: String = ""
    private var mShopId: String = ""
    private var isSet = false
    private var limitString: String = ""
    private var serviceGoodsSkuPresenter: ServiceGoodsSkuPresenter? = null

    private lateinit var listener: (goodsSpecDetail: GoodsSpecDetail?) -> Unit

    fun getSpecSubmit(listener: (goodsSpecDetail: GoodsSpecDetail?) -> Unit) {
        this.listener = listener
    }


    fun setMarketing(marketingType: String?, mapMarketingGoodsId: String?) { //传入活动参数
        this.marketingType = marketingType
        this.mapMarketingGoodsId = mapMarketingGoodsId
    }

    fun setGoodsId(goodsId: String, shopId: String) { //传入goodsId和shopId
        mGoodsId = goodsId
        mShopId = shopId
    }

    companion object {
        fun newInstance(): SecondGoodsSpecDialog {
            val mBundle = Bundle()
            val mGoodsSpecDialog = SecondGoodsSpecDialog()
            mGoodsSpecDialog.arguments = mBundle
            return mGoodsSpecDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mAlertDialog == null && requireContext() != null) {
            val mView: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_second_goods_spec_layout, null)

            serviceGoodsSkuPresenter = ServiceGoodsSkuPresenter(requireActivity(), this)
            initView(mView)
            initListener()
            buildData()

            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(mView)
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
                    decorView.setBackgroundResource(R.drawable.shape_dialog)
                    val params = window.attributes
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT

                    val outMetrics = resources.displayMetrics
                    /* val outMetrics = DisplayMetrics()
                     (requireActivity()).windowManager.defaultDisplay.getMetrics(outMetrics)*/
                    params.height = (outMetrics.heightPixels / 1.5).toInt()
                    params.gravity = Gravity.BOTTOM
                    window.attributes = params
                }
            }
        }
        return mAlertDialog!!
    }

    /**
     * 初始化dialog控件
     * @param view
     */
    private fun initView(view: View) {
        mGoodsImg = view.findViewById(R.id.goodsImg)
        mClose = view.findViewById(R.id.close)

        mGoodsSpecMoneyLeft = view.findViewById(R.id.goodsSpecMoneyLeft)
        mGoodsSpecMoney = view.findViewById(R.id.goodsSpecMoney)
        mPinPeopleNum = view.findViewById(R.id.pinPeopleNum)
        mGoodsCount = view.findViewById(R.id.goodsCount)
        mGoodsSelect = view.findViewById(R.id.goodsSelect)
        mSubmitBtn = view.findViewById(R.id.submitBtn)
        mIncreaseDecrease = view.findViewById(R.id.increase_decrease)

        mSpecLL = view.findViewById(R.id.specLL)
    }

    override fun onResume() {
        super.onResume()
        mAlertDialog?.let {
            buildData()
        }
    }

    private fun buildData() {
        destmap.clear()
        buildGoods(true)
        initSpec(mGoodsSpecList)
    }

    private fun initListener() {
        mClose?.setOnClickListener { dismiss() }
        mSubmitBtn?.setOnClickListener {
            if (!TextUtils.isEmpty(limitString)) {
                Toast.makeText(activity, limitString, Toast.LENGTH_SHORT).show()
                if (mGoodsSpecDetailNew != null) {
                    mGoodsSpecDetailNew?.isErrorCount = true //商品有问题 加入购物车的数量需要变动
                }
            }
            if (mGoodsSpecDetailNew == null) {
                Toast.makeText(activity, "请选择商品规格", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!TextUtils.isEmpty(limitString) && mode == 2) {
                return@setOnClickListener
            }
            if (::listener.isInitialized && mGoodsSpecDetailNew != null && mGoodsSpecDetailNew?.availableInventory!! > 0) {
                dismiss()
                mGoodsSpecDetailNew?.let {
                    it.setFilePath(mGoodsSpecDetail?.filePath)
                        .setGoodsTitle(it.goodsTitle.replace(it.goodsSpecStr, ""))
                        .setCount(mIncreaseDecrease?.num.toString())
                }
                listener.invoke(mGoodsSpecDetailNew)
            }
        }
    }

    /**
     * 设置商品信息
     */
    fun buildGoods(isfirst: Boolean) {
        mGoodsCount?.text = ""
        mGoodsCount?.visibility = View.INVISIBLE
        mSubmitBtn?.alpha = 0.7f
        if (isfirst) {
            mSubmitBtn?.text = "确定"
        } else {
            mSubmitBtn?.alpha = 1f
            mSubmitBtn?.text = "确定"
        }
        //重置下选择器
        mIncreaseDecrease?.setMaxCount(1)
        mIncreaseDecrease?.setMinCount(1)
        if (checkMapSize(destmap) == mGoodsSpecList.size) { //判断规格是不是选正确了
            mGoodsSpecDetailNew?.let {
                it.setParent(mGoodsSpecDetail)
                it.setShopDetailModelSelect(mShopModel)
                mGoodsCount?.visibility = View.VISIBLE
                mGoodsCount?.text = "库存${it.availableInventory}件"
                if (!isSet) {
                    mIncreaseDecrease?.reset()
                    println("设置最大最小起购")
                    mIncreaseDecrease?.setMaxCount(it.markLimitMax)
                    mIncreaseDecrease?.setMinCount(it.markLimitMin)
                }
                if (mIncreaseDecrease?.num!! > it.markLimitMax) {
                    mIncreaseDecrease?.setNoCount(it.markLimitMax)
                }
                if (mIncreaseDecrease?.num!! < mGoodsSpecDetailNew?.markLimitMin!!) {
                    mIncreaseDecrease?.setNoCount(it.markLimitMin)
                }
                limitString = ""
                if ("3" == it.marketingType) {
                    if (it.markLimitMinOrg > 0) {
                        mIncreaseDecrease?.setLimitMinString("起购${it.markLimitMinOrg}件")
                    }
                    mIncreaseDecrease?.setLimitMaxString("秒杀库存不足")
                    if (it.markLimitMin > it.realCanBuy) { //最小起购不满足真实可买的进行情况分析
                        limitString = when {
                            it.markLimitMin > it.availableInventoryMark -> {
                                "秒杀库存不足"
                            }
                            it.markLimitMax - it.nowOrderBuyCount <= 0 -> {
                                "您已购${it.nowOrderBuyCount}件该秒杀商品限购${it.markLimitMax}件"
                            }
                            else -> {
                                "起购${it.markLimitMin}件，您已购${it.nowOrderBuyCount}件，该秒杀商品限购${it.markLimitMax}件"
                            }
                        }
                        mIncreaseDecrease?.setLimitMaxString(limitString)
                    } else {
                        if (it.markLimitMaxOrg > 0) { //限购大于0
                            mIncreaseDecrease?.setLimitMaxString("该秒杀商品限购${it.realCanBuy}件")
                            if (it.markLimitMin > it.availableInventoryMark) {
                                mIncreaseDecrease?.setLimitMaxString("秒杀库存不足")
                            }
                        }
                        if (it.markLimitMin > 0) {
                            mIncreaseDecrease?.setLimitMinString("起购${it.markLimitMin}件")
                        }
                    }
                }
            }
        }
        if (mGoodsSpecDetail != null) {
            if (!TextUtils.isEmpty(mGoodsSpecDetail?.filePath)) {
                try {
                    GlideCopy.with(context)
                        .load(mGoodsSpecDetail?.filePath).placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)
                        
                        .into(mGoodsImg!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        buildGoodsMoney()
    }

    /**
     * 设置商品价格
     */
    fun buildGoodsMoney() {
        //重置标签
        mGoodsSpecMoneyLeft?.visibility = View.GONE
        mGoodsSpecMoney?.visibility = View.VISIBLE
        mPinPeopleNum?.visibility = View.GONE

        if (mGoodsSpecDetailNew != null) { //说明有选的规格
            mGoodsSpecMoney?.text =
                "¥${FormatUtils.moneyKeep2Decimals(mGoodsSpecDetailNew?.getMarketingPrice()!!)}" //展示商品的价格
            when (mGoodsSpecDetailNew?.marketingType) {
                "4" -> {
                    //在getPlatformPrice的时候去检测价格
                    if (isNtReal) {
                        mPinPeopleNum?.visibility = View.VISIBLE
                        mPinPeopleNum?.text = "新客专享"
                    }
                }
                "3" -> { //在getPlatformPrice的时候去检测价格
                    mPinPeopleNum?.visibility = View.VISIBLE
                    mPinPeopleNum?.text = "秒杀"
                }
                "2" -> { //在getPlatformPrice的时候去检测价格
                    mPinPeopleNum?.visibility = View.VISIBLE
                    mPinPeopleNum?.text = "${regimentSize}人拼"
                }
                "1" -> { //在getPlatformPrice的时候去检测价格
                    mGoodsSpecMoneyLeft?.visibility = View.VISIBLE
                }
                "8" -> { //在getPlatformPrice的时候去检测价格
                }
                else -> {
                    mGoodsSpecMoney?.text =
                        "¥${FormatUtils.moneyKeep2Decimals(mGoodsSpecDetailNew?.getPlatformPrice()!!)}" //展示商
                }
            }
        } else { //没有选择规格则展示默认的那个价格
            if (mGoodsSpecDetail != null) {
                mGoodsSpecMoney?.text =
                    "¥${FormatUtils.moneyKeep2Decimals(mGoodsSpecDetail?.getPlatformPrice()!!)}"
            }
        }
    }

    /**
     * 填充规格view
     */
    fun initSpec(goodsSpecList: MutableList<GoodsSpecCell>) {
        this.mGoodsSpecList = goodsSpecList
        mGoodsSpecDetailNew = null
        mSpecLL?.let {
            try {
                mGoodsCount?.text = ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.removeAllViews()
            for (i in goodsSpecList.indices) {
                it.addView(buildTagParent(it.context, destmap, goodsSpecList[i]))
            }
        }
    }

    /**
     * 请求规格列表
     */
    private fun getGoodsSku(destmap: MutableMap<String, Any>) {
        mParamsMap.clear()
        mParamsMap["mapMarketingGoodsId"] = mapMarketingGoodsId
        mParamsMap["marketingType"] = if ("4" == marketingType) null else marketingType
        mParamsMap["goodsId"] = mGoodsId
        mParamsMap["shopId"] = mShopId
        mParamsMap["specValue"] = destmap
        serviceGoodsSkuPresenter?.getGoodsSkuResult(mParamsMap)
    }

    /**
     * 设置规格View及数据
     */
    private fun buildTagParent(
        mActivity: Context?,
        destmap: MutableMap<String, Any>,
        goodsSpecCell: GoodsSpecCell
    ): View? {
        val tagparent = View.inflate(mActivity, R.layout.service_item_spec, null)
        val textView = tagparent.findViewById<TextView>(R.id.title)
        textView.text = goodsSpecCell.getSpecName()
        val tagFlowLayout = tagparent.findViewById<TagFlowLayout>(R.id.id_flowlayout)
        val tagAdapter: TagAdapter<*> = object : TagAdapter<String>(goodsSpecCell.specValue) {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tag = LayoutInflater.from(mActivity)
                    .inflate(R.layout.service_item_spec_tag, parent, false) as ImageTextView
                tag.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.selector_second_spec_color
                    )
                )
                tag.background = resources.getDrawable(R.drawable.selector_second_spec_tag)
                tag.text = s
                if (!TextUtils.isEmpty(selectSku)) {
                    if (selectSku.contains(s)) {
                        destmap[goodsSpecCell.id] = goodsSpecCell.specValue[position]
                        if (checkMapSize(destmap) == mGoodsSpecList.size) { //如果不是新客并且是新客商品  则要请求普通规格信息
                            getGoodsSku(destmap)
                            buildGoodsSkuSelect()
                        }
                    }
                }
                return tag
            }
        }
        tagFlowLayout.setOnTagClickListener { view, position, parent -> //                if (!view.isPressed()) {
//                    return false;
//                }
            val tagView = view as TagView
            if (tagView.isChecked) {
                destmap[goodsSpecCell.id] = goodsSpecCell.specValue[position]
                if (checkMapSize(destmap) == mGoodsSpecList.size) { //全规格选择
                    getGoodsSku(destmap)
                }
            } else {
                destmap.remove(goodsSpecCell.id) //取消选择
                mGoodsSpecDetailNew = null
                buildGoods(true)
            }
            buildGoodsSkuSelect()
            return@setOnTagClickListener false
        }
        tagFlowLayout.adapter = tagAdapter
        for (i in goodsSpecCell.specValue.indices) {
            if (selectSku.isNullOrEmpty().not() && selectSku.contains(goodsSpecCell.specValue[i])) {
                tagAdapter.setSelectedList(i)
            }
        }
        tagFlowLayout.setOnSelectListener { selectPosSet ->
            if (selectPosSet.size > 0) {
            }
        }
        return tagparent
    }


    /**
     * 多规格选中处理
     */
    fun buildGoodsSkuSelect() {
        if (checkMapSize(destmap) > 0) {
            mGoodsSelect?.visibility = View.VISIBLE
            mGoodsSelect?.text = "已选${getHasSku(destmap)}"
        } else {
            mGoodsSelect?.visibility = View.INVISIBLE
        }
        mGoodsCount?.visibility = View.VISIBLE
    }

    fun checkMapSize(destmap: Map<String, Any>): Int {
        var result = 0
        for (value in destmap) {
            if (!TextUtils.isEmpty(value.toString())) {
                result++
            }
        }
        return result
    }

    fun getHasSku(destmap: Map<String, Any>): String {
        var result = ""
        for (value in destmap.entries) {
            if (!TextUtils.isEmpty(value.value.toString())) {
                result += "\"${value.value}\""
            }
        }
        return result
    }

    /**
     * 判断type是否匹配
     */
    private fun checkRealSpec(
        goodsSpecDetailNew: GoodsSpecDetail?,
        exSkuList: MutableList<GoodsSpecDetail>?
    ): GoodsSpecDetail? {
        goodsSpecDetailNew?.let {
            exSkuList?.forEach { model ->
                if (it.getGoodsChildId() == model.getGoodsChildId()) {
                    return model;
                }
            }
        }
        return null;
    }

    /**
     * 获取规格列表
     */
    override fun successGetGoodsSkuResult(result: MutableList<GoodsSpecDetail>) {
        if(!ListUtil.isEmpty(result)){
            mGoodsSpecDetailNew = result[0]
            if (exSkuList != null) {
                if (exSkuList!!.size > 0 && isSingleSelectAct) { //暂时先按照isSingleSelectAct 判断 说明存在选择的池子 则需要替换数据
                    val checkRealSpec = checkRealSpec(mGoodsSpecDetailNew, exSkuList)
                    if (checkRealSpec != null && checkRealSpec.availableInventoryMark <= 0
                    ) { //活动库存不足了
                        if ("8" != marketingType) {
                        }
                    } else { //说明传过来的type和查询到的type不匹配了
                        if (checkRealSpec == null) {
                            Toast.makeText(activity, "该规格未参加活动，可直接购买！", Toast.LENGTH_SHORT).show()
                        } else {
                            mGoodsSpecDetailNew = checkRealSpec(mGoodsSpecDetailNew, exSkuList)
                        }
                    }
                }
            }
            if ("3" == mGoodsSpecDetailNew?.marketingType) {
                mParamsMap.clear()
                mParamsMap["includeNoPay"] = "1"
                mParamsMap["goodsSource"] = "1"
                mParamsMap["goodsId"] = mGoodsSpecDetailNew?.goodsId.toString()
                mParamsMap["goodsSpecId"] = mGoodsSpecDetailNew?.getGoodsSpec()
                mParamsMap["marketingGoodsId"] = mGoodsSpecDetailNew?.mapMarketingGoodsId
                mParamsMap["marketingType"] = mGoodsSpecDetailNew?.marketingType
                mParamsMap["marketingGoodsSpecId"] =
                    mGoodsSpecDetailNew?.getgoodsMarketingGoodsSpec()
                mParamsMap["totalQuantity"] = "0"
                mParamsMap["marketingId"] = mGoodsSpecDetailNew?.marketingId
                mParamsMap["goodsType"] = mGoodsSpecDetailNew?.goodsType
                serviceGoodsSkuPresenter?.getGoodsLimitResult(mParamsMap)
            } else {
                if (marketingType.isNullOrEmpty().not() && result[0].marketingType.isNullOrEmpty()
                        .not() && exSkuList.isNullOrEmpty()
                ) {//
                    if ("8" != marketingType) {
                        if ("4" == marketingType && result[0].marketingType == null && !isNtReal) {//是新客商品 然后如果是普通规格 如果本人又不是新客那就不提示吧了

                        } else {
                            Toast.makeText(activity, "该规格未参加活动，可直接购买！", Toast.LENGTH_SHORT)
                                .show();
                        }
                    }
                }
                buildGoods(false);
            }
        }
    }

    override fun successGetGoodsSkuFinalResult(result: GoodsSpecLimit?) {
        if (result != null) {
            mGoodsSpecDetailNew?.nowOrderBuyCount = result.totalQuantity
        } else {
            mGoodsSpecDetailNew?.nowOrderBuyCount = 0
        }
        buildGoods(false)
    }

    override fun showLoading() {

    }

    override fun showToast(msg: CharSequence?) {
    }

    override fun showNetErr() {
    }

    override fun onRequestStart(disposable: Disposable?) {
    }

    override fun showContent() {
    }

    override fun showEmpty() {
    }

    override fun onRequestFinish() {
    }

    override fun getData() {

    }

    override fun showDataErr() {

    }
}