package com.healthy.library.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.healthy.library.R
import com.healthy.library.constant.SpKey
import com.healthy.library.constant.UrlKeys
import com.healthy.library.model.LotteryInfoModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils

/**
 * author : long
 * Time   :2021/11/8
 * desc   :
 */
class PointsSignInLotteryDialog : DialogFragment() {

    var mAlertDialog: AlertDialog? = null
    private var mLotteryInfoModel: LotteryInfoModel? = null

    fun setLotteryInfoModel(lotteryInfoModel: LotteryInfoModel): PointsSignInLotteryDialog {
        this.mLotteryInfoModel = lotteryInfoModel
        return this
    }

    companion object {
        fun newInstance(): PointsSignInLotteryDialog {
            val args = Bundle()
            val fragment = PointsSignInLotteryDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if (mAlertDialog == null && requireContext() != null) {
            val view: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_points_signin_lottery_layout, null)

            initView(view)

            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true)
                .create()

            mAlertDialog?.apply {
                isCancelable = false
                setCanceledOnTouchOutside(false)

                val window = mAlertDialog!!.window
                if (window != null) {
                    window.setWindowAnimations(R.style.ScaleAnimation)
                    val decorView = window.decorView
                    decorView.setPadding(0, 0, 0, 0)
                    decorView.setBackgroundColor(Color.TRANSPARENT)
                    //decorView.setBackgroundResource(R.drawable.shape_dialog)
                    val params = window.attributes
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.gravity = Gravity.CENTER
                    window.attributes = params
                }
            }
        }

        return mAlertDialog!!
    }

    private fun initView(view: View) {

        val mTvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val mConfirm = view.findViewById<ImageView>(R.id.iv_dialog_lottery_confirm)

        mLotteryInfoModel?.let {
            mTvTitle.text =
                "您已${if (it.memberSignType == 0) "连续" else "累计"}签到${it.memberSignCount}天"
        }
        mConfirm.setOnClickListener {
            mLotteryInfoModel?.let {
                val token = SpUtils.getValue(requireContext(), SpKey.TOKEN)
                var url: String? = "http://192.168.10.181:8000/lottery.html"
                if (!TextUtils.isEmpty(
                        SpUtils.getValue(
                            requireContext(),
                            UrlKeys.H5_LOTTERY_URL
                        )
                    )
                ) {
                    url = SpUtils.getValue(requireContext(), UrlKeys.H5_LOTTERY_URL)
                }
                url += "?id=" + it.lotteryId + "&token=" + token
                ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                    .withString("url", url)
                    .withBoolean("isShowTopBar", false)
                    .navigation()
                dismiss()
            }
        }
    }

}