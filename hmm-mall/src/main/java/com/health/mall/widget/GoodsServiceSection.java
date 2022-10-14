package com.health.mall.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.health.mall.R;

/**
 * @author Li
 * @date 2019/03/05 11:17
 * @des 商品详情-服务详情
 */

public class GoodsServiceSection extends FrameLayout implements View.OnClickListener {
    private TextView mTxtDetailSwitch;
    private Group mGroupDetail;
    private TextView mTxtServiceName;

    public GoodsServiceSection(@NonNull Context context) {
        this(context, null);
    }

    public GoodsServiceSection(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsServiceSection(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_section_service_goods, this);
        mTxtDetailSwitch = findViewById(R.id.txt_service_detail_switch);
        mGroupDetail = findViewById(R.id.group_detail);
        mTxtServiceName = findViewById(R.id.txt_service_name);
        mTxtDetailSwitch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int visibility = mGroupDetail.getVisibility();
        mTxtDetailSwitch.setSelected(visibility != VISIBLE);
        mTxtDetailSwitch.setText(visibility == VISIBLE ?
                R.string.service_detail_expand : R.string.service_detail_collapse);
        mGroupDetail.setVisibility(visibility == VISIBLE ? GONE : VISIBLE);

    }
}
