package com.health.mine.adapter

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.mine.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.dialog.SingleWheelDialog
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.widget.NewInputView
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * @description
 * @author long
 * @date 2021/7/23
 */
class EnlistDetailsBottomAdapter
    : BaseAdapter<EnlistActivityModel>(R.layout.mine_recyclerview_enlist_item_bottom) {


    var model: EnlistActivityModel? = null
    var mEnlistEditName: NewInputView? = null
    var mEnlistEditPhone: NewInputView? = null
    var mSexFemale: RadioButton? = null
    var mSexMale: RadioButton? = null
    var mChooseStatusRightBody: TextView? = null
    var mTabsView: View? = null
    private var mStatusTypeDialog: SingleWheelDialog? = null

    var editEnlistName = ""
    var editEnlistPhone = ""
    var editEnlistSex = 1
    var enlistChooseStatus = ""

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        mEnlistEditName = holder.getView(R.id.mine_enlist_edit_name)
        val mEnlistRadioGroup = holder.getView<RadioGroup>(R.id.radioGroup)
        mSexMale = holder.getView<RadioButton>(R.id.sex_male)
        mSexFemale = holder.getView<RadioButton>(R.id.sex_female)
        mEnlistEditPhone = holder.getView(R.id.mine_enlist_edit_phone)
        mChooseStatusRightBody = holder.getView<TextView>(R.id.chooseStatus_right_body)
        mTabsView = holder.getView<View>(R.id.tabs)
        mEnlistEditPhone?.setInputType(2)

        mEnlistEditName?.setRightEditBody("")
        mEnlistEditPhone?.setRightEditBody("")
        mSexMale?.isChecked = true
        mChooseStatusRightBody?.text = ""

        holder.setOnClickListener(R.id.cl_mine_enlist_ChooseStatus, View.OnClickListener {
            chooseStatusType()
        })

        mEnlistEditName?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editEnlistName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        mEnlistEditPhone?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editEnlistPhone = s.toString().replace(" ", "")
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        /** 性别单选 */
        mEnlistRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            mSexFemale?.isChecked = checkedId == R.id.sex_female
            mSexMale?.isChecked = checkedId == R.id.sex_male
            this.editEnlistSex = if (checkedId == R.id.sex_male) 1 else 0
        }
    }

    fun changeTabs(isShow: Boolean) {
        mTabsView?.let {
            it.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * 选择阶段
     */
    fun chooseStatusType() {
        if (mStatusTypeDialog == null) {
            val list = ArrayList<String>()
            list.add("备孕期")
            list.add("怀孕中")
            list.add("育儿期")
            mStatusTypeDialog = SingleWheelDialog.newInstance(list)
            mStatusTypeDialog?.setOnConfirmClick { pos, data ->
                enlistChooseStatus = data
                mChooseStatusRightBody?.text = enlistChooseStatus
            }
        }
        if (context is FragmentActivity) {
            mStatusTypeDialog?.show(
                (context as FragmentActivity).supportFragmentManager,
                "statusType"
            )
        }
    }

    /**
     * 默认可以输入字母、汉字、数字以及()（）.．·等符号，s为筛选条件
     */
    /*inner class OppositePersonalFilter(val s: String = "[a-zA-Z|\u4e00-\u9fa5]+") : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val p = Pattern.compile(s)
            val m = p.matcher(source.toString())

            for (i in start until end) {
                val charGet = source?.get(i)
                if (!Character.isLetterOrDigit(charGet!!)
                    && !m.matches()
                ) {
                    Toast.makeText(context, "不能输入特殊字符", Toast.LENGTH_SHORT).show()
                    return ""
                }
            }
            return null
        }
    }*/

}