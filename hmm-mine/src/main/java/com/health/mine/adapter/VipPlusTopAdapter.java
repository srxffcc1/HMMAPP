package com.health.mine.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;

public class VipPlusTopAdapter extends BaseAdapter<String> {



    public VipPlusTopAdapter() {
        this(R.layout.item_vip_right_top);
    }

    public VipPlusTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
         TextView topCardBlackBg;
         TextView topBarTmp;
         ImageView topCardBg;
         ImageView vipCardLeft;
         final TextView vipName;
         TextView vipTime;
         TextView vipCardStatus;
         TextView vipTitle;
         ImageView vipSuperBg;
        topCardBlackBg = (TextView) holder.itemView.findViewById(R.id.top_card_black_bg);
        topBarTmp = (TextView) holder.itemView.findViewById(R.id.top_bar_tmp);
        topCardBg = (ImageView) holder.itemView.findViewById(R.id.top_card_bg);
        vipCardLeft = (ImageView) holder.itemView.findViewById(R.id.vip_card_left);
        vipName = (TextView) holder.itemView.findViewById(R.id.vip_name);
        vipTime = (TextView) holder.itemView.findViewById(R.id.vip_time);
        vipCardStatus = (TextView) holder.itemView.findViewById(R.id.vip_card_status);
        vipTitle = (TextView) holder.itemView.findViewById(R.id.vip_title);
        vipSuperBg = (ImageView) holder.itemView.findViewById(R.id.vip_super_bg);

        vipName.setText(SpUtils.getValue(context, SpKey.USER_NICK));
//        vipName.post(new Runnable() {
//            @Override
//            public void run() {
//                StringUtils.autoSplitText(vipName);
//            }
//        });

    }

    private void initView() {

    }
}
