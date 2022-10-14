package com.healthy.library.dialog

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.healthy.library.LibApplication
import com.healthy.library.R
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.constant.SpKey
import com.healthy.library.constant.UrlKeys
import com.healthy.library.contract.SeckShareDialogContract
import com.healthy.library.model.PkVotingActivityModel
import com.healthy.library.model.PostDetail
import com.healthy.library.presenter.SeckShareDialogPresenter
import com.healthy.library.utils.PermissionUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.uuzuche.lib_zxing.activity.CodeUtils
import io.reactivex.rxjava3.disposables.Disposable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * author : long
 * Time   :2021/11/10
 * desc   :
 */
class PkVotingDialog : DialogFragment(), SeckShareDialogContract.View {

    private var mView: View? = null
    private var mAlertDialog: AlertDialog? = null
    private var mRlContent: RelativeLayout? = null
    private var dialogCardView: View? = null
    private var shareViewLiner: ConstraintLayout? = null
    private var share_weixinLiner: LinearLayout? = null
    private var share_friendLiner: LinearLayout? = null
    private var share_downLiner: LinearLayout? = null
    private var mAppLetsImg: ImageView? = null

    private val RC_PERMISSION = 45
    private val mPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var mPresenter: SeckShareDialogPresenter? = null
    private var visitShopId: String? = ""
    private var referral_code: String? = ""
    private var merchantId: String? = ""
    private var mShopLogo: String? = ""
    private var mActivityObject: Any? = null
    private var mBase64Bitmap: Bitmap? = null
    private var personId: String? = ""

    //小程序地址
    private var mPath: String? = ""
    private var mBase64Data: String? = ""
    private val mMap: MutableMap<String, Any> = mutableMapOf()
    private var mExtraMap: SimpleHashMapBuilder<String, String> = SimpleHashMapBuilder()
    private val mStringBuilder = StringBuilder()

    /*--------------- 公用类型字段 START --------------*/
    private var mActivityId: String = ""
    /*--------------- 公用类型字段 END --------------*/

    private var bitmapHasPic = false
    private var umShareListener: UMShareListener = object : UMShareListener {
        override fun onStart(share_media: SHARE_MEDIA) {}
        override fun onResult(share_media: SHARE_MEDIA) {
            Toast.makeText(LibApplication.getAppContext(), "分享成功！", Toast.LENGTH_LONG).show()
            dialog!!.dismiss()
        }

        override fun onError(share_media: SHARE_MEDIA, throwable: Throwable) {
            Toast.makeText(LibApplication.getAppContext(), "分享失败！", Toast.LENGTH_LONG).show()
        }

        override fun onCancel(share_media: SHARE_MEDIA) {
            Toast.makeText(LibApplication.getAppContext(), "取消分享！", Toast.LENGTH_LONG).show()
        }
    }

    fun setMerchantShopId(merchantId: String?, visitShopId: String?): PkVotingDialog {
        this.merchantId = merchantId
        this.visitShopId = visitShopId
        return this
    }

    fun setExtraMap(map: SimpleHashMapBuilder<String, String>): PkVotingDialog {
        this.mExtraMap = map
        return this
    }

    fun setActivityObj(activityObject: Any): PkVotingDialog {
        this.mActivityObject = activityObject
        return this
    }

