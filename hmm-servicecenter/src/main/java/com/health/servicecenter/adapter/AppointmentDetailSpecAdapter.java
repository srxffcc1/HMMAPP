package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.AppointmentMainItemModel;

import java.util.List;

public class AppointmentDetailSpecAdapter extends BaseAdapter<AppointmentMainItemModel> {

    private View.OnClickListener onSelectAttributeListener;
    private TextView specValue;

    public AppointmentDetailSpecAdapter() {
        this(R.layout.appointment_detial_spec_adapter_layout);
    }

    public AppointmentDetailSpecAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        ConstraintLayout specLayout;
        TextView title;
        ImageView specMore;
        ConstraintLayout serviceLayout;
        TextView serviceTitle;
        TextView serviceValue;

        specLayout = (ConstraintLayout) holder.getView(R.id.specLayout);
        title = (TextView) holder.getView(R.id.title);
        specValue = (TextView) holder.getView(R.id.specValue);
        specMore = (ImageView) holder.getView(R.id.specMore);
        serviceLayout = (ConstraintLayout) holder.getView(R.id.serviceLayout);
        serviceTitle = (TextView) holder.getView(R.id.serviceTitle);
        serviceValue = (TextView) holder.getView(R.id.serviceValue);
        View tabsView = holder.getView(R.id.tabs);


        AppointmentMainItemModel model = getModel();

        serviceValue.setText("服务时长" + model.getDuration() + "分钟");
        if ("1".equals(model.getPriceType())) {
            specLayout.setVisibility(View.GONE);
            tabsView.setVisibility(View.GONE);
        } else {
            List<AppointmentMainItemModel.AttributeList> attributeList = model.getAttributeList();
            if (ListUtil.isEmpty(attributeList)) {
                specLayout.setVisibility(View.GONE);
                tabsView.setVisibility(View.GONE);
            } else {
                tabsView.setVisibility(View.VISIBLE);
                specLayout.setVisibility(View.VISIBLE);
                //specValue.setText("次卡等" + attributeList.size() + "种规格");
                specValue.setText("已选:“" + attributeList.get(0).getName() + "”");
                specLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelectAttributeListener != null) {
                            onSelectAttributeListener.onClick(v);
                        }
                    }
                });
            }
        }
    }

    public void setOnSelectAttributeListener(View.OnClickListener onSelectAttributeListener) {
        this.onSelectAttributeListener = onSelectAttributeListener;
    }

    public void setData(AppointmentMainItemModel.AttributeList attributeList) {
        if (specValue != null) {
            specValue.setText("已选:“" + attributeList.getName() + "”");
        }
    }

}
