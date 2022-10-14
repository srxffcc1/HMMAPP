package com.healthy.library.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.healthy.library.R

/**
 * author : long
 * Time   :2021/11/16
 * desc   :
 */
class PkVotingTipDialog : DialogFragment() {

    private var mAlertDialog: AlertDialog? = null
    private var mTipTitle: String = "投票次数不足！"
    private var mTipMessage: String = "亲，每天只可以投X票哦~~"
    private var rootView: View? = null
    private var mTvTipTitle: TextView? = null
    private var mTvTipMessage: TextView? = null

    fun setBody(tipTitle: String, tipMessage: String): PkVotingTipDialog {
        this.mTipTitle = tipTitle
        this.mTipMessage = tipMessage
        return this
    }

    companion object {
        fun newInstance(): PkVotingTipDialog {
            val args = Bundle()
            val fragment = PkVotingTipDialog()
            fragment.arguments = args
            return fragment
        }
    }

    val mRunable = Runnable {
        mAlertDialog?.let {
            if (mAlertDialog!!.isShowing) {
                mAlertDialog!!.dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        rootView = inflater.inflate(R.layout.dialog_pk_voting_tip_layout, null)
        //初始化布局控件
        mTvTipTitle = rootView?.findViewById(R.id.dialog_pkVoting_tip_title)
        mTvTipMessage = rootView?.findViewById(R.id.dialog_pkVoting_tip_message)

        mTvTipTitle?.text = mTipTitle
        mTvTipMessage?.text = mTipMessage
        mAlertDialog = builder.setView(rootView).create()
        mAlertDialog?.setCancelable(false)
        mAlertDialog?.setCanceledOnTouchOutside(false)

        var window: Window? = mAlertDialog!!.window
        window.let {
            it?.setWindowAnimations(R.style.ScaleAnimation)
            val decorView = it?.decorView
            decorView?.setPadding(0, 0, 0, 0)
            decorView?.setBackgroundColor(Color.TRANSPARENT)
            val params = it?.attributes
            params?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.gravity = Gravity.CENTER
            it?.attributes = params
        }

        rootView?.postDelayed(mRunable, 2000)
        return mAlertDialog!!
    }

    override fun onDestroyView() {
        rootView?.removeCallbacks(mRunable)
        super.onDestroyView()
    }

}