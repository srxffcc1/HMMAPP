package com.health.mine.adapter

import android.graphics.Typeface
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.mine.R
import com.health.mine.model.EnlistSignOffModel
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.widget.NewInputView

/**
 * @description
 * @author long
 * @date 2021/7/24
 */
class EnlistSignOffAdapter :
    BaseAdapter<EnlistSignOffModel>(R.layout.mine_recyclerview_signoff_info) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val mSignOffInfo = holder.getView<NewInputView>(R.id.niv_mine_sign_off_info)

        getDatas()?.let {
            val enlistSignOffModel = it[position]
            mSignOffInfo.setTitle(enlistSignOffModel.key)
            mSignOffInfo.setRightBody(enlistSignOffModel.value)
            mSignOffInfo.rightTextView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)//加粗
            holder.setVisible(R.id.sv_mine_sign_off_info_bottom, position == it.size - 1)
        }
    }
}