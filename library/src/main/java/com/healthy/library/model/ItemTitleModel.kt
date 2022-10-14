package com.healthy.library.model

import com.healthy.library.R
import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/9/15
 */
class ItemTitleModel() : Serializable {
    var title: String = ""
    var titleSize: Float = 0f
    var titleColor: String = "#4e4846"
    var titleIsBold: Boolean = true

    var titleRight: String = ""
    var titleRightSize: Float = 0f
    var titleRightColor: String = "#b31724"
    var titleRightIsBold: Boolean = true

    var itemRemark: String = ""
    var itemRemarkSize: Float = 0f
    var itemRemarkColor: String = "#666666"
    var itemRemarkIsBold: Boolean = true

    fun getItemRemarkHtml(): String {
        if (itemRemark.contains("服务门店")) {
            return itemRemark.replace("服务门店", "") + "<font color='#FF614F'>" + itemRemark.substring(
                itemRemark.length - 4,
                itemRemark.length
            ) + "</font>"
        } else {
            return itemRemark
        }
    }

}