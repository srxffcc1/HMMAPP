package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.servicecenter.R;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ParseUtils;

import java.util.List;

public class AppointmentMainAdapter extends BaseQuickAdapter<AppointmentMainItemModel, BaseViewHolder> {

    public AppointmentMainAdapter() {
        this(R.layout.appointment_main_adapter_layout);
    }

    public AppointmentMainAdapter(int viewId) {
        super(viewId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppointmentMainItemModel item) {
        TextView goodSpace = helper.getView(R.id.goodSpace);
        if (item == null) {
            return;
        }
        List<AppointmentMainItemModel.AttributeList> attributeList = item.getAttributeList();
        String goodsMoney = "";
        //售价规格（1:单品 2:多规格）
        if ("1".equals(item.getPriceType())) {
            //goodSpace.setVisibility(View.GONE);
            AppointmentMainItemModel.AttributeList attribute = item.getAttribute();
            if (attribute != null) {
                goodsMoney = FormatUtils.moneyKeep2Decimals(attribute.getPrice());
            }
        } else {
            if (!ListUtil.isEmpty(attributeList)) {
                //goodSpace.setVisibility(View.VISIBLE);
                //goodSpace.setText(attributeList.get(0).getName());
                goodsMoney = FormatUtils.moneyKeep2Decimals(attributeList.get(0).getPrice()) + "起";
            }
        }
        int mNumber = item.getSoldNumber() + item.getVirtualSoldNumber();

        helper.setText(R.id.goodsTitle, item.getName())
                .setText(R.id.goodsMoney, goodsMoney)
                .setText(R.id.goodsSales, "已售" + ParseUtils.parseNumber(String.valueOf(mNumber), 10000, "万"));

        List<String> imgList = item.getImgList();
        String url = "";
        if (!ListUtil.isEmpty(imgList)) {
            url = imgList.get(0);
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(url)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into((ImageView) helper.getView(R.id.goodsImg));
    }
}
