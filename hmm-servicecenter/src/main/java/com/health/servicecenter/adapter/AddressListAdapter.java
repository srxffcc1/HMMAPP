package com.health.servicecenter.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.activity.AddReceivingAddress;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class AddressListAdapter extends BaseAdapter<AddressListModel> {

    boolean isNeedSelect;
    OnAddressClickListener onAddressClickListener;

    public void setOnAddressClickListener(OnAddressClickListener onAddressClickListener) {
        this.onAddressClickListener = onAddressClickListener;
    }

    public void setNeedSelect(boolean needSelect) {
        isNeedSelect = needSelect;
    }

    public AddressListAdapter() {
        this(R.layout.item_address_list_layout);
    }

    public AddressListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final ImageView address_edit = holder.getView(R.id.address_edit);
        TextView user_name = holder.getView(R.id.user_name);
        TextView user_phone = holder.getView(R.id.user_phone);
        TextView user_address = holder.getView(R.id.user_address);
        TextView default_address = holder.getView(R.id.default_address);
        final LinearLayout address_liner = holder.getView(R.id.address_liner);
        if (getDatas().get(position).getOrderBy() == -1) {
            default_address.setVisibility(View.VISIBLE);
        } else {
            default_address.setVisibility(View.GONE);
        }
        user_name.setText("");
        if (!TextUtils.isEmpty(getDatas().get(position).getConsigneeName())) {
            user_name.setText(getDatas().get(position).getConsigneeName());
        }
        user_phone.setText("");
        if (!TextUtils.isEmpty(getDatas().get(position).getConfigneePhone())) {
            user_phone.setText(getDatas().get(position).getConfigneePhone());
        }
        user_address.setText("");
        if (!TextUtils.isEmpty(getDatas().get(position).getAddress())) {
            user_address.setText(getDatas().get(position).getProvinceName()
                    + getDatas().get(position).getCityName()
                    + getDatas().get(position).getDistrictName()
                    + getDatas().get(position).getAddress()
                    + getDatas().get(position).getHouseNum());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeedSelect) {
                    onAddressClickListener.onAddressClick(getDatas().get(position));
                }
            }
        });
        address_liner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (moutClickListener != null) {
                    //address_liner.setBackgroundColor(Color.parseColor("#f5f5f9"));
                    moutClickListener.outClick("delete", getDatas().get(position).getId() + "");
                }
                return false;
            }
        });
        address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddReceivingAddress.class);
                intent.putExtra("listModel", getDatas().get(position));
                intent.putExtra("type", 1);
                context.startActivity(intent);


            }
        });

    }

    public interface OnAddressClickListener {
        void onAddressClick(AddressListModel addressListModel);
    }
}
