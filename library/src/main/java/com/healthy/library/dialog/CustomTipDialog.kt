package com.healthy.library.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.healthy.library.R
import com.healthy.library.base.BaseDialogFragment

/**
 * @description 自定义提示Dialog
 * @author long
 * @date 2021/7/2
 */
class CustomTipDialog : BaseDialogFragment() {
    private var mTextView: TextView? = null
    private var mTipImgView: ImageView? = null

    /*** toast 文本内容*/
    private var message: CharSequence = ""

    /*** toast 提示类型*/
    private var toastType: Int = TOAST_TYPE_SUCCESS

    /*** 弹框关闭回调 */
    private var onDismissListener: DialogInterface.OnDismissListener? = null

    companion object {
        const val TOAST_TYPE_SUCCESS: Int = 0x01;
        const val TOAST_TYPE_ERROR: Int = 0x02;

        fun newInstance(): CustomTipDialog {
            val mBundle = Bundle()
            val mCustomTipDialog = CustomTipDialog()
            mCustomTipDialog.arguments = mBundle
            return mCustomTipDialog
        }
    }

    fun setMessage(message: CharSequence): CustomTipDialog {
        this.message = message
        return this
    }

    fun setTipType(toastType: Int): CustomTipDialog {
        this.toastType = toastType
        return this
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): CustomTipDialog {
        this.onDismissListener = onDismissListener
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val rootView = inflater.inflate(R.layout.custom_toast, null)
        //初始化布局控件
        mTextView = rootView.findViewById(R.id.tipToastContent)
        mTipImgView = rootView.findViewById(R.id.tipToastImg)

        mTextView?.text = message
        var mResource = 0
        if (toastType == TOAST_TYPE_SUCCESS) {
            mResource = R.drawable.toast_success_img
        }
        if (toastType == TOAST_TYPE_ERROR) {
            mResource = R.drawable.toast_error_img
        }
        mTipImgView?.setImageResource(mResource)
        val mAlertDialog = builder.setView(rootView).create()
        var window: Window? = mAlertDialog.window
        window?.let {
            //it.setWindowAnimations(R.style.DialogAlphaAnimation)
            val decorView = it.decorView
            decorView.setPadding(0, 0, 0, 0)
            decorView.setBackgroundColor(Color.TRANSPARENT)
            val params = it.attributes
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.CENTER
        }

        rootView.postDelayed({
            mAlertDialog?.let {
                if (mAlertDialog.isShowing) {
                    mAlertDialog.dismiss()
                }
            }
        }, 2000)

        return mAlertDialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.let {
            it.onDismiss(dialog)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}