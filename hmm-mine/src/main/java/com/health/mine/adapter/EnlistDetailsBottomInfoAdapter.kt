package com.health.mine.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.mine.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.EnlistActivityModel

/**
 * @description
 * @author long
 * @date 2021/7/23
 */
class EnlistDetailsBottomInfoAdapter
    : BaseAdapter<EnlistActivityModel>(R.layout.mine_recyclerview_enlist_item_bottom_info) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        getModel()?.let {
            val mPhone =
                if (it.enlistPhone.isNullOrEmpty().not()) {
                    it.enlistPhone.substring(0, 3)
                        .toString() + "****" + it.enlistPhone.substring(
                        7,
                        it.enlistPhone.length
                    )
                } else {
                    it.enlistPhone
                }
            holder.setText(R.id.tv_info_name, it.enlistName)
                .setText(R.id.tv_info_sex, if (it.enlistSex == 1) "男" else "女")
                .setText(R.id.tv_info_phone, mPhone)
        }
    }

}