package com.healthy.library.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.healthy.library.R
import com.healthy.library.base.BaseDialogFragment
import com.healthy.library.fragment.PhotoFragment
import com.healthy.library.fragment.TencentVideoFragment

/**
 * @description
 * @author long
 * @date 2021/6/23
 */
class PhotoShowDialog : BaseDialogFragment() {

    private var mAlertDialog: AlertDialog? = null
    private var mMap: MutableMap<String, Any> = mutableMapOf()

    /** 0 视频类型 1 图片类型 */
    private var mType: Int = 0
    private lateinit var mTencentVideoFragment: TencentVideoFragment
    private lateinit var mPhotoFragment: PhotoFragment

    companion object {
        fun newInstance(type: Int): PhotoShowDialog {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = PhotoShowDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mType = arguments?.getInt("type")!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mAlertDialog == null && requireContext() != null) {
            val view: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.dialog_content_layout, null)

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
                    //decorView.setBackgroundResource(R.drawable.shape_dialog)
                    val params = window.attributes
                    params.width = WindowManager.LayoutParams.MATCH_PARENT
                    params.gravity = Gravity.CENTER
                }

                setOnDismissListener {
                    childFragmentManager.beginTransaction().remove(
                        childFragmentManager.findFragmentById(R.id.dialog_content_fl)!!
                    ).commit()
                }
            }

            if (0 == mType) {
                mMap["url"] =
                    "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4,http://vjs.zencdn.net/v/oceans.mp4"//
                mMap["isDialogStyle"] = true
                //mMap["isAutoPlay"] = true
                mTencentVideoFragment = TencentVideoFragment.newInstance(mMap)
            }

            if (1 == mType) {
                mMap["url"] =
                    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2604085216,1485343016&fm=26&gp=0.jpg";
                mPhotoFragment = PhotoFragment.newInstance(mMap)
            }
        }
        return mAlertDialog!!
    }

    override fun onResume() {
        super.onResume()

        val beginTransaction = childFragmentManager.beginTransaction()

        if (0 == mType) {
            if (mTencentVideoFragment.isVisible) {
                return
            }
            beginTransaction
                .add(R.id.dialog_content_fl, mTencentVideoFragment, "VideoFragment")
                .show(mTencentVideoFragment)
                .commit()
        }

        if (1 == mType) {
            if (mPhotoFragment.isVisible) {
                return
            }
            beginTransaction
                .add(R.id.dialog_content_fl, mPhotoFragment, "PhotoFragment")
                .show(mPhotoFragment)
                .commit()
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