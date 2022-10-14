package com.healthy.library.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.healthy.library.R
import com.healthy.library.businessutil.GlideCopy
import com.uuzuche.lib_zxing.activity.CodeUtils

/**
 * description 查看核销码
 * author long
 * date 2021/7/23
 */
class QueryCodeDialog : DialogFragment() {

    /** 二维码地址 */
    private var mCode: String = ""

    private var mAlertDialog: AlertDialog? = null

    companion object {
        fun newInstance(code: String): QueryCodeDialog {
            val args = Bundle()
            args.putString("code", code)
            val fragment = QueryCodeDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mCode = it.getString("code")!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mAlertDialog == null && requireContext() != null) {
            val view: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_query_code_layout, null)

            val mIvCode = view.findViewById<ImageView>(R.id.iv_query_code)

            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true)
                .create()

            val mBase64Bitmap = CodeUtils.createImage(mCode, 650, 650, null)
            GlideCopy.with(context)
                .load(mBase64Bitmap)
                .error(R.drawable.img_1_1_default)
                .into(mIvCode)

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
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    params.gravity = Gravity.CENTER
                }
            }
        }

        return mAlertDialog!!
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {

        }
    }
}