    companion object {
        fun newInstance(): PkVotingDialog {
            val args = Bundle()

            val fragment = PkVotingDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(activity, *mPermissions)) {
                Toast.makeText(activity, "需要同意存储权限才能保存图片", Toast.LENGTH_LONG).show()
                PermissionUtils.requestPermissions(
                    activity,
                    RC_PERMISSION,
                    *mPermissions
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //设置背景半透明
        val dm = DisplayMetrics()
        val window = dialog!!.window
        window?.setWindowAnimations(R.style.BottomDialogAnimation)
        requireActivity().windowManager.defaultDisplay.getMetrics(dm)
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        //        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.decorView?.setPadding(0, 0, 0, 0)
        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = params
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DisplayUtility.disabledDisplayDpiChange(this.resources)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mAlertDialog == null && requireContext() != null) {
            mView = requireActivity().layoutInflater.inflate(
                R.layout.dis_pk_voting_share_dialog_layout,
                null
            )
            initView(mView!!)
            mPresenter = SeckShareDialogPresenter(activity, this)

            if (merchantId.isNullOrEmpty()) {
                merchantId = SpUtils.getValue(context, SpKey.CHOSE_MC)
            }
            if (visitShopId.isNullOrEmpty()) {
                visitShopId = SpUtils.getValue(context, SpKey.CHOSE_SHOP)
            }
            //进入小程序投票页面
//            mPath = "packageB/pages/voteDetail/index";
            mPath = getNormalSurlWithScheme("PkVoteDetail", mExtraMap).split("\\?".toRegex())
                .toTypedArray()[0]
            // 活动列表
            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(mView)
                .create()
            initListener()
            getData()
        }
        return mAlertDialog!!
    }

    /**
     * 初始化View
     */
    private fun initView(view: View) {
        mRlContent = view.findViewById<RelativeLayout>(R.id.rl_content)
        dialogCardView = view.findViewById<View>(R.id.dialogCardView)
        shareViewLiner = view.findViewById<ConstraintLayout>(R.id.shareViewLiner)
        share_weixinLiner = view.findViewById<LinearLayout>(R.id.share_weixinLiner)
        share_friendLiner = view.findViewById<LinearLayout>(R.id.share_friendLiner)
        share_downLiner = view.findViewById<LinearLayout>(R.id.share_downLiner)
    }

    /**
     * 事件绑定
     */
    private fun initListener() {
        mRlContent?.setOnClickListener {
            dismiss()
        }

        //分享微信
        share_weixinLiner?.setOnClickListener {
            buildWeixin(viewConversionBitmap(dialogCardView!!))
        }

        //分享朋友圈
        share_friendLiner?.setOnClickListener {
            buildWeixinCircel(
                viewConversionBitmap(
                    dialogCardView!!
                )
            )
        }
        //保存图片
        share_downLiner?.setOnClickListener {
            if (PermissionUtils.hasPermissions(activity, *mPermissions)) { //读写存储权限
                if (!bitmapHasPic) {
                    Thread { viewSaveToImage(dialogCardView!!) }.start()
                } else {
                    Toast.makeText(activity, "已保存至相册！", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                PermissionUtils.requestPermissions(
                    activity,
                    RC_PERMISSION,
                    *mPermissions
                )
            }
        }
    }

    /**
     * PK投票分享布局
     */
    private fun buildPkVoting(faceUrl: String?, nickName: String?) {
        mView?.let {
            mAppLetsImg = it.findViewById<ImageView>(R.id.appletsImg)
            val mImgLayout = it.findViewById<ViewGroup>(R.id.tabs)
            val mPkVotingTitleLayout = it.findViewById<RelativeLayout>(R.id.pkVoting_title_layout)
            val mIvPkVotingSquare = it.findViewById<ImageView>(R.id.iv_pkVoting_square)
            val mIvPkVotingConSide = it.findViewById<ImageView>(R.id.iv_pkVoting_conSide)
            val mSharePkVotingTitle = it.findViewById<TextView>(R.id.share_pkVoting_title)
            val mPkVotingSquareName = it.findViewById<TextView>(R.id.tv_pkVoting_square_name)
            val mPkVotingConSideName = it.findViewById<TextView>(R.id.tv_pkVoting_conSide_name)
            val mPkVotingTextVs = it.findViewById<ImageView>(R.id.iv_pkVoting_text_vs)

            val mUserImg = it.findViewById<ImageView>(R.id.userImg)
            val mUserNickName = it.findViewById<TextView>(R.id.userNickName)
            val mShopLogoImg = it.findViewById<ImageView>(R.id.shareShopLogoImg)
            val mShareBottomBody = it.findViewById<TextView>(R.id.share_bottom_body)

            if (mActivityObject != null) {
                val postDetail = mActivityObject as PostDetail
                mActivityId = postDetail.id
                val model = postDetail.pkActivity
                val mBottomLayoutParams =
                    mShareBottomBody.layoutParams as ConstraintLayout.LayoutParams
                val layoutParams =
                    mPkVotingTitleLayout.layoutParams as ConstraintLayout.LayoutParams
                mSharePkVotingTitle.text = model.activityTitle
                mPkVotingSquareName.text = model.squareTitle
                mPkVotingConSideName.text = model.conSideTitle

                if (TextUtils.isEmpty(model.squareImgUrl) || TextUtils.isEmpty(model.conSideImgUrl)) {
                    mImgLayout.visibility = View.GONE
                    mPkVotingTextVs.visibility = View.VISIBLE
                    layoutParams.topMargin = TransformUtil.dp2px(context, 16f).toInt()
                    mBottomLayoutParams.topMargin = TransformUtil.dp2px(context, 21f).toInt()
                } else {
                    mImgLayout.visibility = View.VISIBLE
                    mPkVotingTextVs.visibility = View.GONE
                    layoutParams.topMargin = 0
                    mBottomLayoutParams.topMargin = TransformUtil.dp2px(context, 12f).toInt()

                    GlideCopy.with(context)
                        .load(model.squareImgUrl)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(mIvPkVotingSquare)

                    GlideCopy.with(context)
                        .load(model.conSideImgUrl)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(mIvPkVotingConSide)
                }

                /**
                 * 计算是否超出两行
                 */
                mPkVotingSquareName.post {
                    if (mPkVotingSquareName.lineCount < 2) {
                        mPkVotingSquareName.gravity = Gravity.CENTER
                    } else {
                        mPkVotingSquareName.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                    }
                    if (mPkVotingConSideName.lineCount < 2) {
                        mPkVotingConSideName.gravity = Gravity.CENTER
                    } else {
                        mPkVotingConSideName.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                    }
                }

                mPkVotingTitleLayout.layoutParams = layoutParams
                mShareBottomBody.layoutParams = mBottomLayoutParams

                val mShareBody = when {
                    model.currentUserLastVoteStand.isNullOrEmpty()
                        .not() && "1" == model.currentUserLastVoteStand -> "我是“${nickName}”，我支持“${model.squareTitle}”，你怎么看?"
                    model.currentUserLastVoteStand.isNullOrEmpty()
                        .not() && "2" == model.currentUserLastVoteStand -> "我是“${nickName}”，我支持“${model.conSideTitle}”，你怎么看?"
                    else -> "我是“${nickName}”，这么有意思的话题，和我一起投一票吧~"
                }
                mShareBottomBody.text = mShareBody
            }

            GlideCopy.with(mAppLetsImg?.context)
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(mAppLetsImg!!)

            mShopLogoImg.visibility = if (TextUtils.isEmpty(mShopLogo)) View.GONE else View.VISIBLE
            GlideCopy.with(mShopLogoImg.context)
                .load(mShopLogo)
                .into(mShopLogoImg)

            GlideCopy.with(mUserImg.context)
                .load(faceUrl)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(mUserImg)

            var mNickName = ""
            if (personId.isNullOrEmpty()) {
                mNickName = "来自\"${nickName}\"的推荐"
                mUserNickName?.text = mNickName
            } else {
                mNickName = "来自“<strong>${personId}母婴顾问</strong>”的推荐"
                mUserNickName?.text =
                    HtmlCompat.fromHtml(mNickName, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }

        }
    }

    override fun getData() {
        mPresenter?.getZxingCode()
    }

    override fun onGetUserInfoSuccess(faceUrl: String?, nickName: String?) {

    }

    override fun getZxingCode(result: String?, personId: String?) {
        this.referral_code = result
        this.personId = personId
        //请求101001 获取门店品牌Logo
        if (TextUtils.isEmpty(visitShopId)) {
            visitShopId = SpUtils.getValue(context, SpKey.CHOSE_SHOP)
        }
        mPresenter?.getStoreDetial(visitShopId)
    }

    /**
     * 门店详情(获取门店品牌Logo)
     *
     * @param shopLogo 品牌Logo
     */

    override fun onGetStoreDetialSuccess(shopLogo: String?, partnerMerchantLogo: String?) {
        mShopLogo = shopLogo
        if (activity == null) { //加固，避免显示弹框接口反应慢此时关闭弹框，后续接口成功回调出现问题
            return
        }
        val mUserIcon = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ICON)
        val mUserNick = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_NICK)
        buildPkVoting(mUserIcon, mUserNick)

        //局限在活动列表分享缓存此数据
        mMap.clear()
        //	小程序页面路径
        mMap["path"] = mPath!!

        /** PK投票-> 分享走向  */
        mMap["params"] =
            mExtraMap.puts("scheme", "PkVoteDetail")
                ?.puts("type", "8")
                ?.puts("referral_code", referral_code)
                ?.puts("id", mActivityId)
                ?.puts("merchantId", merchantId)
                ?.puts("shopId", visitShopId)!!

        //TODO 2021-09-22 调整分享都不走小程序太阳码
        mPresenter?.setAppProgram(mMap)
    }

    /**
     * 小程序二维码图片的base64编码 回调
     *
     * @param data
     */
    override fun onGetBase64DataSuccess(data: String?) {

    }

    override fun onGet32DataSuccess(data: String?) {
        val puts = SimpleHashMapBuilder<String, Any>().puts("keyFrame", data)
        mBase64Bitmap = CodeUtils.createImage(
            getNormalSurl(
                "HMMDEFAULT",
                puts
            ), 650, 650, null
        )

        mAppLetsImg?.let {
            if (context != null) {
                GlideCopy.with(context)
                    .load(mBase64Bitmap)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.hmhg_zxing)
                    .into(it)
            }
        }
    }

    //获取通常形式的分享二维码
    fun getNormalSurlWithScheme(scheme: String, extra: Map<String, String>): String {
        val urlPrefix = "hmmm://hmm/corresponding"
        var url = String.format(
            "%s?type=8&scheme=%s&referral_code=%s",
            urlPrefix,
            scheme,
            referral_code
        )
        mStringBuilder.setLength(0)
        mStringBuilder.append(url)
        for ((key, value) in extra) {
            //url += "&" + key + "=" + value;
            mStringBuilder.append("&").append(key).append("=").append(value)
        }
        url = mStringBuilder.toString()
        return url
    }

    //获取通常形式的分享二维码
    fun getNormalSurl(scheme: String, extra: SimpleHashMapBuilder<Any, Any>?): String {
        val urlPrefix = SpUtils.getValue(context, UrlKeys.H5_BargainUrl)
        var url = String.format(
            "%s?type=8&scheme=%s&referral_code=%s",
            urlPrefix,
            scheme,
            referral_code
        )
        mStringBuilder.setLength(0)
        mStringBuilder.append(url)
        if (extra != null) {
            for ((key, value) in extra) {
                //url += "&" + key + "=" + value;
                mStringBuilder.append("&").append(key).append("=").append(value)
            }
        }
        url = mStringBuilder.toString()
        println("分享的二维码:$url")
        return url
    }

    /**
     * 保存图片
     */
    fun viewSaveToImage(view: View) {
        // 把一个View转换成bitmap
        val bitmap: Bitmap = viewConversionBitmap(view)
        bitmapHasPic = true
        //bitmap转图片并保存本地
        saveBmp2Gallery(bitmap, "hmm" + Date().time) //用时间戳来命名图片名称
    }

    /**
     * view转bitmap
     */
    fun viewConversionBitmap(view: View): Bitmap {
        view.clearFocus()
//        Runtime.getRuntime().gc()
        val bitmap = Bitmap.createBitmap((view.width * 1), (view.height * 1), Bitmap.Config.RGB_565)
        if (bitmap != null) {
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            canvas.setBitmap(null)
        }
        return bitmap
    }

    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    fun saveBmp2Gallery(bmp: Bitmap, picName: String) {
        var fileName: String? = null
        //系统相册目录
        val galleryPath = (Environment.getExternalStorageDirectory()
            .toString() + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator)


        // 声明文件对象
        var file: File? = null
        // 声明输出流
        var outStream: FileOutputStream? = null
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = File(galleryPath, "$picName.jpg")

            // 获得文件相对路径
            fileName = file.toString()
            // 获得输出流，如果文件中有内容，追加内容
            outStream = FileOutputStream(fileName)
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            }
            bmp.recycle()
        } catch (e: Exception) {
            e.stackTrace
        } finally {
            try {
                outStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //通知相册更新
//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
//                    bmp, fileName, null);
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val uri = Uri.fromFile(file)
            intent.data = uri
            requireActivity().sendBroadcast(intent)
            requireActivity().runOnUiThread {
                Toast.makeText(
                    activity,
                    "已保存至相册！",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    /**
     * 分享微信
     */
    private fun buildWeixin(bitmap: Bitmap) {
        val thumb = UMImage(activity, bitmap)
        thumb.setThumb(thumb)
        ShareAction(activity)
            .withText("限时秒杀")
            .setPlatform(SHARE_MEDIA.WEIXIN)
            .withMedia(thumb)
            .setCallback(umShareListener)
            .share()
        dismiss()
    }

    /**
     * 分享朋友圈
     *
     * @param bitmap
     */
    private fun buildWeixinCircel(bitmap: Bitmap) {
        val thumb = UMImage(activity, bitmap)
        thumb.setThumb(thumb)
        ShareAction(activity)
            .withText("限时秒杀")
            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
            .withMedia(thumb)
            .setCallback(umShareListener)
            .share()
        dismiss()
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

    override fun showDataErr() {

    }
}