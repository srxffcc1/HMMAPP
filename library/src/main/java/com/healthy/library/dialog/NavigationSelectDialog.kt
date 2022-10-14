package com.healthy.library.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.healthy.library.R
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.utils.NavigateUtils

/**
 * @author Long
 * @desc: 导航类型选择弹框
 * @createTime :2021/10/9 15:43
 */
class NavigationSelectDialog : DialogFragment() {
    
    companion object {
        fun newInstance(): NavigationSelectDialog {
            val mBundle = Bundle()
            val mNavigationSelectDialog = NavigationSelectDialog()
            mNavigationSelectDialog.arguments = mBundle
            return mNavigationSelectDialog
        }
    }

    private var mStoreDetailModel: ShopDetailModel? = null

    fun setStoreDetailModel(model: ShopDetailModel): NavigationSelectDialog {
        mStoreDetailModel = model
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val rootView = inflater.inflate(R.layout.dialog_navigation_select_layout, null)

        val mNavigationBaidu = rootView.findViewById<ViewGroup>(R.id.ll_baidu)
        val mNavigationGaode = rootView.findViewById<ViewGroup>(R.id.ll_gaode)

        mNavigationBaidu.setOnClickListener(this::navigation)
        mNavigationGaode.setOnClickListener(this::navigation)

        val mAlertDialog = builder.setView(rootView).create()
        var window: Window? = mAlertDialog.window
        window?.let {
            it.setWindowAnimations(R.style.BottomDialogAnimation)
            val decorView = it.decorView
            decorView.setPadding(0, 0, 0, 0)
            decorView.setBackgroundColor(Color.TRANSPARENT)
            val params = it.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.BOTTOM
        }

        return mAlertDialog
    }

    /**
     * 跳转导航app
     */
    private fun navigation(view: View) {
        val which = when (view.id) {
            R.id.ll_gaode -> 1
            else -> 0
        }
        if (which == 1) {
            if (!checkApkExist("com.autonavi.minimap")) {
                Toast.makeText(context, "检测到您未安装高德地图...", Toast.LENGTH_LONG).show()
                return
            }
        } else {
            if (!checkApkExist("com.baidu.BaiduMap")) {
                Toast.makeText(context, "检测到您未安装百度地图...", Toast.LENGTH_LONG).show()
                return
            }
        }
        dismiss()
        NavigateUtils.navigateNoDialog(
            requireContext(),
            mStoreDetailModel?.addressDetails,
            mStoreDetailModel?.longitude.toString(),
            mStoreDetailModel?.latitude.toString(),
            which
        )
    }

    /**
     * 根据包名判断应用是否安装
     * 高德地图包名：com.autonavi.minimap
     * 百度地图包名：com.baidu.BaiduMap
     *
     * @param packageName
     * @return
     */
    @SuppressLint("UseRequireInsteadOfGet")
    private fun checkApkExist(packageName: String?): Boolean {
        return if (packageName == null || "" == packageName) {
            false
        } else try {
            val info: ApplicationInfo = activity!!.getPackageManager().getApplicationInfo(
                packageName,
                PackageManager.GET_UNINSTALLED_PACKAGES
            )
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}