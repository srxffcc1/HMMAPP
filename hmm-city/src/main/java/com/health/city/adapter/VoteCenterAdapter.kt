package com.health.city.adapter

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.dialog.SingleWheelDialog
import com.healthy.library.model.WidgetInputModel
import com.healthy.library.widget.NewInputView
import java.util.*
import java.util.regex.Pattern


/**
 * @description 报名中间页
 * @author long
 * @date 2021/6/22
 */
class VoteCenterAdapter : BaseAdapter<WidgetInputModel>(R.layout.city_item_vote_enroll_layout) {

    private lateinit var mSexDialog: SingleWheelDialog
    var mHasFocus: Boolean = false
    var mDataMap: MutableMap<String, String> = mutableMapOf()


    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        buildEnroll(holder, position)
    }

    /**
     * 报名
     */
    private fun buildEnroll(holder: BaseHolder, position: Int) {
        val dataSize = getDatas().size
        holder.setVisible(R.id.vote_enroll_inputView, position < dataSize - 2)
        holder.setVisible(R.id.tv_slogan, position == dataSize - 2)
        holder.setVisible(R.id.edit_slogan, position == dataSize - 2)
        holder.setVisible(R.id.submit_layout, position == dataSize - 1)
            .setOnClickListener(R.id.tv_submit, View.OnClickListener {
                if (isClickInit()) {
                    moutClickListener.outClick("submit", position)
                }
            })

        if (position == dataSize - 1) {
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.trans_parent))
        } else {
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.colorWhite))
        }

        val mInputView = holder.getView<NewInputView>(R.id.vote_enroll_inputView)
        val mTvSlogan = holder.getView<AppCompatTextView>(R.id.tv_slogan)
        val mEditSlogan = holder.getView<AppCompatEditText>(R.id.edit_slogan)
        getDatas().let {

            val mTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    getDatas()[position].rightEditBody = s.toString().trim()
                    mDataMap[getDatas()[position].title] =
                        getDatas()[position].rightEditBody
                }
            }

            mInputView.apply {
                setTitle(it[position].title)
                setModel(it[position].isEdit)
                setRightEditHint(it[position].rightEditHint)
                setRightBody(it[position].rightEditHint)
                it[position].inputView = this
                it[position].inputView.editText.apply {
                    onFocusChangeListener =
                        View.OnFocusChangeListener { v, hasFocus ->
                            if (hasFocus && isClickInit()) {
                                mHasFocus = hasFocus;
                                moutClickListener.outClick("editFocusChange", position)
                                it[position].inputView.editText.addTextChangedListener(mTextWatcher);
                            } else {
                                it[position].inputView.editText.removeTextChangedListener(
                                    mTextWatcher
                                )
                            }
                        }

                    filters = if ("姓名" == it[position].title) {
                        arrayOf(OppositePersonalFilter(), InputFilter.LengthFilter(5))
                    } else {
                        arrayOf(OppositePersonalFilter())
                    }
                }

                if ("性别" == it[position].title) {
                    mInputView.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            showSexDialog(it[position], mInputView)
                        }
                    })
                }

                if ("联系方式" == it[position].title) {
                    it[position].inputView.setInputType(2);
                }
            }

            if (position == dataSize - 2) {
                mTvSlogan.text = it[position].title
                mEditSlogan.hint = it[position].rightEditHint
                it[position].editText = mEditSlogan
                it[position].editText.apply {
                    onFocusChangeListener =
                        View.OnFocusChangeListener { v, hasFocus ->
                            if (hasFocus && isClickInit()) {
                                mHasFocus = hasFocus;
                                moutClickListener.outClick("editFocusChange", position)
                                it[position].editText.addTextChangedListener(mTextWatcher);
                            } else {
                                it[position].editText.removeTextChangedListener(mTextWatcher)
                            }
                        }
                    filters = arrayOf(OppositePersonalFilter())
                }
            }
        }
    }

    /**
     * 选择性别
     */
    fun showSexDialog(mWidgetInputModel: WidgetInputModel, mView: NewInputView) {
        if (::mSexDialog.isInitialized.not()) {
            val list = ArrayList<String>()
            list.add("男")
            list.add("女")
            mSexDialog = SingleWheelDialog.newInstance(list, "性别")
            mSexDialog.setOnConfirmClick { pos, data ->
                mView.setRightColor(context.resources.getColor(R.color.color_333333))
                mView.setRightBody(data)
                mWidgetInputModel.rightEditBody = data
                mDataMap[mWidgetInputModel.title] = data
            }
        }
        mSexDialog.show((context as? FragmentActivity)?.supportFragmentManager!!, "sexDialog")
    }


    /**
     * 默认可以输入字母、汉字、数字以及()（）.．·等符号，s为筛选条件
     */
    inner class OppositePersonalFilter(val s: String = "[a-zA-Z|\u4e00-\u9fa5]+") : InputFilter {
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

    }
}