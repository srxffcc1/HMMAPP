package com.health.servicecenter.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.servicecenter.R;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;

import java.util.List;

public class AppointmentListAdapter extends BaseQuickAdapter<AppointmentListItemModel, BaseViewHolder> {

    @Override
    protected void convert(BaseViewHolder helper, AppointmentListItemModel item) {

        String appointmentTip = "";
        if (1 == item.getStatus()) appointmentTip = "待接单";
        if (2 == item.getStatus()) appointmentTip = "预约成功";
        if (3 == item.getStatus()) appointmentTip = "已完成";
        if (4 == item.getStatus()) appointmentTip = "已取消";
        String mTime = item.getAppointmentDate() + " " + DateUtils.getDateTimeSplit(item.getAppointmentRangeStartTime()) + "-" + DateUtils.getDateTimeSplit(item.getAppointmentRangeEndTime());
        //	1待接单		2带到店		3已完成		4已取消
        helper.setGone(R.id.wheel_cancel_appointment, (1 == item.getStatus() || 2 == item.getStatus()) && 1 == item.getSupportRefund())
                .setText(R.id.tv_appointment_title, item.getProjectName())
                .setText(R.id.tv_appointment_address, item.getShopName())
                .setText(R.id.tv_appointment_price, FormatUtils.moneyKeep2Decimals(item.getAppointmentAmount()))
                .setText(R.id.tv_appointment_tip, appointmentTip)
                .setText(R.id.tv_appointment_time, mTime)
                .setText(R.id.tv_appointment_payType, 1 == item.getPayType() ? "到店付款 ¥" : "在线支付 ¥")
                .addOnClickListener(R.id.wheel_cancel_appointment);

        List<String> imgList = item.getImgList();
        String url = "";
        if (!ListUtil.isEmpty(imgList)) {
            url = imgList.get(0);
        }

        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(url)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into((ImageView) helper.getView(R.id.iv_appointment));

    }

    public AppointmentListAdapter() {
        super(R.layout.item_appointment_list);
    }
}
